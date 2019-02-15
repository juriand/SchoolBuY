package com.schoolbuy.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.TimeLineAdapter;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;
import com.schoolbuy.view.CircleImageView;
import com.schoolbuy.view.LoadDialog;

public class PersonTimeDetail extends Activity implements OnItemClickListener
{
	private List<Record> recordList = new ArrayList<Record>();
	private ListView recordListView;
	
	private Button backButton;
	private ImageButton loadAgainButton;
	
	private Member currentUser;
	
	private ImageView background;
	private ImageView loadNothing;
	private CircleImageView itemIcon;
	
	private TextView itemName;
	
	private LoadDialog loadDialog;
	
	String itemID = "";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_time_detail);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
        
        init();
        click();
    }
	
	public void init()
	{
		background = (ImageView)findViewById(R.id.background);
		loadNothing = (ImageView)findViewById(R.id.load_nothing);
		itemIcon = (CircleImageView)findViewById(R.id.itemIcon);
		
		itemName = (TextView)findViewById(R.id.name);
		
		loadDialog = new LoadDialog(this,R.style.LoadDialog);
		loadDialog.show();
    	
		backButton = (Button)findViewById(R.id.back);
		
    	loadAgainButton = (ImageButton)findViewById(R.id.loadAgain);
    	loadAgainButton.setVisibility(View.GONE);
		
		currentUser = BmobUser.getCurrentUser(this,Member.class);
		
		//获取物品ID
		Bundle bundle = this.getIntent().getExtras();
		itemID = bundle.getString("itemID");
		
		//获取物品信息
		BmobQuery<Item> query = new BmobQuery<Item>();
    	query.addWhereEqualTo("objectId", itemID);
    	query.findObjects(PersonTimeDetail.this, new FindListener<Item>() {
    	    @Override
    	    public void onSuccess(List<Item> object) {
    	        // TODO Auto-generated method stub
    	    	object.get(0).getItemIcon().loadImage(PersonTimeDetail.this, itemIcon);
    	    	itemName.setText(object.get(0).getItemName());
    	    }
    	    @Override
    	    public void onError(int code, String msg) {
    	        // TODO Auto-generated method stub
    	    }
    	});
		
		//加载背景
		
		
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
		    	//查找该物品所有记录
				BmobQuery<Record> query = new BmobQuery<Record>();
		    	query.addWhereEqualTo("itemID", itemID);
		    	query.addWhereEqualTo("recordStatus", 1);
		    	query.order("-createdAt");
		    	query.findObjects(PersonTimeDetail.this, new FindListener<Record>() {
		    	    @Override
		    	    public void onSuccess(List<Record> object) {
		    	        // TODO Auto-generated method stub
		    	    	if(object.size() == 0)
			        	{
			        		loadNothing.setVisibility(View.VISIBLE);
			        	}
		    	    	else
		    	    	{
		    	    		for (Record item : object) {
		        	    		recordList.add(item);
		    	            }
		    	            TimeLineAdapter itemAdapter = new TimeLineAdapter(PersonTimeDetail.this, R.layout.person_time_detail_item, recordList);
		    	            recordListView = (ListView)findViewById(R.id.list);
		    	            recordListView.setAdapter(itemAdapter);   	
		    	            recordListView.setOnItemClickListener(PersonTimeDetail.this);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
