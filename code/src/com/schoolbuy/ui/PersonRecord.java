package com.schoolbuy.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.RecordAdapter;
import com.schoolbuy.bean.Member;
import com.schoolbuy.bean.Record;
import com.schoolbuy.view.LoadDialog;

public class PersonRecord extends Activity
{
	private List<Record> recordList = new ArrayList<Record>();
	private ListView recordListView;
	
	private RecordAdapter itemAdapter;
	
	private Button backButton;
	private ImageButton loadAgainButton;
	
	private Member currentUser;
	
	private ImageView loadNothing;
	private LoadDialog loadDialog;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_record);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
        
        init();
        click();
    }
	
	public void init()
	{
		loadNothing = (ImageView)findViewById(R.id.load_nothing);
		
		loadDialog = new LoadDialog(this,R.style.LoadDialog);
		loadDialog.show();
		
		backButton = (Button)findViewById(R.id.back);
    	loadAgainButton = (ImageButton)findViewById(R.id.loadAgain);
    	loadAgainButton.setVisibility(View.GONE);
		
		currentUser = BmobUser.getCurrentUser(this,Member.class);

		refresh();
	}
	
	private void refresh() {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){
		    @Override
		    public void run() {
		    // TODO Auto-generated method stub
		    	BmobQuery<Record> query1 = new BmobQuery<Record>();
		    	query1.addWhereEqualTo("sellerID",currentUser.getUsername());
		    	
		    	BmobQuery<Record> query2 = new BmobQuery<Record>();
		    	query2.addWhereEqualTo("buyerID",currentUser.getUsername());
		    	
		    	List<BmobQuery<Record>> queries = new ArrayList<BmobQuery<Record>>();
		    	queries.add(query1);
		    	queries.add(query2);
		    	BmobQuery<Record> mainQuery = new BmobQuery<Record>();
		    	mainQuery.or(queries);
		    	mainQuery.order("-createdAt");
		    	mainQuery.findObjects(PersonRecord.this, new FindListener<Record>() {
		    	    @Override
		    	    public void onSuccess(List<Record> object) {
		    	        // TODO Auto-generated method stub
		    	    	if(object.size() == 0)
			        	{
			        		loadNothing.setVisibility(View.VISIBLE);
			        	}
		    	    	else
		    	    	{
		    	    		for (Record item : object) 
		        	    	{
		        	    		recordList.add(item);
		    	            }
		    	            itemAdapter = new RecordAdapter(PersonRecord.this, R.layout.person_record_item, recordList);
		    	            recordListView = (ListView)findViewById(R.id.list);
		    	            recordListView.setAdapter(itemAdapter);   	
		    	    	}
		    	    	
		    	    }
		    	    @Override
		    	    public void onError(int code, String msg) {
		    	        // TODO Auto-generated method stub
		    	    	Toast.makeText(getApplicationContext(), "≤È—Ø ß∞‹"+msg, Toast.LENGTH_SHORT).show();
		    	    	loadAgainButton.setVisibility(View.VISIBLE);
		    	    }
		    	});
		    	loadDialog.dismiss();
				}}).start();
	}

	public void click()
	{		
		backButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});		
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
