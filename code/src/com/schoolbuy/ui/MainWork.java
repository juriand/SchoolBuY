package com.schoolbuy.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.example.schoolbuy.R;
import com.schoolbuy.adapter.WorkAdapter;
import com.schoolbuy.bean.WorkInfor;
import com.schoolbuy.view.LoadDialog;

public class MainWork extends Activity implements OnItemClickListener
{ 
	private List<WorkInfor> workList = new ArrayList<WorkInfor>();
	private ListView workListView;
	
	private LoadDialog loadDialog;
	private ImageView loadNothing;
	private ImageButton loadAgainButton;
	
	WorkAdapter workAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.itemlist);
        
        Bmob.initialize(this, "a00f5a1a5b5eb533699823f2f704b706");
 
        init();
        click();
    }
    
    public void init()
    {
    	loadDialog = new LoadDialog(this,R.style.LoadDialog);
    	
    	loadAgainButton = (ImageButton)findViewById(R.id.loadAgain);
    	loadAgainButton.setVisibility(View.GONE);
    	
    	loadNothing = (ImageView)findViewById(R.id.load_nothing);
    	
    	loadDialog.show();
		refresh();	
    }
    
    public void click()
    {
    	loadAgainButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				loadAgainButton.setVisibility(View.GONE);
				loadDialog.show();
				refresh();
			}
    		
    	});
    }
    
    public void refresh()
	{
    	new Thread(new Runnable(){
		    @Override
		    public void run() {
		    // TODO Auto-generated method stub
		    BmobQuery<WorkInfor> query = new BmobQuery<WorkInfor>();
		    query.order("-createdAt");
		    //����50�����ݣ����������������䣬Ĭ�Ϸ���10������
		    query.setLimit(50);
		    //ִ�в�ѯ����
		    query.findObjects(MainWork.this, new FindListener<WorkInfor>() 
		    {
		    	@Override
		    	public void onSuccess(List<WorkInfor> object) 
		    	{
		    		// TODO Auto-generated method stub
		    		if(object.size() == 0)
		    	    {
		    	        loadNothing.setVisibility(View.VISIBLE);
		    	    }
		    	    else
		    	    {
		    	    	for (WorkInfor work : object) 
		    	    	{
		    	    		workList.add(work);
		    	    	}
		    	    	workAdapter = new WorkAdapter(MainWork.this, R.layout.main_work_item, workList);
		    	    	workListView = (ListView)findViewById(R.id.list);
		    	    	workListView.setAdapter(workAdapter);   	
		    	    	workListView.setOnItemClickListener(MainWork.this);
		    	    }
		    	}
		    	@Override
		    	public void onError(int code, String msg) 
		    	{
		    		// TODO Auto-generated method stub
		    		Toast.makeText(getApplicationContext(), "��ѯʧ��"+msg, Toast.LENGTH_SHORT).show();
		    		
		    		loadAgainButton.setVisibility(View.VISIBLE);
		    	}
		    });		
		    	loadDialog.dismiss();
				}}).start();
	}
    
    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
    
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
            if((System.currentTimeMillis()-exitTime) > 2000){  
                Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();                                
                exitTime = System.currentTimeMillis();   
            } else {
                finish();
                System.exit(0);
            }
            return true;   
        }
        return super.onKeyDown(keyCode, event);
    }
}