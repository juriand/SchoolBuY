package com.schoolbuy.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RatingBar;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.MessageRecentAdapter;
import com.schoolbuy.bean.Member;

public class PersonCredit extends Activity
{ 
	private Button backButton;
	private RatingBar buyRating, sellRating;
	
	private Member currentUser;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_credit);

    	init();
    	click();
    }
    
    public void init()
    {
    	currentUser = BmobUser.getCurrentUser(this,Member.class);
    	
    	buyRating = (RatingBar)findViewById(R.id.buyRating);
    	sellRating = (RatingBar)findViewById(R.id.sellRating);
    	
    	backButton = (Button)findViewById(R.id.back);
    	
    	//设置信用度
    	buyRating.setRating(currentUser.getBuyCredit()); 	
    	sellRating.setRating(currentUser.getSellCredit());
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
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
            {   
            	finish();
                return true;   
            }
            return super.onKeyDown(keyCode, event);
        }
    	
}
