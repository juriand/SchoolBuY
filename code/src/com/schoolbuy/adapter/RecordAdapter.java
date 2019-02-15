 package com.schoolbuy.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import cn.bmob.im.BmobChat;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import com.example.schoolbuy.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;
import com.schoolbuy.demo.CustomApplication;
import com.schoolbuy.ui.ChatActivity;
import com.schoolbuy.ui.PersonRecordDetail;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RecordAdapter extends ArrayAdapter<Record>{
	
	private int resourceId;
	
	private Member currentUser;
	
	static class ViewClass
	{  
		ImageView itemImage, recordIcon;
		TextView userName, itemStatus, itemprice, itemName, waitText;
		Button sellAgainButton, cancelButton, chatButton;
		ImageButton detailButton, confirmButton;
	} 
	
	public RecordAdapter(Context context, int textViewResourceId, List<Record> objects){
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		BmobChat.getInstance(getContext()).init("a00f5a1a5b5eb533699823f2f704b706");
		final Record recordItem = getItem(position);
		final ViewClass viewclass;	
		currentUser = BmobUser.getCurrentUser(getContext(),Member.class);
		
		if(convertView == null)
		{
			LayoutInflater factory = LayoutInflater.from(getContext());  
			convertView = factory.inflate(resourceId,null);
            
			viewclass = new ViewClass();
			
			//加载组件
			viewclass.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
			viewclass.recordIcon = (ImageView) convertView.findViewById(R.id.recordIcon);
			
			viewclass.userName = (TextView) convertView.findViewById(R.id.timeNumber);
			viewclass.itemprice = (TextView) convertView.findViewById(R.id.itemPrice);
			viewclass.itemName = (TextView) convertView.findViewById(R.id.itemName);
			viewclass.itemStatus = (TextView) convertView.findViewById(R.id.status);
			viewclass.waitText = (TextView) convertView.findViewById(R.id.waitText);
			
			viewclass.sellAgainButton = (Button) convertView.findViewById(R.id.sellAgain);
			viewclass.cancelButton = (Button) convertView.findViewById(R.id.cancel);
			viewclass.chatButton = (Button) convertView.findViewById(R.id.chat);
			
			viewclass.detailButton = (ImageButton) convertView.findViewById(R.id.detail);
			viewclass.confirmButton = (ImageButton) convertView.findViewById(R.id.confirm);
			
			convertView.setTag(viewclass);
		}
		else
		{
			viewclass =(ViewClass) convertView.getTag();
		}

		if(recordItem.getRecordStatus() == 0 || recordItem.getRecordStatus() == 1)
		{
			viewclass.itemStatus.setText("交易结束");
			
			//交易结束后不显示取消、确认、等待
			viewclass.cancelButton.setVisibility(View.INVISIBLE);
			viewclass.cancelButton.setClickable(false);
			
			viewclass.confirmButton.setVisibility(View.INVISIBLE);
			viewclass.confirmButton.setClickable(false);
			viewclass.waitText.setVisibility(View.INVISIBLE);

			if(recordItem.getBuyerID().equals(currentUser.getUsername()) && recordItem.getRecordStatus() == 1)
			{
				viewclass.sellAgainButton.setVisibility(View.VISIBLE);
				viewclass.sellAgainButton.setClickable(true);
			}

		}
		else 
		{
			viewclass.itemStatus.setText("正在交易");
			
			//不显示再次卖出
			viewclass.sellAgainButton.setVisibility(View.INVISIBLE);
			viewclass.sellAgainButton.setClickable(false);
			
			//显示取消
			viewclass.cancelButton.setVisibility(View.VISIBLE);
			viewclass.cancelButton.setClickable(true);
		}	
		
		if(recordItem.getSellerID().equals(currentUser.getUsername()))
		{
			viewclass.recordIcon.setImageResource(R.drawable.person_record_sell);
			viewclass.userName.setText(recordItem.getBuyerID());
			if(recordItem.getRecordStatus() ==  3)
			{
				viewclass.waitText.setVisibility(View.VISIBLE);
				
				viewclass.confirmButton.setVisibility(View.INVISIBLE);
				viewclass.confirmButton.setClickable(false);
			}
			else if((recordItem.getRecordStatus() ==  2) || (recordItem.getRecordStatus() ==  4))
			{
				viewclass.confirmButton.setVisibility(View.VISIBLE);
				viewclass.confirmButton.setClickable(true);
			}
		}
		else if(recordItem.getBuyerID().equals(currentUser.getUsername()))
		{
			viewclass.recordIcon.setImageResource(R.drawable.person_record_buy);
			viewclass.userName.setText(recordItem.getSellerID());
			if(recordItem.getRecordStatus() ==  2)
			{
				viewclass.waitText.setVisibility(View.VISIBLE);
				
				viewclass.confirmButton.setVisibility(View.INVISIBLE);
				viewclass.confirmButton.setClickable(false);
			}
			else if((recordItem.getRecordStatus() ==  3) || (recordItem.getRecordStatus() ==  4))
			{
				viewclass.confirmButton.setVisibility(View.VISIBLE);
				viewclass.confirmButton.setClickable(true);
			}
		}
		
		BmobQuery<Item> query = new BmobQuery<Item>();
		query.getObject(getContext(), recordItem.getItemID(), new GetListener<Item>() {

		    @Override
		    public void onSuccess(Item object) 
		    {
		        // TODO Auto-generated method stub
		    	viewclass.itemprice.setText("¥"+object.getItemPrice());
		    	viewclass.itemName.setText(object.getItemName());
		    	
				//加载物品图片
				String imageUrl = object.getItemIcon().getFileUrl(getContext());
				ImageLoader.getInstance().displayImage(imageUrl,viewclass.itemImage,CustomApplication.getInstance().getOptions(
								R.drawable.item_image_load),new SimpleImageLoadingListener()
				{
							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								super.onLoadingComplete(imageUri, view, loadedImage);
							}

				});
		    }

		    @Override
		    public void onFailure(int code, String arg0) {
		        // TODO Auto-generated method stub
		    	
		    }

		});

		
		
    	//CLICK
		viewclass.sellAgainButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new AlertDialog.Builder(getContext()).setTitle("确定再次卖出吗").
				setPositiveButton("确定", new DialogInterface.OnClickListener() 
					     {
			                    public void onClick(DialogInterface dialog, int whichButton) 
			                    {
			                    	//更改物品状态
			                    	BmobQuery<Item> query = new BmobQuery<Item>();
			                		query.getObject(getContext(), recordItem.getItemID(), new GetListener<Item>() {

			                		    @Override
			                		    public void onSuccess(Item object) {
			                		        // TODO Auto-generated method stub
			                		    	Item tempItem = object;
		                    		    	tempItem.setItemStatus(2);
		                    		    	
		                    		    	tempItem.update(getContext(), recordItem.getItemID(), new UpdateListener() {

					                    	    @Override
					                    	    public void onSuccess() {
					                    	        // TODO Auto-generated method stub
					                    	    	Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
					                    	    }

					                    	    @Override
					                    	    public void onFailure(int code, String msg) {
					                    	        // TODO Auto-generated method stub
					                    	    	Toast.makeText(getContext(), "更新失败"+msg, Toast.LENGTH_SHORT).show();
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
		
		viewclass.chatButton.setOnClickListener(new OnClickListener(){

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
				
				query.findObjects(getContext(), new FindListener<Member>(){
					@Override
					public void onError(int arg0, String arg1) 
					{
						// TODO Auto-generated method stub
						Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(List<Member> arg0) 
					{
						// TODO Auto-generated method stub
						BmobChatUser chatUser = arg0.get(0);
						
						Intent intent = new Intent(getContext(), ChatActivity.class);
						intent.putExtra("user", chatUser);
						getContext().startActivity(intent);
					}
				});
			}
			
		});
		
		viewclass.detailButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getContext(), PersonRecordDetail.class);
				Bundle bundle = new Bundle();
				bundle.putString("ID", recordItem.getObjectId());
				myIntent.putExtras(bundle);
				getContext().startActivity(myIntent);
			}
			
		});
		
		viewclass.cancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new AlertDialog.Builder(getContext()).setTitle("若对方已确认该订单，取消交易会造成信用度降低，确认取消吗").
				setPositiveButton("确定", new DialogInterface.OnClickListener() 
					     {
			                    @SuppressLint("SimpleDateFormat") public void onClick(DialogInterface dialog, int whichButton) 
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
		        					viewclass.cancelButton.setVisibility(View.INVISIBLE);
		        					viewclass.cancelButton.setClickable(false);
			                    	
			                    	//更新订单
			                    	Record tempRecord = new Record();
			                    	tempRecord.setRecordStatus(recordItem.getRecordStatus());
			                    	tempRecord.setEndTime(recordItem.getEndTime());
			                    	
			                    	tempRecord.update(getContext(), recordItem.getObjectId(), new UpdateListener() {

			                    	    @Override
			                    	    public void onSuccess() {
			                    	        // TODO Auto-generated method stub
			                    	    	notifyDataSetChanged();
			                    	    	Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();      
			                    	    }

			                    	    @Override
			                    	    public void onFailure(int code, String msg) {
			                    	        // TODO Auto-generated method stub
			                    	    	Toast.makeText(getContext(), "更新失败"+msg, Toast.LENGTH_SHORT).show();
			                    	    }
			                    	});
			                    	
			                    }
			                  }).setNegativeButton("取消", null).show();
			}
			
		});
		
		viewclass.confirmButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				new AlertDialog.Builder(getContext()).setTitle("请确认交易已经完成后再确认订单").
				setPositiveButton("确定", new DialogInterface.OnClickListener() 
					     {
			                    @SuppressLint("SimpleDateFormat") public void onClick(DialogInterface dialog, int whichButton) 
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
				                		query.getObject(getContext(), recordItem.getItemID(), new GetListener<Item>() {

				                		    @Override
				                		    public void onSuccess(Item object) {
				                		        // TODO Auto-generated method stub
				                		    	Item tempItem = object;
			                    		    	tempItem.setItemStatus(0);
			                    		    	
			                    		    	//商品所属者修改
			                    		    	tempItem.setOwner(recordItem.getBuyerID());
			                    		    	
			                    		    	tempItem.update(getContext(), recordItem.getItemID(), new UpdateListener() {

						                    	    @Override
						                    	    public void onSuccess() {
						                    	        // TODO Auto-generated method stub
						                    	    	Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
						                    	    }

						                    	    @Override
						                    	    public void onFailure(int code, String msg) {
						                    	        // TODO Auto-generated method stub
						                    	    	Toast.makeText(getContext(), "更新失败"+msg, Toast.LENGTH_SHORT).show();
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
			                    	
			                    	tempRecord.update(getContext(), recordItem.getObjectId(), new UpdateListener() {

			                    	    @Override
			                    	    public void onSuccess() {
			                    	        // TODO Auto-generated method stub
			                    	    	Toast.makeText(getContext(), "更新成功", Toast.LENGTH_SHORT).show();
			                    	    	notifyDataSetChanged();
			                    	    }

			                    	    @Override
			                    	    public void onFailure(int code, String msg) {
			                    	        // TODO Auto-generated method stub
			                    	    	Toast.makeText(getContext(), "更新失败"+msg, Toast.LENGTH_SHORT).show();
			                    	    }
			                    	});
			                    }
			                  }).setNegativeButton("取消", null).show();
			}
			
		});
		
		return convertView;
	}

}
