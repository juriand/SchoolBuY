package com.schoolbuy.ui;

import cn.bmob.v3.BmobUser;

import com.example.schoolbuy.R;
import com.schoolbuy.bean.Member;
import com.schoolbuy.cropimage.CropImage;
import com.schoolbuy.view.AddChooseDialog;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends ActivityGroup
{ 
    private View seeTab,buyTab,exchangeTab,askTab,auctionTab,workTab;  
    private View[] tabs = new View[]{seeTab,buyTab,exchangeTab,askTab,auctionTab,workTab}; 
    private int[] images=new int[]{R.drawable.main_see,R.drawable.main_buy,R.drawable.main_exchange,R.drawable.main_ask, R.drawable.main_auction,R.drawable.main_work};
    private int[] imagesPress=new int[]{R.drawable.main_see_press,R.drawable.main_buy_press,R.drawable.main_exchange_press,R.drawable.main_ask_press, R.drawable.main_auction_press,R.drawable.main_work_press};
    
    private Button locationButton, personButton, addButton;
    private EditText searchText;
    
    private Member currentUser;
    
    private int touchNum = 0;
    
    private AddChooseDialog addDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        if(CropImage.instance != null)
        {
        	CropImage.instance.finish();
        }
        
        
        final TabHost tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup(this.getLocalActivityManager());   
        
        Intent intent1 = new Intent(this,MainSee.class);
        Intent intent2 = new Intent(this,MainBuy.class);
        Intent intent3 = new Intent(this,MainExchange.class);
        Intent intent4 = new Intent(this,MainWant.class);
        Intent intent5 = new Intent(this,MainAuction.class);
        Intent intent6 = new Intent(this,MainWork.class);

        Intent[] intents = {intent1,intent2,intent3,intent4,intent5,intent6};
      
        for(int i=0;i<tabs.length;i++)
        {  
            tabs[i] = (View) LayoutInflater.from(this).inflate(R.layout.main_tab_indicator, null);  
            ImageView image = (ImageView) tabs[i].findViewById(R.id.image); 
            if(i==0)
            	image.setImageResource(imagesPress[i]); 
            else
            	image.setImageResource(images[i]);   
            tabHost.addTab(tabHost.newTabSpec("tab"+i).setIndicator(tabs[i]).setContent(intents[i]));  
        } 
        
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new OnTabChangeListener()
        {
			@Override
			public void onTabChanged(String arg0) 
			{
				// TODO Auto-generated method stub
				for(int i =0;i<6;i++)
				{
					if(i == tabHost.getCurrentTab())
					{
						ImageView image = (ImageView) tabs[i].findViewById(R.id.image); 
			            image.setImageResource(imagesPress[i]);  
					}
					else
					{
						ImageView image = (ImageView) tabs[i].findViewById(R.id.image); 
			            image.setImageResource(images[i]); 
					}
				}
			}
        	
        });
        
        init();
        click();
     
    }
    
    public void init()
    {
    	addDialog = new AddChooseDialog(this);
    	
    	locationButton =  (Button)findViewById(R.id.location);
    	personButton = (Button)findViewById(R.id.person);
    	addButton = (Button)findViewById(R.id.add);
    	
    	searchText = (EditText)findViewById(R.id.searchText);
    	
    	currentUser = BmobUser.getCurrentUser(this,Member.class);
    }
    
    public void click()
    {
    	addButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//检查是否登录
		    	if(currentUser == null)
		    	{
		    		Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
		    		Intent myIntent = new Intent(MainActivity.this, LogActivity.class);
		    		MainActivity.this.startActivity(myIntent);
		    	}
		    	else
		    	{
		    		addDialog.popSelectDialog();
		    	}
				
			}
		});
    	
    	personButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Intent myIntent = new Intent(MainActivity.this,PersonActivity.class);
				MainActivity.this.startActivity(myIntent);
				
				finish();
			}
		});
    	
    	searchText.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(touchNum == 0)
				{
					touchNum++;
					Intent myIntent = new Intent(MainActivity.this,SearchActivity.class);
					MainActivity.this.startActivity(myIntent);
					
					finish();			
				}
				
				return false;
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

