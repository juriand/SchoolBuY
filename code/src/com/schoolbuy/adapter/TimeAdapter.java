package com.schoolbuy.adapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Record;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TimeAdapter extends ArrayAdapter<Item>{
	
	private int resourceId;
	
	static class ViewClass
	{  
		ImageView photoImage;
		TextView itemName,timeNumber;
		ImageButton detailButton;
	} 
	
	public TimeAdapter(Context context, int textViewResourceId, List<Item> objects){
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final Item item = getItem(position);
		final ViewClass viewclass;
		
		if(convertView == null)
		{
			LayoutInflater factory = LayoutInflater.from(getContext());  
			convertView = factory.inflate(resourceId,null);
            
			viewclass = new ViewClass();
			
			//加载组件
			viewclass.photoImage = (ImageView) convertView.findViewById(R.id.itemIcon);
			
			viewclass.itemName = (TextView) convertView.findViewById(R.id.itemName);
			viewclass.timeNumber = (TextView) convertView.findViewById(R.id.timeNumber);
			
			viewclass.detailButton = (ImageButton) convertView.findViewById(R.id.detail);
			
			convertView.setTag(viewclass);
		}
		else
		{
			viewclass =(ViewClass) convertView.getTag();
		}

		viewclass.itemName.setText(item.getItemName());	
		
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
		
		//查找轨迹数
		BmobQuery<Record> query = new BmobQuery<Record>();
    	query.addWhereEqualTo("itemID", item.getObjectId());
    	query.addWhereEqualTo("recordStatus", 1);
    	query.order("-createdAt");
    	query.findObjects(getContext(), new FindListener<Record>() {
    	    @Override
    	    public void onSuccess(List<Record> object) {
    	        // TODO Auto-generated method stub
    	    	if(object.size() != 0)
    	    	{
    	    		viewclass.timeNumber.setText("共"+object.size()+"个轨迹");
    	    	}
    	    }
    	    @Override
    	    public void onError(int code, String msg) {
    	        // TODO Auto-generated method stub
    	    }
    	});
		
		
		//CLICK
		viewclass.detailButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getContext(),PersonTimeDetail.class);
				Bundle mBundle = new Bundle();
				mBundle.putString("itemID", item.getObjectId());
				myIntent.putExtras(mBundle);
    	    	getContext().startActivity(myIntent);
			}
			
		});

		return convertView;
	}

}
