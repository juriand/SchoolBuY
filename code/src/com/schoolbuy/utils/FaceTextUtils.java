package com.schoolbuy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

public class FaceTextUtils 
{
	/** 
	  * toSpannableString
	  * @return SpannableString
	  * @throws
	  */
	public static SpannableString toSpannableString(Context context, String text) {
		if (!TextUtils.isEmpty(text)) {
			SpannableString spannableString = new SpannableString(text);
			int start = 0;
			Pattern pattern = Pattern.compile("\\\\ue[a-z0-9]{3}", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				String faceText = matcher.group();
				String key = faceText.substring(1);
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
						context.getResources().getIdentifier(key, "drawable", context.getPackageName()), options);
				ImageSpan imageSpan = new ImageSpan(context, bitmap);
				int startIndex = text.indexOf(faceText, start);
				int endIndex = startIndex + faceText.length();
				if (startIndex >= 0)
					spannableString.setSpan(imageSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				start = (endIndex - 1);
			}

			return spannableString;
		} else {
			return new SpannableString("");
		}
	}

	public static SpannableString toSpannableString(Context context, String text, SpannableString spannableString) {

		int start = 0;
		Pattern pattern = Pattern.compile("\\\\ue[a-z0-9]{3}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String faceText = matcher.group();
			String key = faceText.substring(1);
			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inSampleSize = 2;
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources()
					.getIdentifier(key, "drawable", context.getPackageName()), options);
			ImageSpan imageSpan = new ImageSpan(context, bitmap);
			int startIndex = text.indexOf(faceText, start);
			int endIndex = startIndex + faceText.length();
			if (startIndex >= 0)
				spannableString.setSpan(imageSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			start = (endIndex - 1);
		}

		return spannableString;
	}

}
