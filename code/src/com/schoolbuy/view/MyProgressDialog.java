package com.schoolbuy.view;


import com.example.schoolbuy.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

public class MyProgressDialog extends Dialog 
{  
    public MyProgressDialog(Context context, int theme) 
    {  
        super(context, theme);  
        this.setContentView(R.layout.my_progress_dialog);   
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
    }  
  
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) 
    {  
        if (!hasFocus) 
        {  
            dismiss();  
        }  
    }  
}
