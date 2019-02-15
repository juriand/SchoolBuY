package com.schoolbuy.cropimage;

import java.io.File;

import com.schoolbuy.utils.BitmapUtils;
import com.schoolbuy.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class CropHelper {
	// SD��������
	public static final String SDCARD_NOT_EXISTS = "SD�������ڣ��޷�����";

	// ����ͷ��
	public static final int HEAD_FROM_ALBUM = 2106;
	public static final int HEAD_FROM_CAMERA = 2107;
	public static final int HEAD_SAVE_PHOTO = 2108;

	// ͷ��������ʾ
	public static final String HEAD_SET_CANCEL = "ȡ������";
	public static final String HEAD_UPLOAD_SUCCESS = "ͼƬ�ϴ��ɹ�";
	public static final String HEAD_IMAGE_INVALID = "��ЧͼƬ";
	public static final String UPLOAD_HEAD = "�ϴ�ͼƬ";

	private Activity mActivity = null;

	// ��Ƭ����·��
	private String mTempPhotoPath;

	public CropHelper(Activity act, String photoPath) {
		mActivity = act;
		mTempPhotoPath = photoPath;
	}

	public Bitmap getBitmap(Intent data) {
		if (data == null) {
			return null;
		}
		return data.getParcelableExtra("data");
	}

	/**
	 * ��ʼ�ü�
	 * 
	 */
	public void startPhotoCrop(Uri uri, String duplicatePath, int reqCode, boolean mBWPhoto) {

		Intent intent = new Intent();
		intent.setClass(mActivity,CropImage.class);
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("scale", false);
		intent.putExtra("scaleUpIfNeeded", false);
		intent.putExtra("BWPhoto", mBWPhoto);
		mActivity.startActivityForResult(intent, reqCode);
		
	}

	public void startCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// �������ָ������������պ����Ƭ�洢��·��
		File temp = new File(mTempPhotoPath);
		if (temp.exists()) {
			temp.delete();
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
		mActivity.startActivityForResult(intent, HEAD_FROM_CAMERA);
	}

	public void startAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		mActivity.startActivityForResult(intent, HEAD_FROM_ALBUM);
	}

	public void showToast(String msg) {
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
	}

	public void getDataFromCamera(Intent data) {
		File temp = new File(mTempPhotoPath);
		if (!temp.exists()) {
			showToast(HEAD_SET_CANCEL);
		} else {
			Uri uri = null;
			try {
				uri = Uri.parse(mTempPhotoPath);
				//uri = data.getData();
				startPhotoCrop(uri, null, HEAD_SAVE_PHOTO ,false);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * �����������
	 * @param data
	 * @param mBWphoto �Ƿ�Ϊ�ڰ���Ƭ
	 */
	public void getDataFromCamera(Intent data,boolean mBWphoto) {
		File temp = new File(mTempPhotoPath);
		if (!temp.exists()) {
			showToast(HEAD_SET_CANCEL);
		} else {
			Uri uri = null;
			try {
				uri = Uri.parse(mTempPhotoPath);
				startPhotoCrop(uri, null, HEAD_SAVE_PHOTO, mBWphoto );
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void getDataFromAlbum(Intent data) 
	{
		if (data == null) {
			showToast(HEAD_SET_CANCEL);
		} else {
			Uri uri = null;
			uri = data.getData();
			if (uri != null) {
				startPhotoCrop(uri, null, CropHelper.HEAD_SAVE_PHOTO,false);
			}
		}
	}
	
	//���и���Ƭ
	public Uri getDataFromAlbumWithoutCrop(Intent data)
	{
		if (data == null) 
		{
			showToast(HEAD_SET_CANCEL);
		} 
		else 
		{
			Uri uri = null;
			uri = data.getData();
			if (uri != null) 
			{
				//startPhotoCrop(uri, null, CropHelper.HEAD_SAVE_PHOTO,false);
				return uri;
			}
		}
		return null;
	}

	/**
	 * ��ͼ����Ϊ��ʱ�ļ�
	 * 
	 * @param data
	 * @return
	 */
	public boolean saveTempPhoto(Intent data) {
		if (data != null) {
			return BitmapUtils.saveFile(getBitmap(data), mTempPhotoPath);
		} else {
			showToast(HEAD_IMAGE_INVALID);
			return false;
		}
	}

	/**
	 * ��ͼ���浽ָ��Ŀ¼
	 * 
	 * @param data
	 * @return
	 */
	public boolean savePhoto(Intent data, String filePath) {
		if (data != null) {
			return BitmapUtils.saveFile(getBitmap(data), filePath);
		} else {
			showToast(HEAD_IMAGE_INVALID);
			return false;
		}
	}
	
	/**
	 * ������ʱ·���µĽ�ͼ�ļ���ָ��·��
	 * 
	 * @param filePath
	 * @return
	 */
	public boolean copyTempPhoto(String filePath) {
		return FileUtils.copyFile(mTempPhotoPath, filePath);
	}

	/**
	 * ��ȡ��ͼ��ŵ���ʱ·��
	 * 
	 * @return
	 */
	public String getTempPath() {
		return mTempPhotoPath;
	}
}
