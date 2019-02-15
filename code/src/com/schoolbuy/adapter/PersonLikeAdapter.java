package com.schoolbuy.adapter;

import java.util.HashMap;
import java.util.List;

import android.R.array;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolbuy.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.schoolbuy.bean.Item;
import com.schoolbuy.demo.CustomApplication;
import com.schoolbuy.ui.PersonItemDetail;
import com.schoolbuy.ui.PersonTimeDetail;

public class PersonLikeAdapter extends ArrayAdapter<Item>{
	
	private int resourceId;
	
	private String[] itemTag = new String[3];
	
	private boolean deleteMode = false;
	
	private HashMap<Integer, View> mView ;
	public  HashMap<Integer, Integer> visiblecheck ;//用来记录是否显示checkBox
	public  HashMap<Integer, Boolean> ischeck;
	
	static class ViewClass
	{  
		ImageView photoImage;
		TextView itemprice, itemName;
		Button detailsButton;
		CheckBox checkDeleteBox;
	} 
	
	public PersonLikeAdapter(Context context, int textViewResourceId, List<Item> objects){
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
			
			viewclass.itemprice = (TextView) convertView.findViewById(R.id.itemPrice);
			viewclass.itemName = (TextView) convertView.findViewById(R.id.itemName);
			
			viewclass.detailsButton = (Button) convertView.findViewById(R.id.sellAgain);
			
			viewclass.checkDeleteBox = (CheckBox) convertView.findViewById(R.id.checkDelete);
			
			convertView.setTag(viewclass);
		}
		else
		{
			viewclass =(ViewClass) convertView.getTag();
		}

		viewclass.itemprice.setText("¥"+item.getItemPrice());
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
		
		//CLICK
		viewclass.detailsButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getContext(),PersonItemDetail.class);
				Bundle mBundle = new Bundle();
				mBundle.putString("itemID", item.getObjectId().toString());
				myIntent.putExtras(mBundle);
    	    	getContext().startActivity(myIntent);
			}
			
		});
		
		convertView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(deleteMode == true)
				{
					if(viewclass.checkDeleteBox.isChecked() == true)
					{
						viewclass.checkDeleteBox.setChecked(false);
					}
					else
					{
						viewclass.checkDeleteBox.setChecked(true);
					}
					
				}
			}
			
		});

		return convertView;
	}

	public boolean isDeleteMode() {
		return deleteMode;
	}

	public void setDeleteMode(boolean deleteMode) {
		this.deleteMode = deleteMode;
	}
}
