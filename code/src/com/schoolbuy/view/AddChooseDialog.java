package com.schoolbuy.view;


import com.example.schoolbuy.R;
import com.schoolbuy.ui.NewItemActivity;
import com.schoolbuy.ui.NewMessActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AddChooseDialog implements OnClickListener 
{
	private Dialog mDialog = null;
	private Activity mActivity = null;

	public AddChooseDialog(Activity act) 
	{
		mActivity = act;
	}

	public void popSelectDialog() 
	{
		setDialog();
		mDialog.show();
	}

	private void setDialog() {
		if (mDialog == null) 
		{
			mDialog = new Dialog(mActivity, R.style.MyDialog);
			mDialog.setContentView(R.layout.add_set_choice);
			mDialog.setCanceledOnTouchOutside(true);
			TextView addMess = (TextView) mDialog.findViewById(R.id.add_message);
			TextView addItem = (TextView) mDialog.findViewById(R.id.add_item);
			
			addMess.setOnClickListener(this);
			addItem.setOnClickListener(this);
		}
	}

	public void showToast(String msg) 
	{
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) 
	{
		int viewId = v.getId();
		if (viewId == R.id.add_message) {
			clickMessage();
		} 
		else if (viewId == R.id.add_item) {
			clickItem();
		}
	}

	//发布消息
	public void clickMessage() {
		if (mDialog != null)
		{
			Intent intent = new Intent(mActivity,NewMessActivity.class);
			mActivity.startActivity(intent);
			
			mDialog.dismiss();
		}
			
		
	}
	
	//发布物品
	public void clickItem() {
		if (mDialog != null)
		{
			Intent intent = new Intent(mActivity,NewItemActivity.class);
			mActivity.startActivity(intent);
			
			mDialog.dismiss();
		}
			
		
	}
}
