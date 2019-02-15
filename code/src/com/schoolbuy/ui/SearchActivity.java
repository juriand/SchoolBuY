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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.ItemAdapter;
import com.schoolbuy.bean.Item;
import com.schoolbuy.view.LoadDialog;

public class SearchActivity extends Activity
{
	private Button searchButton;
	private EditText searchText;
	private Spinner kindSpinner;
	private LoadDialog loadDialog;
	private ImageView loadNothing;
	
	private List<String> kindList = new ArrayList<String>();       
    private ArrayAdapter<String> adapter; 
    
    private List<Item> itemList = new ArrayList<Item>();
	private ListView itemListView;
	
	BmobUserManager userManager;
	
	String sortKind="";

	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
        
        userManager = BmobUserManager.getInstance(this);
        
        init();
        click();
     
    }
	
	public void init()
	{
		loadDialog = new LoadDialog(this,R.style.LoadDialog);
		
		loadNothing = (ImageView)findViewById(R.id.load_nothing);
        
		searchButton = (Button)findViewById(R.id.searchButton);
		searchText = (EditText)findViewById(R.id.searchText);
		kindSpinner = (Spinner)findViewById(R.id.kindSpinner);
		
		kindList.add("全部");
		kindList.add("生活小物");    
		kindList.add("课程附属");
		kindList.add("学习资料");
		kindList.add("数码产品");
		kindList.add("其他");
	    
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, kindList);    
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     
        kindSpinner.setAdapter(adapter);  
        
        sortKind = adapter.getItem(0);
        
        kindSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){    
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {    
                // TODO Auto-generated method stub    
            	sortKind = adapter.getItem(arg2);
            }    
            public void onNothingSelected(AdapterView<?> arg0) {    
                // TODO Auto-generated method stub    
            	
            }    
        });  

	}
	
	public void click()
	{
		searchButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				//分词
				/*
				String text = searchText.getText().toString();  
		        Segment s = new Segment();  
		        try {  
		            System.out.println(s.segment(text));  
		        } catch (Exception e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        } 
		        */
		        
				loadDialog.show();
				final String text = searchText.getText().toString();
				
				new Thread(new Runnable(){
				    @Override
				    public void run() {
				    // TODO Auto-generated method stub	
				    	//清空上一次搜索列表
				    	itemList.clear();
				    	
				    	BmobQuery<Item> eq1 = new BmobQuery<Item>();
				    	eq1.addWhereContains("itemName", text);
				    	BmobQuery<Item> eq2 = new BmobQuery<Item>();
				    	eq2.addWhereContains("itemIntro", text);
				    	List<BmobQuery<Item>> queries = new ArrayList<BmobQuery<Item>>();
				    	queries.add(eq1);
				    	queries.add(eq2);
				    	
				    	BmobQuery<Item> mainQuery = new BmobQuery<Item>();
				    	mainQuery.or(queries);
				    	mainQuery.order("-createdAt");
				    	//返回50条数据，如果不加上这条语句，默认返回10条数据
				    	mainQuery.setLimit(50);
				    	//执行查询方法
				    	mainQuery.findObjects(SearchActivity.this, new FindListener<Item>() {
				    	        @Override
				    	        public void onSuccess(List<Item> object) 
				    	        {
				    	            // TODO Auto-generated method stub
				    	        	if(sortKind.equals("全部"))
				    	        	{
				    	        		itemList = object;
				    	        	}
				    	        	else
				    	        	{
				    	        		for (Item item : object) 
					    	            {
				    	        			if(item.getItemTag()[0].equals(sortKind))
				    	        			{
				    	        				itemList.add(item);
				    	        			}
					    	            	
					    	            }
				    	        	}

				    	        	if(itemList.size() == 0)
				    	        	{
				    	        		loadNothing.setVisibility(View.VISIBLE);
				    	        	}
				    	        	else
				    	        	{
				    	        		loadNothing.setVisibility(View.INVISIBLE);
				    	        	}
				    	        	
				    	        	ItemAdapter itemAdapter = new ItemAdapter(SearchActivity.this, R.layout.itemlist_item, itemList);
				    	        	itemListView = (ListView)findViewById(R.id.list);
				    	        	itemListView.setAdapter(itemAdapter);   					    	            			    	            
				    	        }
				    	        @Override
				    	        public void onError(int code, String msg) {
				    	            // TODO Auto-generated method stub
				    	        	Toast.makeText(getApplicationContext(), "查询失败"+msg, Toast.LENGTH_SHORT).show();
				    	        }
				    	});
				    	loadDialog.dismiss();
						}}).start();		
				
			}
		});
		
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {   
        	Intent myIntent = new Intent(SearchActivity.this,MainActivity.class);
        	SearchActivity.this.startActivity(myIntent);
			
        	finish();
            return true;   
        }
        return super.onKeyDown(keyCode, event);
    }
	
	
}
