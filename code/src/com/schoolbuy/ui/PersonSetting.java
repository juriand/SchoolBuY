package com.schoolbuy.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.example.schoolbuy.R;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;

public class PersonSetting extends Activity
{
	private List<Record> recordList = new ArrayList<Record>();
	private ListView recordListView;
	
	private Button backButton;
	private ImageButton exitButton;
	
	private Member currentUser;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_setting);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
        
        init();
        click();
    }
	
	public void init()
	{
		currentUser = BmobUser.getCurrentUser(this,Member.class);
		    	
    	backButton = (Button)findViewById(R.id.back);
    	
    	exitButton = (ImageButton)findViewById(R.id.exit);
	}
	
	public void click()
	{		
		backButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(PersonSetting.this, PersonActivity.class);
				PersonSetting.this.startActivity(myIntent);
				
				finish();
			}
		});	
		
		exitButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				currentUser.logOut(PersonSetting.this);
				
				Intent myIntent = new Intent(PersonSetting.this, MainActivity.class);
				PersonSetting.this.startActivity(myIntent);
				
				finish();
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
