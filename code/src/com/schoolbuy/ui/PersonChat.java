package com.schoolbuy.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.MessageRecentAdapter;
import com.schoolbuy.bean.Member;

public class PersonChat extends Activity implements OnItemClickListener
{ 
	private ListView listview;
	private MessageRecentAdapter adapter;
	private Button backButton;
	private ImageView loadNothing;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_chat);

    	init();
    	click();
    }
    
    public void init()
    {
    	listview = (ListView)findViewById(R.id.chatList);
    	backButton = (Button)findViewById(R.id.back);
    	loadNothing = (ImageView)findViewById(R.id.load_nothing);
    	
		listview.setOnItemClickListener(this);
		adapter = new MessageRecentAdapter(PersonChat.this, R.layout.item_conversation, BmobDB.create(PersonChat.this).queryRecents());
		listview.setAdapter(adapter);
    }
	
	public void click()
	{
		backButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});	
	}

    	@Override
    	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
    	{
    		// TODO Auto-generated method stub
    		BmobRecent recent = adapter.getItem(position);
    		//重置未读消息
    		BmobDB.create(PersonChat.this).resetUnread(recent.getTargetid());
    		
    		//组装聊天对象
    		Member user = new Member();
    		
    		user.setAvatar(recent.getAvatar());
    		user.setNick(recent.getNick());
    		user.setUsername(recent.getUserName());
    		user.setObjectId(recent.getTargetid());
    		
    		Intent intent = new Intent(PersonChat.this, ChatActivity.class);
    		intent.putExtra("user", user);
    		
    		startActivity(intent);
    	}
    	
    	@Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
            {   
            	finish();
                return true;   
            }
            return super.onKeyDown(keyCode, event);
        }
    	
}
