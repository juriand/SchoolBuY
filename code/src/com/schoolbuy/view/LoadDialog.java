package com.schoolbuy.view;


import com.example.schoolbuy.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

public class LoadDialog extends Dialog 
{  
    public LoadDialog(Context context, int theme) 
    {  
        super(context, theme);  
        this.setContentView(R.layout.list_progress_dialog);   
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
