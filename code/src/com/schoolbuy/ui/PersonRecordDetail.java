package com.schoolbuy.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.schoolbuy.R;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;

public class PersonRecordDetail extends Activity
{ 
	ImageView itemImage, recordIcon, userImage;
	TextView userName, itemStatus, itemPrice, itemName, itemKind, itemIntro, waitText, recordIDText, startTime, endTime;
	Button sellAgainButton, cancelButton, chatButton, backButton;
	ImageButton confirmButton;
	
	private Record recordItem;
	private Member currentUser;
	private String userID="";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_record_detail);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
 
        init();
        click();
    }
    
    public void init()
    {
    	currentUser = BmobUser.getCurrentUser(this,Member.class);
    	String recordID = this.getIntent().getExtras().getString("ID");
    	
    	itemImage = (ImageView) findViewById(R.id.itemImage);
    	userImage = (ImageView) findViewById(R.id.userImage);
		recordIcon = (ImageView) findViewById(R.id.recordIcon);
		
		userName = (TextView) findViewById(R.id.timeNumber);
		itemPrice = (TextView) findViewById(R.id.itemPrice);
		itemName = (TextView) findViewById(R.id.itemName);
		itemStatus = (TextView) findViewById(R.id.status);
		itemKind =  (TextView) findViewById(R.id.itemKind);
		itemIntro = (TextView) findViewById(R.id.itemIntro);
		waitText = (TextView) findViewById(R.id.waitText);
		recordIDText = (TextView) findViewById(R.id.recordID);
		startTime = (TextView) findViewById(R.id.startTime);
		endTime = (TextView) findViewById(R.id.endTime);
		
		sellAgainButton = (Button) findViewById(R.id.sellAgain);
		cancelButton = (Button) findViewById(R.id.cancel);
		chatButton = (Button) findViewById(R.id.chat);
		backButton = (Button)findViewById(R.id.back);
		
		confirmButton = (ImageButton) findViewById(R.id.confirm);	
		
		//查找记录
		BmobQuery<Record> query = new BmobQuery<Record>();
		query.getObject(this, recordID, new GetListener<Record>() {
			    @Override
			    public void onSuccess(Record object) {
			        // TODO Auto-generated method stub
			    	recordItem = object;
			    	
			    	if(recordItem.getRecordStatus() == 0 || recordItem.getRecordStatus() == 1)
					{
						itemStatus.setText("交易结束");
						
						//交易结束后不显示取消、确认、等待
						cancelButton.setVisibility(View.INVISIBLE);
						cancelButton.setClickable(false);
						
						confirmButton.setVisibility(View.INVISIBLE);
						confirmButton.setClickable(false);
						waitText.setVisibility(View.INVISIBLE);

						if(recordItem.getBuyerID().equals(currentUser.getUsername()) && recordItem.getRecordStatus() == 1)
						{
							sellAgainButton.setVisibility(View.VISIBLE);
							sellAgainButton.setClickable(true);
						}

					}
					else 
					{
						itemStatus.setText("正在交易");
						
						cancelButton.setVisibility(View.VISIBLE);
						cancelButton.setClickable(true);
					}	
					
					if(recordItem.getSellerID().equals(currentUser.getUsername()))
					{
						recordIcon.setImageResource(R.drawable.person_record_sell);
						userName.setText(recordItem.getBuyerID());
						if(recordItem.getRecordStatus() ==  3)
						{
							waitText.setVisibility(View.VISIBLE);
							
							confirmButton.setVisibility(View.INVISIBLE);
							confirmButton.setClickable(false);
						}
						else if((recordItem.getRecordStatus() ==  2) || (recordItem.getRecordStatus() ==  4))
						{
							confirmButton.setVisibility(View.VISIBLE);
							confirmButton.setClickable(true);
						}
					}
					else if(recordItem.getBuyerID().equals(currentUser.getUsername()))
					{
						recordIcon.setImageResource(R.drawable.person_record_buy);
						userName.setText(recordItem.getSellerID());
						if(recordItem.getRecordStatus() ==  2)
						{
							waitText.setVisibility(View.VISIBLE);
							
							confirmButton.setVisibility(View.INVISIBLE);
							confirmButton.setClickable(false);
						}
						else if((recordItem.getRecordStatus() ==  3) || (recordItem.getRecordStatus() ==  4))
						{
							confirmButton.setVisibility(View.VISIBLE);
							confirmButton.setClickable(true);
						}
					}
					
					//查询物品信息
					BmobQuery<Item> query = new BmobQuery<Item>();
					query.getObject(PersonRecordDetail.this, recordItem.getItemID(), new GetListener<Item>() 
					{
					    @Override
					    public void onSuccess(Item object) 
					    {
					        // TODO Auto-generated method stub
					    	if(object.getItemKind() == 2)
					    	{
					    		itemPrice.setText("交换物品：¥"+object.getTarget());
					    	}
					    	else
					    	{
					    		itemPrice.setText("物品价格：¥"+object.getItemPrice());
					    	}
					    	
					    	itemName.setText("物品名称：" + object.getItemName());
					    	itemKind.setText("物品种类："+object.getItemTag()[0]);
					    	itemIntro.setText("简介："+object.getItemIntro());
							object.getItemIcon().loadImage(PersonRecordDetail.this, itemImage);
							
							recordIDText.setText("订单编号："+object.getObjectId());
							startTime.setText("创建时间："+object.getCreatedAt());
							if(object.getItemStatus() == 0)
							{
								endTime.setText("结束时间："+recordItem.getEndTime());
							}
							
							//确定对方ID
							if( recordItem.getBuyerID() == currentUser.getUsername())
							{
								userID = recordItem.getSellerID();
							}
							else
							{
								userID = recordItem.getBuyerID();
							}
							
							//查询对方信息
							BmobQuery<Member> query1 = new BmobQuery<Member>();
							query1.addWhereEqualTo("username", userID);
							query1.findObjects(PersonRecordDetail.this, new FindListener<Member>()
							{

								@Override
								public void onError(int arg0, String arg1) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(List<Member> object) {
									// TODO Auto-generated method stub
									object.get(0).getIcon().loadImage(PersonRecordDetail.this, userImage);
								}


							});
							
					    }

					    @Override
					    public void onFailure(int code, String arg0) {
					        // TODO Auto-generated method stub
					    	
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
    	
    	sellAgainButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new AlertDialog.Builder(PersonRecordDetail.this).setTitle("确定再次卖出吗").
				setPositiveButton("确定", new DialogInterface.OnClickListener() 
					     {
			                    public void onClick(DialogInterface dialog, int whichButton) 
			                    {
			                    	//更改物品状态
			                    	BmobQuery<Item> query = new BmobQuery<Item>();
			                		query.getObject(PersonRecordDetail.this, recordItem.getItemID(), new GetListener<Item>() {

			                		    @Override
			                		    public void onSuccess(Item object) {
			                		        // TODO Auto-generated method stub
			                		    	Item tempItem = object;
		                    		    	tempItem.setItemStatus(2);
		                    		    	
		                    		    	tempItem.update(PersonRecordDetail.this, recordItem.getItemID(), new UpdateListener() {

					                    	    @Override
					                    	    public void onSuccess() {
					                    	        // TODO Auto-generated method stub
					                    	    	Toast.makeText(PersonRecordDetail.this, "更新成功", Toast.LENGTH_SHORT).show();
					                    	    }

					                    	    @Override
					                    	    public void onFailure(int code, String msg) {
					                    	        // TODO Auto-generated method stub
					                    	    	Toast.makeText(PersonRecordDetail.this, "更新失败"+msg, Toast.LENGTH_SHORT).show();
					                    	    }
					                    	});
			                		    }

			                		    @Override
			                		    public void onFailure(int code, String arg0) {
			                		        // TODO Auto-generated method stub
			                		    	
			                		    }

			                		});
			                    }
			                  }).setNegativeButton("取消", null).show();
			}
			
		});
		
		chatButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				BmobQuery<Member> query = new BmobQuery<Member>();
				if(recordItem.getSellerID().equals(currentUser.getUsername()))
				{
					query.addWhereEqualTo("username", recordItem.getBuyerID());
				}
				else if(recordItem.getBuyerID().equals(currentUser.getUsername()))
				{
					query.addWhereEqualTo("username", recordItem.getSellerID());
				}
				
				query.findObjects(PersonRecordDetail.this, new FindListener<Member>(){
					@Override
					public void onError(int arg0, String arg1) 
					{
						// TODO Auto-generated method stub
						Toast.makeText(PersonRecordDetail.this, "failed", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(List<Member> arg0) 
					{
						// TODO Auto-generated method stub
						BmobChatUser chatUser = arg0.get(0);
						
						Intent intent = new Intent(PersonRecordDetail.this, ChatActivity.class);
						intent.putExtra("user", chatUser);
						PersonRecordDetail.this.startActivity(intent);
					}
				});
			}
			
		});
		
		cancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new AlertDialog.Builder(PersonRecordDetail.this).setTitle("若对方已确认该订单，取消交易会造成信用度降低，确认取消吗").
				setPositiveButton("确定", new DialogInterface.OnClickListener() 
					     {
			                    public void onClick(DialogInterface dialog, int whichButton) 
			                    {
			                    	if((recordItem.getSellerID().equals(currentUser.getUsername()) && recordItem.getRecordStatus() == 2)
			                    			|| (recordItem.getBuyerID().equals(currentUser.getUsername()) && recordItem.getRecordStatus() == 3) )
			        				{
			                    		//降低用户信用度
			                    		if(recordItem.getSellerID().equals(currentUser.getUsername()))
			                    		{
			                    			currentUser.setSellCredit(currentUser.getSellCredit() - 1);
			                    		}
			                    		else
			                    		{
			                    			currentUser.setBuyCredit(currentUser.getBuyCredit() - 1);
			                    		}

			        				}
			        				
			                    	//通知对方
		        					
		        					//设置结束时间
		        					Date currentTime = new Date();
			                    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			                    	String dateString = formatter.format(currentTime);
			                    	recordItem.setEndTime(dateString);
			                    	
			                    	//设置订单状态
		        					recordItem.setRecordStatus(0);
		        					cancelButton.setVisibility(View.INVISIBLE);
		        					cancelButton.setClickable(false);
			                    	
			                    	//更新订单
			                    	Record tempRecord = new Record();
			                    	tempRecord.setRecordStatus(recordItem.getRecordStatus());
			                    	tempRecord.setEndTime(recordItem.getEndTime());
			                    	
			                    	tempRecord.update(PersonRecordDetail.this, recordItem.getObjectId(), new UpdateListener() {

			                    	    @Override
			                    	    public void onSuccess() {
			                    	        // TODO Auto-generated method stub
			                    	    	//notifyDataSetChanged();
			                    	    	Toast.makeText(PersonRecordDetail.this, "更新成功", Toast.LENGTH_SHORT).show();      
			                    	    }

			                    	    @Override
			                    	    public void onFailure(int code, String msg) {
			                    	        // TODO Auto-generated method stub
			                    	    	Toast.makeText(PersonRecordDetail.this, "更新失败"+msg, Toast.LENGTH_SHORT).show();
			                    	    }
			                    	});
			                    	
			                    }
			                  }).setNegativeButton("取消", null).show();
			}
			
		});
		
		confirmButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new AlertDialog.Builder(PersonRecordDetail.this).setTitle("请确认交易已经完成后再确认订单").
				setPositiveButton("确定", new DialogInterface.OnClickListener() 
					     {
			                    public void onClick(DialogInterface dialog, int whichButton) 
			                    {
			                    	if(recordItem.getRecordStatus() == 2 || recordItem.getRecordStatus() == 3)
			                    	{
			                    		//设置结束时间
			        					Date currentTime = new Date();
				                    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				                    	String dateString = formatter.format(currentTime);
				                    	recordItem.setEndTime(dateString);
				                    	
				                    	//设置订单状态
			                    		recordItem.setRecordStatus(1);
			                    		
			                    		//商品下架
				                    	BmobQuery<Item> query = new BmobQuery<Item>();
				                		query.getObject(PersonRecordDetail.this, recordItem.getItemID(), new GetListener<Item>() {

				                		    @Override
				                		    public void onSuccess(Item object) {
				                		        // TODO Auto-generated method stub
				                		    	Item tempItem = object;
			                    		    	tempItem.setItemStatus(0);
			                    		    	
			                    		    	//商品所属者修改
			                    		    	tempItem.setOwner(recordItem.getBuyerID());
			                    		    	
			                    		    	tempItem.update(PersonRecordDetail.this, recordItem.getItemID(), new UpdateListener() {

						                    	    @Override
						                    	    public void onSuccess() {
						                    	        // TODO Auto-generated method stub
						                    	    	Toast.makeText(PersonRecordDetail.this, "更新成功", Toast.LENGTH_SHORT).show();
						                    	    }

						                    	    @Override
						                    	    public void onFailure(int code, String msg) {
						                    	        // TODO Auto-generated method stub
						                    	    	Toast.makeText(PersonRecordDetail.this, "更新失败"+msg, Toast.LENGTH_SHORT).show();
						                    	    }
						                    	});
				                		    }

				                		    @Override
				                		    public void onFailure(int code, String arg0) {
				                		        // TODO Auto-generated method stub
				                		    	
				                		    }

				                		});
			       	
			                    	}
			                    	else
			                    	{
			                    		if(recordItem.getSellerID().equals(currentUser.getUsername()))
				        				{
				                    		recordItem.setRecordStatus(3);
				        				}
				        				else if(recordItem.getBuyerID().equals(currentUser.getUsername()))
				        				{
				        					recordItem.setRecordStatus(2);
				        				}
			                    	}
			                    	
			                    	//更新订单
			                    	Record tempRecord = new Record();
			                    	tempRecord.setRecordStatus(recordItem.getRecordStatus());
			                    	tempRecord.setEndTime(recordItem.getEndTime());
			                    	
			                    	tempRecord.update(PersonRecordDetail.this, recordItem.getObjectId(), new UpdateListener() {

			                    	    @Override
			                    	    public void onSuccess() {
			                    	        // TODO Auto-generated method stub
			                    	    	Toast.makeText(PersonRecordDetail.this, "更新成功", Toast.LENGTH_SHORT).show();
			                    	    	//notifyDataSetChanged();
			                    	    }

			                    	    @Override
			                    	    public void onFailure(int code, String msg) {
			                    	        // TODO Auto-generated method stub
			                    	    	Toast.makeText(PersonRecordDetail.this, "更新失败"+msg, Toast.LENGTH_SHORT).show();
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
