package com.schoolbuy.cropimage;

import com.example.schoolbuy.R;
import com.schoolbuy.utils.OSUtils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseDialog implements OnClickListener {
	// SD��������
	public static final String SDCARD_NOT_EXISTS = "SD�������ڣ��޷�����ͷ��";
	private Dialog mDialog = null;
	private Activity mActivity = null;
	private CropHelper mCropHelper = null;

	public ChooseDialog(Activity act, CropHelper helper) {
		mActivity = act;
		mCropHelper = helper;
	}

	public void popSelectDialog() {
		if (OSUtils.ExistSDCard()) {
			setDialog();
			mDialog.show();
		} else {
			showToast(SDCARD_NOT_EXISTS);
			return;
		}
	}

	private void setDialog() {
		// �˴�ֱ��newһ��Dialog�����������ʵ������ʱ��������
		if (mDialog == null) {
			mDialog = new Dialog(mActivity, R.style.MyDialog);
			mDialog.setContentView(R.layout.add_set_choice);
			mDialog.setCanceledOnTouchOutside(true);
			TextView takePic = (TextView) mDialog.findViewById(R.id.take_pictures);
			TextView cancelTxt = (TextView) mDialog.findViewById(R.id.select_cancel);
			
			TextView selectAlbum = (TextView) mDialog
					.findViewById(R.id.select_photo);
			takePic.setOnClickListener(this);
			selectAlbum.setOnClickListener(this);
			cancelTxt.setOnClickListener(this);
		}
	}

	public void showToast(String msg) {
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if (viewId == R.id.take_pictures) {
			clickInCamera();
		} else if (viewId == R.id.select_photo) {
			clickInAlbum();
		}else if (viewId == R.id.select_cancel) {
			mDialog.dismiss();
		}
	}

	// ����
	public void clickInCamera() {
		if (mDialog != null)
			mDialog.dismiss();
		mCropHelper.startCamera();
	}

	// �ӱ������
	public void clickInAlbum() {
		if (mDialog != null)
			mDialog.dismiss();
		mCropHelper.startAlbum();
	}
}
