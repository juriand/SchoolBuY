package com.schoolbuy.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class UpdateUtils 
{
	public static class UpdateCheckUtils
	{
		public static boolean hasNetwork()
		{
			return NetworkUtils.haveInternet();
		}
		
		public static boolean canWriteFile(String path)
		{
			File file = new File(path);
			return file.exists() && file.canWrite();
		}
		
		/**
		 * �?测是否有安装未知源的权限，若没有，则只能安装market源程�?
		 * @return
		 */
		public static boolean IsAllowInstal()
		{
			Context context = NetworkUtils.getApplicationContext();
			if (null == context) return false;
			
			int result = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS,  0);     
	
			if (result == 0)
			{     
				return false;    
			}
			return true;
		}
	}
	
	public static boolean hasNewVersion(String serverVersion)
	{
		return !AppInfoUtils.getAppVersionName().equals(serverVersion);
	}
	
	/**
	 * 自动安装
	 * @param uri
	 * @param context
	 */
	public static boolean installAPK(String installFile)
	{
		Context context = NetworkUtils.getApplicationContext();
		if (null == context) return false;
		
		String fileVersion = AppInfoUtils.getAppVersionName(installFile);
		if(null == fileVersion || fileVersion.equals(AppInfoUtils.getAppVersionName())) return false;
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		intent.setDataAndType(Uri.parse("file://"+installFile) ,"application/vnd.android.package-archive");
		context.startActivity(intent);
		return true;
	}
}
