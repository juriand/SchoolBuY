package com.schoolbuy.adapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Want;
import com.schoolbuy.demo.CustomApplication;
import com.schoolbuy.ui.PersonTimeDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WantAdapter extends ArrayAdapter<Want>{
	
	private int resourceId;
	
	static class ViewClass
	{  
		ImageView iconImage;
		TextView userName,itemName, itemIntro;
		Button haveButton;
	} 
	
	public WantAdapter(Context context, int textViewResourceId, List<Want> objects){
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final Want item = getItem(position);
		final ViewClass viewclass;
		
		if(convertView == null)
		{
			LayoutInflater factory = LayoutInflater.from(getContext());  
			convertView = factory.inflate(resourceId,null);
            
			viewclass = new ViewClass();
			
			viewclass.iconImage = (ImageView) convertView.findViewById(R.id.icon);
			
			viewclass.userName = (TextView) convertView.findViewById(R.id.username);
			viewclass.itemName = (TextView) convertView.findViewById(R.id.itemName);
			viewclass.itemIntro = (TextView) convertView.findViewById(R.id.introduction);
			
			viewclass.haveButton = (Button) convertView.findViewById(R.id.contact);
			
			convertView.setTag(viewclass);
		}
		else
		{			
			viewclass =(ViewClass) convertView.getTag();
		}
		
		viewclass.userName.setText(item.getOwner());	
		viewclass.itemName.setText(item.getName());
		viewclass.itemIntro.setText("简介："+item.getIntro());

		
		//加载头像
		BmobQuery<Member> query = new BmobQuery<Member>();
    	query.addWhereEqualTo("username",item.getOwner());
    	//执行查询方法
    	query.findObjects(getContext(), new FindListener<Member>() {
    	        @Override
    	        public void onSuccess(List<Member> object) {
    	            // TODO Auto-generated method stub
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
    	
    	//CLICK
    	viewclass.haveButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				//chat
				
			}
			
		});
		
		return convertView;
	}
}
