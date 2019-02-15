package com.schoolbuy.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

import com.example.schoolbuy.R;
import com.schoolbuy.view.MyProgressDialog;


public class LogActivity extends Activity
{
	private Button backButton;
	private TextView forgetText, registerText;
	private ImageButton logButton;
	private EditText nameText, passText;
	
	private MyProgressDialog progressDialog;
	BmobUserManager userManager;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
        
        userManager = BmobUserManager.getInstance(this);
        progressDialog = new MyProgressDialog(this,R.style.CustomProgressDialog);
        
        init();
        click();
     
    }
	
	public void init()
	{
		backButton = (Button)findViewById(R.id.back);
		forgetText = (TextView)findViewById(R.id.forget); 
		registerText = (TextView)findViewById(R.id.register);
		logButton = (ImageButton)findViewById(R.id.log);
		nameText = (EditText)findViewById(R.id.username);
		passText = (EditText)findViewById(R.id.password);
	}
	
	public void click()
	{
		logButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				progressDialog.show();
				log();
			}
		});
		
		backButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		
		registerText.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(LogActivity.this,RegisterActivity.class);
				LogActivity.this.startActivity(myIntent);
			}
		});
		
		forgetText.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(LogActivity.this,ForgetActivity.class);
				LogActivity.this.startActivity(myIntent);
			}
		});
		
	}
	
	public void log()
	{
		final String username = nameText.getText().toString();
		final String password = passText.getText().toString();
		new Thread(new Runnable(){
		    @Override
		    public void run() 
		    {
		    // TODO Auto-generated method stub
		    	final BmobChatUser user = new BmobChatUser();
				user.setUsername(username);
				user.setPassword(password);
				user.login(LogActivity.this, new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						//UserInfor tempUser = BmobUser.getCurrentUser(LogActivity.this, UserInfor.class);
						userManager.bindInstallationForRegister(user.getUsername());
						
						Toast.makeText(LogActivity.this, "µÇÂ¼³É¹¦", Toast.LENGTH_SHORT).show();			
						Intent myIntent = new Intent(LogActivity.this,PersonActivity.class);
		    			LogActivity.this.startActivity(myIntent);
		    			
		    			finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(LogActivity.this, "µÇÂ¼Ê§°Ü:"+arg1, Toast.LENGTH_SHORT).show();
					}
				});
				progressDialog.dismiss();
				}}).start();
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
