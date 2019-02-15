package com.schoolbuy.adapter;

import java.util.List;
import java.util.StringTokenizer;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.UpdateListener;

import com.example.schoolbuy.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.schoolbuy.bean.Item;
import com.schoolbuy.demo.CustomApplication;
import com.schoolbuy.ui.PersonItemDetail;
import com.schoolbuy.ui.PersonTimeDetail;

public class PersonItemAdapter extends ArrayAdapter<Item>{
	
	private int resourceId;
	
	private String[] itemTag = new String[3];
	
	static class ViewClass
	{  
		ImageView photoImage;
		TextView itemprice, itemName;
		Button detailsButton;
	} 
	
	public PersonItemAdapter(Context context, int textViewResourceId, List<Item> objects){
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

		return convertView;
	}

}
