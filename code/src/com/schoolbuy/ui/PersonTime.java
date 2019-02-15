package com.schoolbuy.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.TimeAdapter;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;
import com.schoolbuy.view.LoadDialog;

public class PersonTime extends Activity
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
        setContentView(R.layout.person_time);
        
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
	
	public void refresh()
	{
		new Thread(new Runnable(){
		    @Override
		    public void run() {
		    // TODO Auto-generated method stub
		    	BmobQuery<Record> query = new BmobQuery<Record>();
		    	query.addWhereEqualTo("sellerID",currentUser.getUsername());
		    	query.findObjects(PersonTime.this, new FindListener<Record>() 
		    	{
		    	    @Override
		    	    public void onSuccess(List<Record> object) {
		    	        // TODO Auto-generated method stub
		    	    	if(object.size() != 0)
		    	    	{
		    	    		for (Record record : object) 
		    	    		{
		    	    			BmobQuery<Item> query1 = new BmobQuery<Item>();
		    	    			query1.getObject(PersonTime.this, record.getItemID(), new GetListener<Item>() 
		    	    			{
		    	    			    @Override
		    	    			    public void onSuccess(Item tempItem) {
		    	    			        // TODO Auto-generated method stub
		    	    			    	itemList.add(tempItem);
		    	    			    }

		    	    			    @Override
		    	    			    public void onFailure(int code, String arg0) {
		    	    			        // TODO Auto-generated method stub
		    	    			    	
		    	    			    }

		    	    			});
		    	            }
		    	    	}
		    	    	
		    	    	BmobQuery<Item> query2 = new BmobQuery<Item>();
		    	    	query2.addWhereEqualTo("owner",currentUser.getUsername());
		    	    	query2.findObjects(PersonTime.this, new FindListener<Item>() {
		    	    	    @Override
		    	    	    public void onSuccess(List<Item> object) {
		    	    	        // TODO Auto-generated method stub
		    	    	    	if(object.size() != 0)
		    	    	    	{
		    	    	    		for (Item item : object) 
		    	    	    		{
		    	        	    		itemList.add(item);
		    	    	            }   
		    	    	    	}
		    	    	    	
		    	    	    	if(itemList.size() == 0)
		    	    	    	{
		    	    	    		loadNothing.setVisibility(View.VISIBLE);
		    	    	    	}
		    	    	    	else
		    	    	    	{
		    	    	    		TimeAdapter itemAdapter = new TimeAdapter(PersonTime.this, R.layout.person_time_item, itemList);
		    	    	            itemListView = (ListView)findViewById(R.id.list);
		    	    	            itemListView.setAdapter(itemAdapter);   	
		    	    	    	}
		    	    	    	
		    	    	    }
		    	    	    @Override
		    	    	    public void onError(int code, String msg) {
		    	    	        // TODO Auto-generated method stub
		    	    	    	Toast.makeText(getApplicationContext(), "≤È—Ø ß∞‹"+msg, Toast.LENGTH_SHORT).show();
		    	    	    }
		    	    	});
		    	    	
		    	    }
		    	    @Override
		    	    public void onError(int code, String msg) {
		    	        // TODO Auto-generated method stub
		    	    	Toast.makeText(getApplicationContext(), "≤È—Ø ß∞‹"+msg, Toast.LENGTH_SHORT).show();
		    	    	loadAgainButton.setVisibility(View.VISIBLE);
		    	    }
		    	}); 
		    	loadDialog.dismiss();
				}}).start();
		
		
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
