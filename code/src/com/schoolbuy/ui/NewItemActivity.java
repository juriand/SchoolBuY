package com.schoolbuy.ui;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.schoolbuy.R;
import com.schoolbuy.cropimage.CropHelper;
import com.schoolbuy.cropimage.CropImage;
import com.schoolbuy.utils.OSUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NewItemActivity extends Activity
{
	private CropHelper mCropHelper;
	private String date = "";
	
	private TextView chooseKindButton;
	private ImageButton sendButton, kind1Button, kind2Button, kind3Button, kind4Button, kind5Button;
	private EditText nameText, priceText, introText, tagText;
	
	private int itemKind = 0;
	
	private String kindText="";
	
	public static NewItemActivity instance = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new);
        instance = this;
        
        init();
        click();
     
    }
	
	public void init()
	{
		chooseKindButton = (TextView)findViewById(R.id.kindText);
		sendButton = (ImageButton)findViewById(R.id.send);
		nameText = (EditText)findViewById(R.id.inputName);
		priceText = (EditText)findViewById(R.id.inputPrice); 
		introText = (EditText)findViewById(R.id.inputIntro);
		tagText = (EditText)findViewById(R.id.inputTag);
		
		mCropHelper=new CropHelper(this, OSUtils.getSdCardDirectory()+"/head.png");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");       
		Date curDate = new Date(System.currentTimeMillis());      
		date = formatter.format(curDate);

	}
	
    public void click() 
    {
    	chooseKindButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//����
				LayoutInflater factory = LayoutInflater.from(NewItemActivity.this);
                final View textEntryView = factory.inflate(R.layout.add_dialog, null);
                
                kind1Button = (ImageButton)textEntryView.findViewById(R.id.kind1);
                kind2Button = (ImageButton)textEntryView.findViewById(R.id.kind2);
                kind3Button = (ImageButton)textEntryView.findViewById(R.id.kind3);
                kind4Button = (ImageButton)textEntryView.findViewById(R.id.kind4);
                kind5Button = (ImageButton)textEntryView.findViewById(R.id.kind5);
                
                //���ð�ť
                switch(itemKind)
            	{
            	case 1:
            		kind1Button.setImageResource(R.drawable.item_kind1_press);
            		break;
            	case 2:
            		kind2Button.setImageResource(R.drawable.item_kind2_press);
            		break;
            	case 3:
            		kind3Button.setImageResource(R.drawable.item_kind3_press);
            		break;
            	case 4:
            		kind4Button.setImageResource(R.drawable.item_kind4_press);
            		break;
            	case 5:
            		kind5Button.setImageResource(R.drawable.item_kind5_press);
            		break;
            	default:
            		break;
            	}
                
                //ѡ��
                kind1Button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(itemKind == 0)
						{
							itemKind = 1;
							kind1Button.setImageResource(R.drawable.item_kind1_press);
						}
						else if(itemKind == 1)
						{
							itemKind = 0;
							kind1Button.setImageResource(R.drawable.item_kind1);
						}
						
					}
                	
                });
                
                kind2Button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(itemKind == 0)
						{
							itemKind = 2;
							kind2Button.setImageResource(R.drawable.item_kind2_press);
						}
						else if(itemKind == 2)
						{
							itemKind = 0;
							kind2Button.setImageResource(R.drawable.item_kind2);
						}
						
					}
                	
                });
                
                kind3Button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(itemKind == 0)
						{
							itemKind = 3;
							kind3Button.setImageResource(R.drawable.item_kind3_press);
						}
						else if(itemKind == 3)
						{
							itemKind = 0;
							kind3Button.setImageResource(R.drawable.item_kind3);
						}
						
					}
                	
                });
                
                kind4Button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(itemKind == 0)
						{
							itemKind = 4;
							kind4Button.setImageResource(R.drawable.item_kind4_press);
						}
						else if(itemKind == 4)
						{
							itemKind = 0;
							kind4Button.setImageResource(R.drawable.item_kind4);
						}
						
					}
                	
                });
                
                kind5Button.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(itemKind == 0)
						{
							itemKind = 5;
							kind5Button.setImageResource(R.drawable.item_kind5_press);
						}
						else if(itemKind == 5)
						{
							itemKind = 0;
							kind5Button.setImageResource(R.drawable.item_kind5);
						}
						
					}
                	
                });
                
				new AlertDialog.Builder(NewItemActivity.this).setTitle("��Ʒ����").
				setView(textEntryView).
				setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
					     {
			                    public void onClick(DialogInterface dialog, int whichButton) 
			                    {
			                    	switch(itemKind)
			                    	{
			                    	case 1:
			                    		kindText="����С��";
			                    		break;
			                    	case 2:
			                    		kindText="�γ̸���";
			                    		break;
			                    	case 3:
			                    		kindText="ѧϰ����";
			                    		break;
			                    	case 4:
			                    		kindText="�����Ʒ";
			                    		break;
			                    	case 5:
			                    		kindText="����";
			                    		break;
			                    	default:
			                    		kindText="";
			                    		break;
			                    	}                    	
			                    	chooseKindButton.setText(kindText);
			                    }
			              })
					     .setNegativeButton("ȡ��", null).show();
			}
		});
    	
    	sendButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				//camera
				if(kindText.equals("") || nameText.getText().toString().equals("") 
						|| priceText.getText().toString().equals(""))
				{
					Toast.makeText(NewItemActivity.this, "��Ҫ��������Ʒ��Ϣ", Toast.LENGTH_SHORT).show();
				}
				else
				{
					mCropHelper.startCamera();
				}
				
			}
		});

    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		Log.e("onActivityResult", requestCode+"**"+resultCode);
		if(requestCode==RESULT_CANCELED)
		{
			return;
		}else
		{
			switch (requestCode) 
			{
			case CropHelper.HEAD_FROM_ALBUM:
				mCropHelper.getDataFromAlbum(data);
				Log.e("onActivityResult", "���յ�ͼ��ͼƬ");
				break;
			case CropHelper.HEAD_FROM_CAMERA:
				mCropHelper.getDataFromCamera(data);
				Log.e("onActivityResult", "���յ�����ͼƬ");
				break;
			case CropHelper.HEAD_SAVE_PHOTO:
				if(data!=null&&data.getParcelableExtra("data")!=null)
				{				
					mCropHelper.savePhoto(data, OSUtils.getSdCardDirectory()+"/" + date + ".png");			
					mCropHelper.getDataFromCamera(data);
					
					Intent myIntent = new Intent(NewItemActivity.this,PhotoActivity.class);
					Bundle bundle = new Bundle();
	            	bundle.putString("name", nameText.getText().toString());
	            	bundle.putString("price", priceText.getText().toString());
	            	bundle.putString("intro", introText.getText().toString());
	            	bundle.putString("tag", kindText + " " + tagText.getText().toString());
	            	bundle.putString("path", OSUtils.getSdCardDirectory()+"/" + date + ".png");
	            	myIntent.setDataAndType(data.getData(), "image/*");
	            	myIntent.putExtras(bundle);

					NewItemActivity.this.startActivity(myIntent);
				}
				break;
			default:
				break;
			}
		}
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {   
        	finish();
            return true;   
        }
        return super.onKeyDown(keyCode, event);
    }
	
	
}
