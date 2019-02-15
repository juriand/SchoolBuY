package com.schoolbuy.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.schoolbuy.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;
import com.schoolbuy.demo.CustomApplication;
import com.schoolbuy.ui.LogActivity;
import com.schoolbuy.ui.PersonRecord;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;


public class ItemAdapter extends ArrayAdapter<Item>{
	
	private int resourceId;
	
	private String[] itemTag = new String[3];
	private String targetId="";
	
	private Member currentUser;
	
	private boolean isAdd = true,isLike = false;
	
	TextView[] itemTagText = new TextView[3];
	
	private BmobChatManager manager;
	private BmobChatUser targetUser;
	
	static class ViewClass
	{  
		ImageView photoImage, iconImage;
		TextView userName, itemprice, itemName, itemIntro, likeText, timeText;
		Button buyButton;
	} 
	
	public ItemAdapter(Context context, int textViewResourceId, List<Item> objects){
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final Item item = getItem(position);
		final ViewClass viewclass;
		
		manager = BmobChatManager.getInstance(getContext());
		currentUser = BmobUser.getCurrentUser(getContext(),Member.class);
		
		if(convertView == null)
		{
			LayoutInflater factory = LayoutInflater.from(getContext());  
			convertView = factory.inflate(resourceId,null);
            
			viewclass = new ViewClass();
			
			viewclass.photoImage = (ImageView) convertView.findViewById(R.id.photo);
			viewclass.iconImage = (ImageView) convertView.findViewById(R.id.icon);
			
			viewclass.userName = (TextView) convertView.findViewById(R.id.username);
			viewclass.itemprice = (TextView) convertView.findViewById(R.id.price);
			viewclass.itemName = (TextView) convertView.findViewById(R.id.itemName);
			viewclass.itemIntro = (TextView) convertView.findViewById(R.id.introduction);
			viewclass.likeText = (TextView) convertView.findViewById(R.id.like);
			viewclass.timeText = (TextView) convertView.findViewById(R.id.timeText);
			
			viewclass.buyButton = (Button) convertView.findViewById(R.id.contact);

			convertView.setTag(viewclass);
		}
		else
		{			
			viewclass =(ViewClass) convertView.getTag();
		}
		
		viewclass.userName.setText(item.getOwner());
		if(item.getItemKind() == 2)
		{
			viewclass.itemprice.setText("交换物品："+item.getTarget());
		}
		else
		{
			viewclass.itemprice.setText("¥"+item.getItemPrice());
		}
		
		viewclass.itemName.setText(item.getItemName());
		viewclass.itemIntro.setText("简介："+item.getItemIntro());
		viewclass.timeText.setText(item.getCreatedAt());
		
		
		//加载物品图片
		String imageUrl = item.getItemIcon().getFileUrl(getContext());
		ImageLoader.getInstance().displayImage(imageUrl,viewclass.photoImage,CustomApplication.getInstance().getOptions(
						R.drawable.item_image_load),new SimpleImageLoadingListener()
		{
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
					}

		});

		//加载标签	
		itemTagText[0] = (TextView) convertView.findViewById(R.id.title1);
		itemTagText[1] = (TextView) convertView.findViewById(R.id.tag2);
		itemTagText[2] = (TextView) convertView.findViewById(R.id.tag3);
		
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
		BmobQuery<Member> query = new BmobQuery<Member>();
    	query.addWhereEqualTo("username",item.getOwner());
    	//执行查询方法
    	query.findObjects(getContext(), new FindListener<Member>() {
    	        @Override
    	        public void onSuccess(List<Member> object) {
    	            // TODO Auto-generated method stub
    	        	targetUser = object.get(0);
    	        	targetId = object.get(0).getObjectId();
    	        	String userIconUrl = object.get(0).getIcon().getFileUrl(getContext());
    	        	
    	        	ImageLoader.getInstance().displayImage(userIconUrl,viewclass.iconImage,CustomApplication.getInstance().getOptions(
    						R.drawable.icon_noset),new SimpleImageLoadingListener()
    	        	{
    					@Override
    					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
    						// TODO Auto-generated method stub
    						super.onLoadingComplete(imageUri, view, loadedImage);
    					}

    	        	});
    	        }
    	        @Override
    	        public void onError(int code, String msg) {
    	            // TODO Auto-generated method stub
    	        	//Toast.makeText(getApplicationContext(), "查询失败"+msg, Toast.LENGTH_SHORT).show();
    	        }
    	});
    	
    	//搜索收藏列表是否已存在该物品
    	if(currentUser != null)
    	{
    		for(int i=0;i<currentUser.getLike().size();i++)
    		{
    			if(currentUser.getLike().get(i).equals(item.getObjectId()))
    			{
    				isLike = true;
    				break;
    			}
    		}
    	}
		
		if(isLike == true)
		{
			currentUser.getLike().add(item.getObjectId());
			viewclass.likeText.setText("已收藏");
			isLike = false;
		}
		else
		{
			viewclass.likeText.setText("收藏");
		}

    	viewclass.buyButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				//检查是否登录
				if(currentUser == null)
				{
					Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
					Intent myIntent = new Intent(getContext(), LogActivity.class);
					getContext().startActivity(myIntent);
				}
				else
				{
					new AlertDialog.Builder(getContext()).setTitle("确定交易吗").
					setPositiveButton("确定", new DialogInterface.OnClickListener() 
				    {
						public void onClick(DialogInterface dialog, int whichButton) 
	                    {
	                    	//检查用户	                    	
	                    	if( currentUser.getUsername().equals(item.getOwner()))
	                    	{
	                    		Toast.makeText(getContext(), "不能交易自己的商品", Toast.LENGTH_SHORT).show();
	                    		
	                    		isAdd = false;    		
	                    	}
	                    	
	                    	//检查交易状态
	                    	BmobQuery<Record> query = new BmobQuery<Record>();
	                    	query.addWhereEqualTo("sellerID",item.getOwner());
	                    	//执行查询方法
	                    	query.findObjects(getContext(), new FindListener<Record>() {
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
		                    	        		Toast.makeText(getContext(), "交易记录已存在", Toast.LENGTH_SHORT).show();
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
					                    	
					                    	tempRecord.save(getContext(), new SaveListener(){
					                    	    @Override
					                    	    public void onSuccess() {
					                    	        // TODO Auto-generated method stub		                    	    	
					                    	    	Toast.makeText(getContext(), "添加交易记录成功", Toast.LENGTH_SHORT).show();
					                    	    	
					                    	    	//通知对方
					                    	    	BmobMsg message = BmobMsg.createTextSendMsg(getContext(), targetId, "我要交易你发布的"+item.getItemName());
					                    			message.setExtra("Bmob");
					                    			manager.sendTextMessage(targetUser, message);
					                    	    	
					                    			//转向记录页
					                    	    	Intent myIntent = new Intent(getContext(),PersonRecord.class);
					                    	    	getContext().startActivity(myIntent);
					                    	    }

					                    	    @Override
					                    	    public void onFailure(int code, String msg) {
					                    	        // TODO Auto-generated method stub
					                    	    	Toast.makeText(getContext(), "添加交易记录失败"+msg, Toast.LENGTH_SHORT).show();
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
				
			}
			
		});
		
		viewclass.likeText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				//搜索收藏列表是否已存在该物品
				for(int i=0;i<currentUser.getLike().size();i++)
				{
					if(currentUser.getLike().get(i).equals(item.getObjectId()))
					{
						Toast.makeText(getContext(), "已经在收藏中了", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				currentUser.getLike().add(item.getObjectId());
				viewclass.likeText.setText("已收藏");
				
				//保存数据
				Member tempUser = currentUser;
				tempUser.update(getContext(), currentUser.getObjectId(), new UpdateListener(){
            	    @Override
            	    public void onSuccess() {
            	        // TODO Auto-generated method stub		                    	    	
            	    	Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
            	    }

            	    @Override
            	    public void onFailure(int code, String msg) {
            	        // TODO Auto-generated method stub
            	    	Toast.makeText(getContext(), "收藏失败"+msg, Toast.LENGTH_SHORT).show();
            	    }
            	});
			}
			
		});
		return convertView;
	}
}
