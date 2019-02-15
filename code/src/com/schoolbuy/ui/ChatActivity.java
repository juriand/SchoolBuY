package com.schoolbuy.ui;

import java.util.EventListener;
import java.util.List;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.MessageChatAdapter;
import com.schoolbuy.demo.MyMessageReceiver;
import com.schoolbuy.utils.CommonUtils;
import com.schoolbuy.view.XListView;
import com.schoolbuy.view.XListView.IXListViewListener;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobRecordManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.OnRecordChangeListener;
import cn.bmob.im.inteface.UploadListener;
import cn.bmob.im.util.BmobLog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 聊天界面
 * 
 * @ClassName: ChatActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-3 下午4:33:11
 */
/**
 * @ClassName: ChatActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-23 下午3:28:49
 */
@SuppressLint({ "ClickableViewAccessibility", "InflateParams" })
public class ChatActivity extends Activity implements OnClickListener,IXListViewListener, EventListener 
{
	XListView mListView;
	
	EditText inputText;

	String targetId = "";
	
	BmobChatUser targetUser;

	private static int MsgPagerNum;

	private ImageButton sendButton,longSpeakButton, addImageButton,voiceButton;
	private ImageView inputImage;
	
	private Button backButton;
	
	MessageChatAdapter mAdapter;

	// 语音有关
	RelativeLayout layout_record;
	TextView tv_voice_tips;
	ImageView iv_record;

	private Drawable[] drawable_Anims;// 话筒动画

	BmobRecordManager recordManager;
	BmobChatManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		
		BmobChat.getInstance(this).startPollService(10);
		manager = BmobChatManager.getInstance(this);
		MsgPagerNum = 0;
		// 组装聊天对象
		targetUser = (BmobChatUser) getIntent().getSerializableExtra("user");
		targetId = targetUser.getObjectId();
//		BmobLog.i("聊天对象：" + targetUser.getUsername() + ",targetId = "
//				+ targetId);
		//注册广播接收器
		initNewMessageBroadCast();
		initView();
	}
	
	private void initRecordManager(){
		// 语音相关管理器
		recordManager = BmobRecordManager.getInstance(this);
		// 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
		recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {
	
			@Override
			public void onVolumnChanged(int value) {
				// TODO Auto-generated method stub
				iv_record.setImageDrawable(drawable_Anims[value]);
			}
	
			@Override
			public void onTimeChanged(int recordTime, String localPath) {
				// TODO Auto-generated method stub
				BmobLog.i("voice", "已录音长度:" + recordTime);
				if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
					// 需要重置按钮
					longSpeakButton.setPressed(false);
					longSpeakButton.setClickable(false);
					// 取消录音框
					layout_record.setVisibility(View.INVISIBLE);
					// 发送语音消息
					sendVoiceMessage(localPath, recordTime);
					//是为了防止过了录音时间后，会多发一条语音出去的情况。
					handler.postDelayed(new Runnable() {
	
						@Override
						public void run() {
							// TODO Auto-generated method stub
							longSpeakButton.setClickable(true);
						}
					}, 1000);
				}else{
					
				}
			}
		});
	}

	private void initView() 
	{
		TextView titleName = (TextView) findViewById(R.id.name);
		titleName.setText("与" + targetUser.getUsername() + "对话中");
		mListView = (XListView) findViewById(R.id.list);
		
		initChat();
		initXListView();
		initVoiceView();
	}

	/**
	 * 初始化语音布局
	 * 
	 * @Title: initVoiceView
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initVoiceView() {
		layout_record = (RelativeLayout) findViewById(R.id.layout_record);
		tv_voice_tips = (TextView) findViewById(R.id.tv_voice_tips);
		iv_record = (ImageView) findViewById(R.id.iv_record);
		longSpeakButton.setOnTouchListener(new VoiceTouchListen());
		initVoiceAnimRes();
		initRecordManager();
	}

	/**
	 * 长按说话
	 * @ClassName: VoiceTouchListen
	 * @Description: TODO
	 * @author smile
	 * @date 2014-7-1 下午6:10:16
	 */
	class VoiceTouchListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.checkSdCard()) {
					//ShowToast("发送语音需要sdcard支持！");
					return false;
				}
				try {
					v.setPressed(true);
					layout_record.setVisibility(View.VISIBLE);
					tv_voice_tips.setText("录音取消");
					// 开始录音
					recordManager.startRecording(targetId);
				} catch (Exception e) {
				}
				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					tv_voice_tips.setText("录音取消");
					tv_voice_tips.setTextColor(Color.RED);
				} else {
					tv_voice_tips.setText(getString(R.string.voice_up_tips));
					tv_voice_tips.setTextColor(Color.WHITE);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				layout_record.setVisibility(View.INVISIBLE);
				try {
					if (event.getY() < 0) {// 放弃录音
						recordManager.cancelRecording();
						BmobLog.i("voice", "放弃发送语音");
					} else {
						int recordTime = recordManager.stopRecording();
						if (recordTime > 1) {
							// 发送语音文件
							BmobLog.i("voice", "发送语音");
							sendVoiceMessage(
									recordManager.getRecordFilePath(targetId),
									recordTime);
						} else {// 录音时间过短，则提示录音过短的提示
							layout_record.setVisibility(View.GONE);
							//showShortToast().show();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return true;
			default:
				return false;
			}
		}
	}
	
	/**
	 * 发送语音消息
	 * @Title: sendImageMessage
	 * @Description: TODO
	 * @param @param localPath
	 * @return void
	 * @throws
	 */
	private void sendVoiceMessage(String local, int length) {
		manager.sendVoiceMessage(targetUser, local, length,
				new UploadListener() {

					@Override
					public void onStart(BmobMsg msg) {
						// TODO Auto-generated method stub
						refreshMessage(msg);
					}

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						mAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(int error, String arg1) {
						// TODO Auto-generated method stub
						//ShowLog("上传语音失败 -->arg1：" + arg1);
						mAdapter.notifyDataSetChanged();
					}
				});
	}

	/**
	 * 初始化语音动画资源
	 * @Title: initVoiceAnimRes
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initVoiceAnimRes() {
		drawable_Anims = new Drawable[] {
				getResources().getDrawable(R.drawable.chat_icon_voice2),
				getResources().getDrawable(R.drawable.chat_icon_voice3),
				getResources().getDrawable(R.drawable.chat_icon_voice4),
				getResources().getDrawable(R.drawable.chat_icon_voice5),
				getResources().getDrawable(R.drawable.chat_icon_voice6) };
	}

	/**
	 * 加载消息历史，从数据库中读出
	 */
	private List<BmobMsg> initMsgData()
	{
		List<BmobMsg> list = BmobDB.create(this).queryMessages(targetId,MsgPagerNum);
		return list;
	}

	/**
	 * 界面刷新
	 * @Title: initOrRefresh
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initOrRefresh() {
		if (mAdapter != null) {
			if (MyMessageReceiver.mNewNum != 0) 
			{// 用于更新当在聊天界面锁屏期间来了消息，这时再回到聊天页面的时候需要显示新来的消息
				int news=  MyMessageReceiver.mNewNum;//有可能锁屏期间，来了N条消息,因此需要倒叙显示在界面上
				int size = initMsgData().size();
				for(int i=(news-1);i>=0;i--){
					mAdapter.add(initMsgData().get(size-(i+1)));// 添加最后一条消息到界面显示
				}
				mListView.setSelection(mAdapter.getCount() - 1);
			} else {
				mAdapter.notifyDataSetChanged();
			}
		} else {
			mAdapter = new MessageChatAdapter(this, initMsgData());
			mListView.setAdapter(mAdapter);
		}
	}


	private void initChat() 
	{
		sendButton = (ImageButton) findViewById(R.id.send);
		longSpeakButton = (ImageButton) findViewById(R.id.speak);
		addImageButton = (ImageButton) findViewById(R.id.addImage);
		voiceButton = (ImageButton) findViewById(R.id.voice);
		inputText = (EditText) findViewById(R.id.chatText);
		inputImage = (ImageView)findViewById(R.id.inputImage);
		backButton = (Button) findViewById(R.id.back);
		
		inputText.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		addImageButton.setOnClickListener(this);
		voiceButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
	}

	private void initXListView() 
	{
		// 首先不允许加载更多
		mListView.setPullLoadEnable(false);
		// 允许下拉
		mListView.setPullRefreshEnable(true);
		// 设置监听器
		mListView.setXListViewListener(this);
		mListView.pullRefreshing();
		mListView.setDividerHeight(0);
		// 加载数据
		initOrRefresh();
		mListView.setSelection(mAdapter.getCount() - 1);
		mListView.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				/*
				hideSoftInputView();
				layout_more.setVisibility(View.GONE);
				layout_add.setVisibility(View.GONE);
				btn_chat_voice.setVisibility(View.VISIBLE);
				btn_chat_keyboard.setVisibility(View.GONE);
				btn_chat_send.setVisibility(View.GONE);
				*/
				return false;
			}
		});
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) 
		{
		case R.id.addImage:// 显示图片
			
			break;
		case R.id.voice:// 语音按钮
			if(longSpeakButton.getVisibility() == View.VISIBLE)
			{
				inputImage.setVisibility(View.VISIBLE);
				inputText.setVisibility(View.VISIBLE);
				longSpeakButton.setVisibility(View.INVISIBLE);
			}
			else
			{
				inputImage.setVisibility(View.INVISIBLE);
				inputText.setVisibility(View.INVISIBLE);
				longSpeakButton.setVisibility(View.VISIBLE);				
			}		
			break;
		case R.id.send:// 发送文本
			final String msg = inputText.getText().toString();
			
			if (msg.equals("")) 
			{
				return;
			}
			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
			if (!isNetConnected) 
			{
				Toast.makeText(ChatActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
				// return;
			}
			// 组装BmobMessage对象
			BmobMsg message = BmobMsg.createTextSendMsg(this, targetId, msg);
			message.setExtra("Bmob");
			// 默认发送完成，将数据保存到本地消息表和最近会话表中
			manager.sendTextMessage(targetUser, message);
			// 刷新界面
			refreshMessage(message);
			break;
		case R.id.back://返回
			finish();
		default:
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == NEW_MESSAGE) {
				BmobMsg message = (BmobMsg) msg.obj;
				String uid = message.getBelongId();
				BmobMsg m = BmobChatManager.getInstance(ChatActivity.this).getMessage(message.getConversationId(), message.getMsgTime());
				if (!uid.equals(targetId))// 如果不是当前正在聊天对象的消息，不处理
					return;
				mAdapter.add(m);
				// 定位
				mListView.setSelection(mAdapter.getCount() - 1);
				//取消当前聊天对象的未读标示
				BmobDB.create(ChatActivity.this).resetUnread(targetId);
			}
		}
	};

public static final int NEW_MESSAGE = 0x001;// 收到消息
	
	NewBroadcastReceiver  receiver;
	
	private void initNewMessageBroadCast()
	{
		// 注册接收消息广播
		receiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		//设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
		intentFilter.setPriority(5);
		registerReceiver(receiver, intentFilter);
	}
	
	/**
	 * 新消息广播接收者
	 * 
	 */
	private class NewBroadcastReceiver extends BroadcastReceiver 
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			String from = intent.getStringExtra("fromId");
			String msgId = intent.getStringExtra("msgId");
			String msgTime = intent.getStringExtra("msgTime");
			// 收到这个广播的时候，message已经在消息表中，可直接获取
			BmobMsg msg = BmobChatManager.getInstance(ChatActivity.this).getMessage(msgId, msgTime);
			if (!from.equals(targetId))// 如果不是当前正在聊天对象的消息，不处理
				return;
			//添加到当前页面
			mAdapter.add(msg);
			// 定位
			mListView.setSelection(mAdapter.getCount() - 1);
			//取消当前聊天对象的未读标示
			BmobDB.create(ChatActivity.this).resetUnread(targetId);
			// 记得把广播给终结掉
			abortBroadcast();
		}
	}
	
	/**
	 * 刷新界面
	 * @Title: refreshMessage
	 * @Description: TODO
	 * @param @param message
	 * @return void
	 * @throws
	 */

	private void refreshMessage(BmobMsg msg) {
		// 更新界面
		mAdapter.add(msg);
		mListView.setSelection(mAdapter.getCount() - 1);
		inputText.setText("");
	}

	
	@Override
	public void onMessage(BmobMsg message) {
		// TODO Auto-generated method stub
		Message handlerMsg = handler.obtainMessage(NEW_MESSAGE);
		handlerMsg.obj = message;
		handler.sendMessage(handlerMsg);
	}

	@Override
	public void onNetChange(boolean isNetConnected) {
		// TODO Auto-generated method stub
		if (!isNetConnected) {
			Toast.makeText(ChatActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAddUser(BmobInvitation invite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOffline() {
		// TODO Auto-generated method stub
		Toast.makeText(ChatActivity.this, "不在线", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onReaded(String conversionId, String msgTime) {
		// TODO Auto-generated method stub
		// 此处应该过滤掉不是和当前用户的聊天的回执消息界面的刷新
		if (conversionId.split("&")[1].equals(targetId)) {
			// 修改界面上指定消息的阅读状态
			for (BmobMsg msg : mAdapter.getList()) {
				if (msg.getConversationId().equals(conversionId)
						&& msg.getMsgTime().equals(msgTime)) {
					msg.setStatus(BmobConfig.STATUS_SEND_RECEIVERED);
				}
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	public void onRefresh() {
		// TODO Auto-generated method stub
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MsgPagerNum++;
				int total = BmobDB.create(ChatActivity.this).queryChatTotalCount(targetId);
				BmobLog.i("记录总数：" + total);
				int currents = mAdapter.getCount();
				if (total <= currents) {
					Toast.makeText(ChatActivity.this, "聊天记录加载完了", Toast.LENGTH_SHORT).show();
				} else {
					List<BmobMsg> msgList = initMsgData();
					mAdapter.setList(msgList);
					mListView.setSelection(mAdapter.getCount() - currents - 1);
				}
				mListView.stopRefresh();
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			ChatActivity.this.onDestroy();
			finish();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//hideSoftInputView();
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
		}
		
	}

}
