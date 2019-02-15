package com.schoolbuy.ui;

import java.util.List;

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
import android.widget.Toast;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.example.schoolbuy.R;
import com.schoolbuy.bean.Member;
import com.schoolbuy.view.LoadDialog;
import com.schoolbuy.view.MyProgressDialog;

public class RegisterActivity extends Activity
{
	private Button backButton;
	private ImageButton registerButton;
	private EditText nameText, passText, passAgainText;
	
	private MyProgressDialog progressDialog;
	BmobUserManager userManager;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
        
        userManager = BmobUserManager.getInstance(this);
        progressDialog = new MyProgressDialog(this,R.style.CustomProgressDialog);
        
        init();
        click();
     
    }
	
	public void init()
	{
		backButton = (Button)findViewById(R.id.back);
		registerButton = (ImageButton)findViewById(R.id.register);
		nameText = (EditText)findViewById(R.id.username);
		passText = (EditText)findViewById(R.id.password);
		passAgainText = (EditText)findViewById(R.id.passAgain);
	}
	
	public void click()
	{
		registerButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				progressDialog.show();
				searchNoSet();	
				
				if(!passAgainText.getText().toString().equals(passText.getText().toString()))
				{
					Toast.makeText(RegisterActivity.this, "两遍密码输入不一致", Toast.LENGTH_SHORT).show();
				}	
			}
		});
		
		backButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});		
	}
	
	public void register(final BmobFile tempFile)
	{
		new Thread(new Runnable(){
		    @Override
		    public void run() {
		    // TODO Auto-generated method stub
					String name = nameText.getText().toString();
					String pass = passText.getText().toString();
					String passAgain = passAgainText.getText().toString();
					
					//判断确认密码与密码是否一致
					if(passAgain.equals(pass))
					{
						final Member user = new Member();
						user.setUsername(name);
						user.setPassword(pass);
						user.setBuyCredit(10);
						user.setSellCredit(10);

						//设置头像
						BmobFile iconFile = tempFile;
						user.setIcon(iconFile);
						
						user.signUp(RegisterActivity.this, new SaveListener() 
						{		
							@Override
							public void onSuccess() 
							{
								// TODO Auto-generated method stub								
								Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
								Intent myIntent = new Intent(RegisterActivity.this,PersonActivity.class);
						    	RegisterActivity.this.startActivity(myIntent);
						    	
						    	finish();
							}
							
							@Override
							public void onFailure(int arg0, String arg1) 
							{
								// TODO Auto-generated method stub
								Toast.makeText(RegisterActivity.this, "注册失败:"+arg1, Toast.LENGTH_SHORT).show();
							}
						});	
					}
				progressDialog.dismiss();
				}}).start();			
	}
	

	public void searchNoSet()
	{
		//查询到默认头像文件
		BmobQuery<Member> query = new BmobQuery<Member>();
		query.addWhereEqualTo("username", "admin");
		query.findObjects(this, new FindListener<Member>() 
		{
		    @Override
		    public void onSuccess(List<Member> object) 
		    {
		        // TODO Auto-generated method stub
		    	register(object.get(0).getIcon());
		    }
		    @Override
		    public void onError(int code, String msg) 
		    {
		        // TODO Auto-generated method stub
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
