package com.schoolbuy.adapter;

import java.util.List;

import cn.bmob.im.BmobChat;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import com.example.schoolbuy.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;
import com.schoolbuy.demo.CustomApplication;
import com.schoolbuy.ui.ChatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TimeLineAdapter extends ArrayAdapter<Record>{
	
	private int resourceId;
	
	private Member currentUser;
	
	static class ViewClass
	{  
		ImageView iconImage;
		TextView userName, recordDate, itemprice;
		Button chatButton;
	} 
	
	public TimeLineAdapter(Context context, int textViewResourceId, List<Record> objects){
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		BmobChat.getInstance(getContext()).init("a00f5a1a5b5eb533699823f2f704b706");	
		currentUser = BmobUser.getCurrentUser(getContext(),Member.class);
		final Record recordItem = getItem(position);
		final ViewClass viewclass;
		
		if(convertView == null)
		{
			LayoutInflater factory = LayoutInflater.from(getContext());  
			convertView = factory.inflate(resourceId,null);
            
			viewclass = new ViewClass();
			
			viewclass.userName = (TextView) convertView.findViewById(R.id.name);
			viewclass.itemprice = (TextView) convertView.findViewById(R.id.price);
			viewclass.recordDate = (TextView) convertView.findViewById(R.id.date);
			
			viewclass.chatButton = (Button) convertView.findViewById(R.id.chat);	
			
			viewclass.iconImage = (ImageView) convertView.findViewById(R.id.itemIcon);
			
			convertView.setTag(viewclass);
		}
		else
		{
			viewclass =(ViewClass) convertView.getTag();
		}
		
		viewclass.userName.setText("由"+recordItem.getBuyerID()+"购入");
		viewclass.recordDate.setText(recordItem.getEndTime());
		
		//购入者为自己则不显示联系
		if(recordItem.getBuyerID().equals(currentUser.getUsername()))
		{
			viewclass.chatButton.setVisibility(View.INVISIBLE);
			viewclass.chatButton.setClickable(false);
		}
		
		BmobQuery<Item> query = new BmobQuery<Item>();
		query.getObject(getContext(), recordItem.getItemID(), new GetListener<Item>() {

		    @Override
		    public void onSuccess(Item object) {
		        // TODO Auto-generated method stub
		    	viewclass.itemprice.setText("¥"+object.getItemPrice());
		    }

		    @Override
		    public void onFailure(int code, String arg0) {
		        // TODO Auto-generated method stub
		    	
		    }

		});
		
		BmobQuery<Member> query2 = new BmobQuery<Member>();
    	query2.addWhereEqualTo("username",recordItem.getBuyerID());
    	//执行查询方法
    	query2.findObjects(getContext(), new FindListener<Member>() {
    	        @Override
    	        public void onSuccess(List<Member> object) {
    	            // TODO Auto-generated method stub
    	    		//加载物品图片
    	    		String imageUrl = object.get(0).getIcon().getFileUrl(getContext());
    	    		ImageLoader.getInstance().displayImage(imageUrl,viewclass.iconImage,CustomApplication.getInstance().getOptions(
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
    	        public void onError(int code, String msg) {
    	            // TODO Auto-generated method stub
    	        	//Toast.makeText(getApplicationContext(), "查询失败"+msg, Toast.LENGTH_SHORT).show();
    	        }
    	});
		
    	//CLICK	
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
		
		return convertView;
	}

}
