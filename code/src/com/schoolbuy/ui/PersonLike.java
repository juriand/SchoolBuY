package com.schoolbuy.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.PersonLikeAdapter;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.view.LoadDialog;

public class PersonLike extends Activity
{
	private List<Item> itemList = new ArrayList<Item>();
	private ListView itemListView;
	PersonLikeAdapter itemAdapter;
	
	private Button backButton, deleteButton;
	
	private ImageButton loadAgainButton;

	private Member currentUser;
	
	private ImageView loadNothing;
	private LoadDialog loadDialog;
	
	private ArrayList<Integer> deleteList;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_like);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
        
        init();
        click();
    }
	
	public void init()
	{	
		itemAdapter = new PersonLikeAdapter(PersonLike.this, R.layout.person_like_item, itemList);
		loadNothing = (ImageView)findViewById(R.id.load_nothing);
		
		loadDialog = new LoadDialog(this,R.style.LoadDialog);
		loadDialog.show();
		
		backButton = (Button)findViewById(R.id.back);
		deleteButton = (Button)findViewById(R.id.delete);
		
    	loadAgainButton = (ImageButton)findViewById(R.id.loadAgain);
    	loadAgainButton.setVisibility(View.GONE);
		
		currentUser = BmobUser.getCurrentUser(this,Member.class);
		
		deleteList = new ArrayList<Integer>();
		
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
		
		deleteButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				itemAdapter.setDeleteMode(false);
				
				//检查item
				for(int i=0;i<itemListView.getCount();i++)
				{
					View tempItemView = itemListView.getChildAt(i);
					
					CheckBox checkDeleteBox = (CheckBox) tempItemView.findViewById(R.id.checkDelete);
					if(checkDeleteBox.isChecked() == true)
					{
						deleteList.add(i);
					}
				}
				
				//移除item
				for(int i:deleteList)
				{
					itemList.remove(i);
				}
				
				//保存数据
				ArrayList<String> newLike = new ArrayList<String>();			
				for(Item i:itemList)
					newLike.add(i.getObjectId());
				currentUser.setLike(newLike);
				
				Member tempUser = currentUser;
				tempUser.update(PersonLike.this, currentUser.getObjectId(), new UpdateListener(){
            	    @Override
            	    public void onSuccess() {
            	        // TODO Auto-generated method stub		                    	    	
            	    	Toast.makeText(PersonLike.this, "保存成功", Toast.LENGTH_SHORT).show();
            	    	
            	    	deleteButton.setVisibility(View.GONE);
            	    	deleteButton.setClickable(false);
            	    	
            	    	itemList.clear();
            	    	refresh();
            	    }

            	    @Override
            	    public void onFailure(int code, String msg) {
            	        // TODO Auto-generated method stub
            	    	Toast.makeText(PersonLike.this, "保存失败"+msg, Toast.LENGTH_SHORT).show();
            	    }
            	});
				
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
		    	for (final String item : currentUser.getLike()) 
				{
					BmobQuery<Item> query = new BmobQuery<Item>();
					query.getObject(PersonLike.this, item, new GetListener<Item>() {
					    @Override
					    public void onSuccess(Item object) {
					        // TODO Auto-generated method stub
					    	
					    	itemList.add(object);
					    	
					    	if(item == currentUser.getLike().get(currentUser.getLike().size()-1))
					    	{
					    		if(itemList.size() == 0)
					    		{
					    			loadNothing.setVisibility(View.VISIBLE);
					    		}
					    		else
					    		{
					    			
					    	    	itemListView = (ListView)findViewById(R.id.list);
					    	    	itemListView.setAdapter(itemAdapter);
					    	    	itemListView.setOnItemLongClickListener(new OnItemLongClickListener(){
					    	    		
										@Override
										public boolean onItemLongClick(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											// TODO Auto-generated method stub
											itemAdapter.setDeleteMode(true);
											deleteButton.setVisibility(View.VISIBLE);
											deleteButton.setClickable(true);
											
											for(int i=0;i<itemListView.getCount();i++)
											{
												View tempItemView = itemListView.getChildAt(i);
												
												CheckBox checkDeleteBox = (CheckBox) tempItemView.findViewById(R.id.checkDelete);
												checkDeleteBox.setVisibility(View.VISIBLE);
											}
											
											return true;
										}
					    	    		
					    	    	});
					    		}
					    	}
					    }

					    @Override
					    public void onFailure(int code, String msg) {
					        // TODO Auto-generated method stub
					    	loadAgainButton.setVisibility(View.VISIBLE);
					    }
					});
		        }
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
