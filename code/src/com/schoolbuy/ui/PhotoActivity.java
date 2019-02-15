package com.schoolbuy.ui;

import java.io.File;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.example.schoolbuy.R;
import com.schoolbuy.bean.Item;
import com.schoolbuy.bean.Member;
import com.schoolbuy.cropimage.CropImage;
import com.schoolbuy.view.MyProgressDialog;

public class PhotoActivity extends Activity
{
	private ImageButton yesButton, closeButton, exchangeButton, auctionButton, buyButton;
	private ImageView photoView;
	private Boolean isExchange = false, isAuction = false, isBuy = false;
	private float itemPrice;
	private String itemName, itemIntro, itemPath, tempTag, itemTarget="";
	private int itemKind;
	private String[] itemTag = new String[3];
	private Bitmap tempImage;
	private BmobFile itemIcon;
	
	private MyProgressDialog progressDialog;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo);
        
        init();
        click();
     
    }
	
	public void init()
	{
		Bundle bundle = this.getIntent().getExtras();
		itemName = bundle.getString("name");
		itemIntro = bundle.getString("intro");
		itemPrice = Float.parseFloat(bundle.getString("price"));
		
		StringTokenizer st = new StringTokenizer(bundle.getString("tag"));
		for(int i = 0 ; st.hasMoreTokens() && i<6 ; i++)
		{
			itemTag[i] = st.nextToken();
		}
		
		tempImage = (Bitmap) bundle.getParcelable("data");
		itemPath = bundle.getString("path");
		itemIcon = new BmobFile(new File(itemPath));
		
		tempImage = BitmapFactory.decodeFile(itemPath); 
		photoView = (ImageView)findViewById(R.id.photo);
		photoView.setImageBitmap(tempImage);
		
		yesButton = (ImageButton)findViewById(R.id.yes);
		closeButton = (ImageButton)findViewById(R.id.close);
		exchangeButton = (ImageButton)findViewById(R.id.exchange);
		auctionButton = (ImageButton)findViewById(R.id.auction);
		buyButton = (ImageButton)findViewById(R.id.contact);	
		
		progressDialog = new MyProgressDialog(this,R.style.CustomProgressDialog);
	}
	
	public void click()
	{
		closeButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		
		exchangeButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{			
				if( !isExchange && !isAuction && !isBuy)
				{
					exchangeButton.setImageResource(R.drawable.photo_exchange_press);
					isExchange = true;
				}
				else
				{
					if(isExchange)
					{
						exchangeButton.setImageResource(R.drawable.photo_exchange);
						isExchange = false;
					}
				}
			}
		});
		
		auctionButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{		
				if( !isExchange && !isAuction && !isBuy)
				{
					auctionButton.setImageResource(R.drawable.photo_auction_press);
					isAuction = true;
				}
				else
				{
					if(isAuction)
					{
						auctionButton.setImageResource(R.drawable.photo_auction);
						isAuction = false;
					}
				}
				
			}
		});
		
		buyButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if( !isExchange && !isAuction && !isBuy)
				{
					buyButton.setImageResource(R.drawable.photo_buy_press);
					isBuy = true;
				}
				else
				{
					if(isBuy)
					{
						buyButton.setImageResource(R.drawable.photo_buy);
						isBuy = false;
					}
				}
				
			}
		});
		
		yesButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				if(!isExchange && !isAuction && !isBuy)
				{
					Toast.makeText(PhotoActivity.this, "��ѡ�񷢲���Ϣ������", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(isExchange)
					{
						//want
						LayoutInflater factory = LayoutInflater.from(PhotoActivity.this);
		                final View textEntryView = factory.inflate(R.layout.photo_dialog, null);
						new AlertDialog.Builder(PhotoActivity.this).setTitle("���뻻��ʲô����").
						setView(textEntryView).
						setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
							     {
					                    public void onClick(DialogInterface dialog, int whichButton) 
					                    {
					                        EditText tv=(EditText)textEntryView.findViewById(R.id.inputTarget);
					                        itemTarget = tv.getText().toString();
					                        
					                        progressDialog.show();
					                        add();
					                    }
					                  })
							     .setNegativeButton("ȡ��", null).show();
					}
					else
					{
						add();
					}
				}
			}
		});
	}
	
	public void add()
	{
		new Thread(new Runnable(){
		    @Override
		    public void run() {
		    // TODO Auto-generated method stub
		    	itemIcon.uploadblock(PhotoActivity.this, new UploadFileListener() {

				    @Override
				    public void onSuccess() {
				        // TODO Auto-generated method stub
				    	Toast.makeText(PhotoActivity.this, "�ϴ�ͼƬ�ɹ�", Toast.LENGTH_SHORT).show();
				    	
				    	final Item newItem = new Item();
				    	final Member currentUser = BmobUser.getCurrentUser(PhotoActivity.this,Member.class);
						
						newItem.setItemName(itemName);
						newItem.setItemPrice(itemPrice);
						newItem.setItemIntro(itemIntro);
						newItem.setItemTag(itemTag);
						newItem.setTarget(itemTarget);
						newItem.setItemStatus(2);
						newItem.setOwner(currentUser.getUsername());
						
						if(isExchange)
						{
							newItem.setItemKind(2);
						}
						else if(isAuction)
						{
							newItem.setItemKind(3);
						}
						else
						{
							newItem.setItemKind(1);
						}
				    	newItem.setItemIcon(itemIcon);			
						newItem.save(PhotoActivity.this, new SaveListener() {

						    @Override
						    public void onSuccess() {
						        // TODO Auto-generated method stub	
						    	currentUser.addItem(newItem.getObjectId());
						    	newItem.setOwner(currentUser.getUsername());
						    	
						    	Toast.makeText(PhotoActivity.this, "��ӳɹ�", Toast.LENGTH_SHORT).show();

						    	Intent myIntent = new Intent(PhotoActivity.this,MainActivity.class);
						    	PhotoActivity.this.startActivity(myIntent);
						    	
						    	NewItemActivity.instance.finish();
						    	
						    	finish();
						    }

						    @Override
						    public void onFailure(int code, String arg0) {
						        // TODO Auto-generated method stub
						        // ���ʧ��
						    	Toast.makeText(PhotoActivity.this, "���ʧ��"+arg0, Toast.LENGTH_SHORT).show();
						    }
						});
				    }

				    @Override
				    public void onProgress(Integer value) {
				        // TODO Auto-generated method stub
				        // ���ص��ϴ����ȣ��ٷֱȣ�
				    }

				    @Override
				    public void onFailure(int code, String msg) {
				        // TODO Auto-generated method stub
				    	Toast.makeText(PhotoActivity.this, "�ϴ�ͼƬʧ��"+msg, Toast.LENGTH_SHORT).show();
				    }
				});		
				progressDialog.dismiss();
				}}).start();
		
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {   
        	finish();
            return true;   
        }
        return super.onKeyDown(keyCode, event);
    }
}
