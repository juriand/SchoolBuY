package com.schoolbuy.ui;

import java.util.ArrayList;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.PersonItemAdapter;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.view.LoadDialog;

public class PersonItem extends Activity
{
	private List<Item> itemList = new ArrayList<Item>();
	private ListView itemListView;
	
	private Button backButton;
	
	private ImageButton loadAgainButton;
	
	private Member currentUser;
	
	private ImageView loadNothing;
	private LoadDialog loadDialog;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_item);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
        
        init();
        click();
    }
	
	public void init()
	{	
		loadNothing = (ImageView)findViewById(R.id.load_nothing);
		
		loadDialog = new LoadDialog(this,R.style.LoadDialog);
		loadDialog.show();
		
		backButton = (Button)findViewById(R.id.back);
    	loadAgainButton = (ImageButton)findViewById(R.id.loadAgain);
    	loadAgainButton.setVisibility(View.GONE);
		
		currentUser = BmobUser.getCurrentUser(this,Member.class);
		
		refresh();
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
		
		loadAgainButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				loadAgainButton.setVisibility(View.GONE);
				loadDialog.show();
				refresh();
			}
    		
    	});
	}
	
	public void refresh()
	{
		new Thread(new Runnable(){
		    @Override
		    public void run() {
		    // TODO Auto-generated method stub
		    	BmobQuery<Item> query = new BmobQuery<Item>();
		    	query.addWhereEqualTo("owner",currentUser.getUsername());
		    	query.order("-createdAt");
		    	//返回50条数据，如果不加上这条语句，默认返回10条数据
		    	query.setLimit(50);
		    	//执行查询方法
		    	query.findObjects(PersonItem.this, new FindListener<Item>() {
		    	        @Override
		    	        public void onSuccess(List<Item> object) {
		    	            // TODO Auto-generated method stub
		    	        	if(object.size() == 0)
		    	        	{
		    	        		loadNothing.setVisibility(View.VISIBLE);
		    	        	}
		    	        	else
		    	        	{
		    	        		for (Item item : object) {
		        	            	itemList.add(item);
		        	            }
		        	            Log.e("find", "success");
		        	            PersonItemAdapter itemAdapter = new PersonItemAdapter(PersonItem.this, R.layout.person_itemlist_item, itemList);
		        	        	itemListView = (ListView)findViewById(R.id.list);
		        	        	itemListView.setAdapter(itemAdapter);   	
		    	        	}	            
		    	        }
		    	        @Override
		    	        public void onError(int code, String msg) {
		    	            // TODO Auto-generated method stub
		    	        	Toast.makeText(getApplicationContext(), "查询失败"+msg, Toast.LENGTH_SHORT).show();
		    	        	loadAgainButton.setVisibility(View.VISIBLE);
		    	        }
		    	});
		    	loadDialog.dismiss();
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
