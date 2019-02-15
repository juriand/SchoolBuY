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
 * �������
 * 
 * @ClassName: ChatActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-3 ����4:33:11
 */
/**
 * @ClassName: ChatActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-23 ����3:28:49
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

	// �����й�
	RelativeLayout layout_record;
	TextView tv_voice_tips;
	ImageView iv_record;

	private Drawable[] drawable_Anims;// ��Ͳ����

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
		// ��װ�������
		targetUser = (BmobChatUser) getIntent().getSerializableExtra("user");
		targetId = targetUser.getObjectId();
//		BmobLog.i("�������" + targetUser.getUsername() + ",targetId = "
//				+ targetId);
		//ע��㲥������
		initNewMessageBroadCast();
		initView();
	}
	
	private void initRecordManager(){
		// ������ع�����
		recordManager = BmobRecordManager.getInstance(this);
		// ����������С����--�����￪���߿����Լ�ʵ�֣���ʣ��10������µĸ��û�����ʾ������΢�ŵ���������
		recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {
	
			@Override
			public void onVolumnChanged(int value) {
				// TODO Auto-generated method stub
				iv_record.setImageDrawable(drawable_Anims[value]);
			}
	
			@Override
			public void onTimeChanged(int recordTime, String localPath) {
				// TODO Auto-generated method stub
				BmobLog.i("voice", "��¼������:" + recordTime);
				if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1���ӽ�����������Ϣ
					// ��Ҫ���ð�ť
					longSpeakButton.setPressed(false);
					longSpeakButton.setClickable(false);
					// ȡ��¼����
					layout_record.setVisibility(View.INVISIBLE);
					// ����������Ϣ
					sendVoiceMessage(localPath, recordTime);
					//��Ϊ�˷�ֹ����¼��ʱ��󣬻�෢һ��������ȥ�������
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
		titleName.setText("��" + targetUser.getUsername() + "�Ի���");
		mListView = (XListView) findViewById(R.id.list);
		
		initChat();
		initXListView();
		initVoiceView();
	}

	/**
	 * ��ʼ����������
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
	 * ����˵��
	 * @ClassName: VoiceTouchListen
	 * @Description: TODO
	 * @author smile
	 * @date 2014-7-1 ����6:10:16
	 */
	class VoiceTouchListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.checkSdCard()) {
					//ShowToast("����������Ҫsdcard֧�֣�");
					return false;
				}
				try {
					v.setPressed(true);
					layout_record.setVisibility(View.VISIBLE);
					tv_voice_tips.setText("¼��ȡ��");
					// ��ʼ¼��
					recordManager.startRecording(targetId);
				} catch (Exception e) {
				}
				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					tv_voice_tips.setText("¼��ȡ��");
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
					if (event.getY() < 0) {// ����¼��
						recordManager.cancelRecording();
						BmobLog.i("voice", "������������");
					} else {
						int recordTime = recordManager.stopRecording();
						if (recordTime > 1) {
							// ���������ļ�
							BmobLog.i("voice", "��������");
							sendVoiceMessage(
									recordManager.getRecordFilePath(targetId),
									recordTime);
						} else {// ¼��ʱ����̣�����ʾ¼�����̵���ʾ
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
	 * ����������Ϣ
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
						//ShowLog("�ϴ�����ʧ�� -->arg1��" + arg1);
						mAdapter.notifyDataSetChanged();
					}
				});
	}

	/**
	 * ��ʼ������������Դ
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
	 * ������Ϣ��ʷ�������ݿ��ж���
	 */
	private List<BmobMsg> initMsgData()
	{
		List<BmobMsg> list = BmobDB.create(this).queryMessages(targetId,MsgPagerNum);
		return list;
	}

	/**
	 * ����ˢ��
	 * @Title: initOrRefresh
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initOrRefresh() {
		if (mAdapter != null) {
			if (MyMessageReceiver.mNewNum != 0) 
			{// ���ڸ��µ���������������ڼ�������Ϣ����ʱ�ٻص�����ҳ���ʱ����Ҫ��ʾ��������Ϣ
				int news=  MyMessageReceiver.mNewNum;//�п��������ڼ䣬����N����Ϣ,�����Ҫ������ʾ�ڽ�����
				int size = initMsgData().size();
				for(int i=(news-1);i>=0;i--){
					mAdapter.add(initMsgData().get(size-(i+1)));// ������һ����Ϣ��������ʾ
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
		// ���Ȳ�������ظ���
		mListView.setPullLoadEnable(false);
		// ��������
		mListView.setPullRefreshEnable(true);
		// ���ü�����
		mListView.setXListViewListener(this);
		mListView.pullRefreshing();
		mListView.setDividerHeight(0);
		// ��������
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
		case R.id.addImage:// ��ʾͼƬ
			
			break;
		case R.id.voice:// ������ť
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
		case R.id.send:// �����ı�
			final String msg = inputText.getText().toString();
			
			if (msg.equals("")) 
			{
				return;
			}
			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
			if (!isNetConnected) 
			{
				Toast.makeText(ChatActivity.this, "���粻����", Toast.LENGTH_SHORT).show();
				// return;
			}
			// ��װBmobMessage����
			BmobMsg message = BmobMsg.createTextSendMsg(this, targetId, msg);
			message.setExtra("Bmob");
			// Ĭ�Ϸ�����ɣ������ݱ��浽������Ϣ�������Ự����
			manager.sendTextMessage(targetUser, message);
			// ˢ�½���
			refreshMessage(message);
			break;
		case R.id.back://����
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
				if (!uid.equals(targetId))// ������ǵ�ǰ��������������Ϣ��������
					return;
				mAdapter.add(m);
				// ��λ
				mListView.setSelection(mAdapter.getCount() - 1);
				//ȡ����ǰ��������δ����ʾ
				BmobDB.create(ChatActivity.this).resetUnread(targetId);
			}
		}
	};

public static final int NEW_MESSAGE = 0x001;// �յ���Ϣ
	
	NewBroadcastReceiver  receiver;
	
	private void initNewMessageBroadCast()
	{
		// ע�������Ϣ�㲥
		receiver = new NewBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(BmobConfig.BROADCAST_NEW_MESSAGE);
		//���ù㲥�����ȼ������Mainacitivity,���������Ϣ����ʱ��������chatҳ�棬ֱ����ʾ��Ϣ����������ʾ��Ϣδ��
		intentFilter.setPriority(5);
		registerReceiver(receiver, intentFilter);
	}
	
	/**
	 * ����Ϣ�㲥������
	 * 
	 */
	private class NewBroadcastReceiver extends BroadcastReceiver 
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			String from = intent.getStringExtra("fromId");
			String msgId = intent.getStringExtra("msgId");
			String msgTime = intent.getStringExtra("msgTime");
			// �յ�����㲥��ʱ��message�Ѿ�����Ϣ���У���ֱ�ӻ�ȡ
			BmobMsg msg = BmobChatManager.getInstance(ChatActivity.this).getMessage(msgId, msgTime);
			if (!from.equals(targetId))// ������ǵ�ǰ��������������Ϣ��������
				return;
			//��ӵ���ǰҳ��
			mAdapter.add(msg);
			// ��λ
			mListView.setSelection(mAdapter.getCount() - 1);
			//ȡ����ǰ��������δ����ʾ
			BmobDB.create(ChatActivity.this).resetUnread(targetId);
			// �ǵðѹ㲥���ս��
			abortBroadcast();
		}
	}
	
	/**
	 * ˢ�½���
	 * @Title: refreshMessage
	 * @Description: TODO
	 * @param @param message
	 * @return void
	 * @throws
	 */

	private void refreshMessage(BmobMsg msg) {
		// ���½���
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
			Toast.makeText(ChatActivity.this, "���粻����", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAddUser(BmobInvitation invite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOffline() {
		// TODO Auto-generated method stub
		Toast.makeText(ChatActivity.this, "������", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onReaded(String conversionId, String msgTime) {
		// TODO Auto-generated method stub
		// �˴�Ӧ�ù��˵����Ǻ͵�ǰ�û�������Ļ�ִ��Ϣ�����ˢ��
		if (conversionId.split("&")[1].equals(targetId)) {
			// �޸Ľ�����ָ����Ϣ���Ķ�״̬
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
				BmobLog.i("��¼������" + total);
				int currents = mAdapter.getCount();
				if (total <= currents) {
					Toast.makeText(ChatActivity.this, "�����¼��������", Toast.LENGTH_SHORT).show();
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
