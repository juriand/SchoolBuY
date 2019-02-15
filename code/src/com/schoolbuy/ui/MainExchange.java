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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.ItemAdapter;
import com.schoolbuy.bean.Item;
import com.schoolbuy.view.LoadDialog;

public class MainExchange extends Activity implements OnItemClickListener
{ 
	private List<Item> itemList = new ArrayList<Item>();
	private ListView itemListView;
	
	private LoadDialog loadDialog;
	private ImageView loadNothing;
	private ImageButton loadAgainButton;
	
	ItemAdapter itemAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.itemlist);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
 
        init();
        click();
    }
    
    public void init()
    {
    	loadDialog = new LoadDialog(this,R.style.LoadDialog);
    	
    	loadAgainButton = (ImageButton)findViewById(R.id.loadAgain);
    	loadAgainButton.setVisibility(View.GONE);
    	
    	loadNothing = (ImageView)findViewById(R.id.load_nothing);
    	
    	loadDialog.show();
		refresh();	
    }
    
    public void click()
    {
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
		    query.addWhereEqualTo("itemStatus",2);
		    query.addWhereEqualTo("itemKind",2);
		    query.order("-createdAt");
		    //返回50条数据，如果不加上这条语句，默认返回10条数据
		    //query.setLimit(50);
		    //执行查询方法
		    query.findObjects(MainExchange.this, new FindListener<Item>() 
		    {
		    	@Override
		    	public void onSuccess(List<Item> object) 
		    	{
		    		// TODO Auto-generated method stub
		    		if(object.size() == 0)
		    	    {
		    	        loadNothing.setVisibility(View.VISIBLE);
		    	    }
		    	    else
		    	    {
		    	    	for (Item item : object) 
		    	    	{
		    	    		itemList.add(item);
		    	    	}
		    	    	itemAdapter = new ItemAdapter(MainExchange.this, R.layout.itemlist_item, itemList);
		    	    	itemListView = (ListView)findViewById(R.id.list);
		    	    	itemListView.setAdapter(itemAdapter);   	
		    	    	itemListView.setOnItemClickListener(MainExchange.this);
		    	    }
		    	}
		    	@Override
		    	public void onError(int code, String msg) 
		    	{
		    		// TODO Auto-generated method stub
		    		Toast.makeText(getApplicationContext(), "查询失败"+msg, Toast.LENGTH_SHORT).show();
		    		
		    		loadAgainButton.setVisibility(View.VISIBLE);
		    	}
		    });		
		    	loadDialog.dismiss();
				}}).start();
	}
    
    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
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
