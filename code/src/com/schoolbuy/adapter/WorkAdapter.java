package com.schoolbuy.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cn.bmob.im.BmobChat;

import com.example.schoolbuy.R;
import com.schoolbuy.bean.WorkInfor;

public class WorkAdapter extends ArrayAdapter<WorkInfor>{
	
	private int resourceId;
	
	static class ViewClass
	{  
		TextView title, body;
	} 
	
	public WorkAdapter(Context context, int textViewResourceId, List<WorkInfor> objects){
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		BmobChat.getInstance(getContext()).init("a00f5a1a5b5eb533699823f2f704b706");	
		final WorkInfor workItem = getItem(position);
		final ViewClass viewclass;
		
		if(convertView == null)
		{
			LayoutInflater factory = LayoutInflater.from(getContext());  
			convertView = factory.inflate(resourceId,null);
            
			viewclass = new ViewClass();
			
			viewclass.title = (TextView) convertView.findViewById(R.id.timeNumber);
			viewclass.body = (TextView) convertView.findViewById(R.id.workBody);
			
			convertView.setTag(viewclass);
		}
		else
		{
			viewclass =(ViewClass) convertView.getTag();
		}
		
		viewclass.title.setText(workItem.getTitle());
		viewclass.body.setText(workItem.getBody());
		
		return convertView;
	}

}
