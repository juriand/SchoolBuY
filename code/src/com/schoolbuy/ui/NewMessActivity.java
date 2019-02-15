package com.schoolbuy.ui;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.example.schoolbuy.R;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Want;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewMessActivity extends Activity
{
	private ImageButton sendButton;
	private EditText nameText, introText;

	public static NewMessActivity instance = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_mess);
        instance = this;
        
        init();
        click();
     
    }
	
	public void init()
	{
		sendButton = (ImageButton)findViewById(R.id.send);
		
		nameText = (EditText)findViewById(R.id.inputName);
		introText = (EditText)findViewById(R.id.inputIntro);
	}
	
    public void click() 
    {
    	sendButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if(nameText.getText().toString().equals(""))
				{
					Toast.makeText(NewMessActivity.this, "需要完整的物品信息", Toast.LENGTH_SHORT).show();
				}
				else
				{
					//发布
					Want newWant = new Want(BmobUser.getCurrentUser(NewMessActivity.this,Member.class).getUsername(), 
							nameText.getText().toString(),
							introText.getText().toString());
					
					newWant.save(NewMessActivity.this, new SaveListener() {
					    @Override
					    public void onSuccess() {
					        // TODO Auto-generated method stub
					    	Toast.makeText(NewMessActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
					    	
					    	Intent myIntent = new Intent(NewMessActivity.this,MainActivity.class);
					    	NewMessActivity.this.startActivity(myIntent);
					    	
					    	finish();
					    }

					    @Override
					    public void onFailure(int code, String msg) {
					        // TODO Auto-generated method stub
					    	Toast.makeText(NewMessActivity.this, "发布失败"+msg, Toast.LENGTH_SHORT).show();
					    }
					});
				}
				
			}
		});

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
