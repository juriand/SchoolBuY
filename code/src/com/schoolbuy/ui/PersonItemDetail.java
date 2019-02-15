package com.schoolbuy.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.schoolbuy.R;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;

public class PersonItemDetail extends Activity
{ 
	private String[] itemTag = new String[3];
	
	private boolean isAdd = true;
	
	private ImageView photoImage, iconImage;
	
	private TextView[] itemTagText = new TextView[3];
	private TextView userName, itemprice, itemName, itemIntro, likeText, createTime, itemStatus;
	
	private Button buyButton, backButton, editButton;
	
	private Item item;
	private Member currentUser;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_item_detail);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
 
        init();
        click();
    }
    
    public void init()
    {
    	currentUser = BmobUser.getCurrentUser(this,Member.class);
    	String itemID = PersonItemDetail.this.getIntent().getExtras().getString("itemID");
    	
    	backButton = (Button)findViewById(R.id.back);
    	buyButton = (Button) findViewById(R.id.contact);
    	editButton = (Button) findViewById(R.id.delete);
    	
    	photoImage = (ImageView) findViewById(R.id.photo);
		iconImage = (ImageView) findViewById(R.id.icon);
		
		userName = (TextView) findViewById(R.id.username);
		itemprice = (TextView) findViewById(R.id.price);
		itemName = (TextView) findViewById(R.id.itemName);
		itemIntro = (TextView) findViewById(R.id.introduction);
		likeText = (TextView) findViewById(R.id.like);
		createTime = (TextView) findViewById(R.id.createTime);
		itemStatus = (TextView) findViewById(R.id.itemNowStatus);
		
		//搜索物品
		BmobQuery<Item> query = new BmobQuery<Item>();
		query.getObject(this, itemID, new GetListener<Item>() {
			    @Override
			    public void onSuccess(Item object) {
			        // TODO Auto-generated method stub
			    	item = object;
			    	
			    	createTime.setText("创建时间："+ item.getCreatedAt());
			    	if(object.getItemStatus() == 0)
			    	{
			    		itemStatus.setText("已结束交易");
			    	}
			    	
					userName.setText(item.getOwner());
					itemName.setText(item.getItemName());
					itemIntro.setText("简介："+item.getItemIntro());
					if(item.getItemKind() == 2)
					{
						itemprice.setText("交换物品："+item.getTarget());
					}
					else
					{
						itemprice.setText("¥"+item.getItemPrice());
					}
					
					//能否编辑
					if(item.getOwner().equals(currentUser+""))
					{
						editButton.setVisibility(View.VISIBLE);
						editButton.setClickable(true);
					}
					
					//加载物品图片
					photoImage.setImageResource(R.drawable.item_image_load);
					item.getItemIcon().loadImage(PersonItemDetail.this, photoImage);
					
					//加载标签	
					itemTagText[0] = (TextView) findViewById(R.id.title1);
					itemTagText[1] = (TextView) findViewById(R.id.tag2);
					itemTagText[2] = (TextView) findViewById(R.id.tag3);
					
					for(int i=0;i<3;i++)
					{
						itemTagText[i].setVisibility(View.INVISIBLE);
					}
					
					itemTag = item.getItemTag();
					for(int i=0;i<3;i++)
					{
						if(itemTag[i] != null)
						{
							itemTagText[i].setVisibility(View.VISIBLE);
							itemTagText[i].setText(itemTag[i]);
						}	       
					}
					
					//加载头像
					BmobQuery<Member> query2 = new BmobQuery<Member>();
			    	query2.addWhereEqualTo("username",item.getOwner());
			    	//执行查询方法
			    	query2.findObjects(PersonItemDetail.this, new FindListener<Member>() {
			    	        @Override
			    	        public void onSuccess(List<Member> object) {
			    	            // TODO Auto-generated method stub
			    	        	object.get(0).getIcon().loadImage(PersonItemDetail.this, iconImage);
			    	        }
			    	        @Override
			    	        public void onError(int code, String msg) {
			    	            // TODO Auto-generated method stub
			    	        	//Toast.makeText(getApplicationContext(), "查询失败"+msg, Toast.LENGTH_SHORT).show();
			    	        }
			    	});
			    }

			    @Override
			    public void onFailure(int code, String msg) {
			        // TODO Auto-generated method stub
			    	
			    }
    	});
				
				
    }
    
    public void click()
    {
    	backButton.setOnClickListener(new OnClickListener()
    	{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
    	});
    	
    	editButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//编辑信息
				LayoutInflater factory = LayoutInflater.from(PersonItemDetail.this);
                final View textEntryView = factory.inflate(R.layout.item_edit_dialog, null);
                
                ImageView itemPhoto = (ImageView)textEntryView.findViewById(R.id.top);
            	
            	TextView nameText = (TextView)textEntryView.findViewById(R.id.aboutText);
            	
            	final EditText newPrice = (EditText)textEntryView.findViewById(R.id.newPrice);
            	final EditText newIntro = (EditText)textEntryView.findViewById(R.id.newIntro);
            	final EditText newTag = (EditText)textEntryView.findViewById(R.id.newTag);
            	
            	item.getItemIcon().loadImage(PersonItemDetail.this, itemPhoto);
            	nameText.setText(item.getItemName());
            	
				new AlertDialog.Builder(PersonItemDetail.this).setTitle("编辑物品信息").
				setView(textEntryView).
				setPositiveButton("确定", new DialogInterface.OnClickListener() 
					     {
			                    public void onClick(DialogInterface dialog, int whichButton) 
			                    {
			                    	Item newItem = item;
			                    	
			                    	if(!newPrice.getText().toString().equals(""))
			                    	{
			                    		newItem.setItemPrice(Float.parseFloat(newPrice.getText().toString()));
			                    	}
			                    	
			                    	if(!newIntro.getText().toString().equals(""))
			                    	{
			                    		newItem.setItemIntro(newIntro.getText().toString());
			                    	}
			                    	
			                    	if(!newTag.getText().toString().equals(""))
			                    	{
			                    		StringTokenizer st = new StringTokenizer(newTag.getText().toString());
				                		for(int i = 0 ; st.hasMoreTokens() && i<6 ; i++)
				                		{
				                			itemTag[i] = st.nextToken();
				                		}
				                    	newItem.setItemTag(itemTag);
			                    	}
			                    	
			                    	//保存数据
			                    	newItem.update(PersonItemDetail.this, item.getObjectId(), new UpdateListener() {

			                    	    @Override
			                    	    public void onSuccess() {
			                    	        // TODO Auto-generated method stub
			                    	    	Toast.makeText(PersonItemDetail.this, "保存成功", Toast.LENGTH_SHORT).show();
			                    	    	init();
			                    	    }

			                    	    @Override
			                    	    public void onFailure(int code, String msg) {
			                    	        // TODO Auto-generated method stub
			                    	    	Toast.makeText(PersonItemDetail.this, "保存失败"+msg, Toast.LENGTH_SHORT).show();
			                    	    }
			                    	});
			                    	
			                    }
			             })
					     .setNegativeButton("取消", null).show();
			}
			
		});
    	
    	buyButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new AlertDialog.Builder(PersonItemDetail.this).setTitle("确定交易吗").
				setPositiveButton("确定", new DialogInterface.OnClickListener() 
			    {
					public void onClick(DialogInterface dialog, int whichButton) 
                    {
                    	//检查用户	                    	
                    	if( currentUser.getUsername().equals(item.getOwner()))
                    	{
                    		Toast.makeText(PersonItemDetail.this, "不能交易自己的商品", Toast.LENGTH_SHORT).show();
                    		
                    		isAdd = false;    		
                    	}
                    	
                    	//检查交易状态
                    	BmobQuery<Record> query = new BmobQuery<Record>();
                    	query.addWhereEqualTo("sellerID",item.getOwner());
                    	//执行查询方法
                    	query.findObjects(PersonItemDetail.this, new FindListener<Record>() {
                    	        @Override
                    	        public void onSuccess(List<Record> object) {
                    	            // TODO Auto-generated method stub
                    	        	for(int i=0;object.size() != 0 && i <object.size();i++)
                    	        	{
                    	        		Record cmpRecord = object.get(i);
	                    	        	
	                    	        	if(cmpRecord.getItemID().equals(item.getObjectId())&&
	                    	        			cmpRecord.getBuyerID().equals(currentUser.getUsername()) &&
	                    	        			cmpRecord.getSellerID().equals(item.getOwner()) &&
	                    	        			cmpRecord.getRecordStatus() != 1 &&
	                    	        			cmpRecord.getRecordStatus() != 0)
	                    	        	{
	                    	        		Toast.makeText(PersonItemDetail.this, "交易记录已存在", Toast.LENGTH_SHORT).show();
	                    	        		Log.e("add1", isAdd+"");
	                    	        		isAdd = false;
	                    	        	}
                    	        	}
                    	        	
                    	        	//进入交易状态
			                    	if(isAdd == true)
			                    	{
			                    		Date currentTime = new Date();
				                    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				                    	String dateString = formatter.format(currentTime);
				                    	
				                    	Record tempRecord = new Record(item.getObjectId(), currentUser.getUsername(), item.getOwner(), dateString, 4);
				                    	
				                    	tempRecord.save(PersonItemDetail.this, new SaveListener(){
				                    	    @Override
				                    	    public void onSuccess() {
				                    	        // TODO Auto-generated method stub		                    	    	
				                    	    	Toast.makeText(PersonItemDetail.this, "添加交易记录成功", Toast.LENGTH_SHORT).show();
				                    	    	
				                    	    	Intent myIntent = new Intent(PersonItemDetail.this,PersonRecord.class);
				                    	    	PersonItemDetail.this.startActivity(myIntent);
				                    	    }

				                    	    @Override
				                    	    public void onFailure(int code, String msg) {
				                    	        // TODO Auto-generated method stub
				                    	    	Toast.makeText(PersonItemDetail.this, "添加交易记录失败"+msg, Toast.LENGTH_SHORT).show();
				                    	    }
				                    	});
			                    	} 
			                    	
			                    	isAdd = true;
                    	        	
                    	        }
                    	        @Override
                    	        public void onError(int code, String msg) {
                    	            // TODO Auto-generated method stub
                    	        	//Toast.makeText(getApplicationContext(), "查询失败"+msg, Toast.LENGTH_SHORT).show();
                    	        }
                    	});
                    	
                    }
			     }).setNegativeButton("取消", null).show();
				
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
