package com.schoolbuy.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.BmobUser;

import com.example.schoolbuy.R;
import com.readystatesoftware.viewbadger.BadgeView;
import com.schoolbuy.bean.Member;
import com.schoolbuy.view.AddChooseDialog;

public class PersonActivity extends Activity
{
	private Button backButton, creditButton, messButton, timeButton, homeButton, addButton;
	private ImageButton likeButton, settingButton, recordButton, itemButton, logButton, registerButton;
	private TextView registerText, nameText;
	private ImageView iconImage;
	
	private Member currentUser;
	
	private AddChooseDialog addDialog;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_person);
        
        init();
        click();
    }
	
	public void init()
	{
		addDialog = new AddChooseDialog(this);
		
		homeButton =  (Button)findViewById(R.id.home);
		addButton =  (Button)findViewById(R.id.add);
		
		creditButton =  (Button)findViewById(R.id.credit);
		messButton =  (Button)findViewById(R.id.message);
		timeButton =  (Button)findViewById(R.id.time);
		
		//显示提醒
		if(BmobDB.create(PersonActivity.this).getAllUnReadCount() != 0)
		{
			messButton.setBackgroundResource(R.drawable.noread);
		}

		timeButton.setBackgroundResource(R.drawable.noread);
		
		likeButton =  (ImageButton)findViewById(R.id.like);
		settingButton =  (ImageButton)findViewById(R.id.setting);
		recordButton =  (ImageButton)findViewById(R.id.aboutButton);
		itemButton =  (ImageButton)findViewById(R.id.item);
		
		logButton =  (ImageButton)findViewById(R.id.log);
		registerText = (TextView)findViewById(R.id.register);
		
		currentUser = BmobUser.getCurrentUser(this,Member.class);
		if(currentUser != null)
		{
			//设为不可见
			logButton.setVisibility(0x00000004);	
			registerText.setVisibility(0x00000004);
			
			//禁止点击
			logButton.setClickable(false);
			registerText.setClickable(false);
			
			iconImage = (ImageView)findViewById(R.id.icon);
			currentUser.getIcon().loadImage(this, iconImage);
			
			nameText = (TextView)findViewById(R.id.name);
			nameText.setText(currentUser.getUsername());
		}
		
	}
	
	public void click()
	{
		logButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(PersonActivity.this,LogActivity.class);
				PersonActivity.this.startActivity(myIntent);
				
				//finish();
			}
		});
		
		registerText.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(PersonActivity.this,RegisterActivity.class);
				PersonActivity.this.startActivity(myIntent);
				
				//finish();
			}
		});
		
		homeButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(PersonActivity.this,MainActivity.class);
				PersonActivity.this.startActivity(myIntent);
				
				finish();
			}
		});
		
		addButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//检查是否登录
				if(currentUser == null)
				{
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(PersonActivity.this, LogActivity.class);
					PersonActivity.this.startActivity(myIntent);
				}
				else
				{
					addDialog.popSelectDialog();
				}
				
			}
		});
		
		itemButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//检查是否登录
				if(currentUser == null)
				{
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(PersonActivity.this, LogActivity.class);
					PersonActivity.this.startActivity(myIntent);
				}
				else
				{
					Intent myIntent = new Intent(PersonActivity.this,PersonItem.class);
					PersonActivity.this.startActivity(myIntent);
				}
				
			}
		});
		
		recordButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//检查是否登录
				if(currentUser == null)
				{
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(PersonActivity.this, LogActivity.class);
					PersonActivity.this.startActivity(myIntent);
				}
				else
				{
					Intent myIntent = new Intent(PersonActivity.this,PersonRecord.class);
					PersonActivity.this.startActivity(myIntent);
				}
				
			}
		});
		
		likeButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//检查是否登录
				if(currentUser == null)
				{
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(PersonActivity.this, LogActivity.class);
					PersonActivity.this.startActivity(myIntent);
				}
				else
				{
					Intent myIntent = new Intent(PersonActivity.this,PersonLike.class);
					PersonActivity.this.startActivity(myIntent);
				}
				
			}
		});
		
		settingButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(PersonActivity.this,PersonSetting.class);
				PersonActivity.this.startActivity(myIntent);
				
				//finish();
			}
		});
		
		messButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//检查是否登录
				if(currentUser == null)
				{
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(PersonActivity.this, LogActivity.class);
					PersonActivity.this.startActivity(myIntent);
				}
				else
				{
					Intent myIntent = new Intent(PersonActivity.this,PersonChat.class);
					PersonActivity.this.startActivity(myIntent);
				}
				
			}
		});
		
		timeButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//检查是否登录
				if(currentUser == null)
				{
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(PersonActivity.this, LogActivity.class);
					PersonActivity.this.startActivity(myIntent);
				}
				else
				{
					Intent myIntent = new Intent(PersonActivity.this,PersonTime.class);
					PersonActivity.this.startActivity(myIntent);
				}
				
			}
		});
		
		creditButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//检查是否登录
				if(currentUser == null)
				{
					Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(PersonActivity.this, LogActivity.class);
					PersonActivity.this.startActivity(myIntent);
				}
				else
				{
					Intent myIntent = new Intent(PersonActivity.this,PersonCredit.class);
					PersonActivity.this.startActivity(myIntent);
				}
				
			}
		});
	}
	
	private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis()-exitTime) > 2000){  
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
                exitTime = System.currentTimeMillis();   
            } else {
                finish();
                System.exit(0);
            }
            return true;   
        }
        return super.onKeyDown(keyCode, event);
    }
	
	
}
