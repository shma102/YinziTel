package com.yinsidh.helper;


import android.app.Activity;
import android.app.Dialog;

import com.yinsidh.android.R;


public class MsgWaitDialog {
	static Dialog dialog;
	public static void waitdialog(Activity msgActivity){
		 
	   	 dialog = new Dialog(msgActivity, R.style.NoFrameNoDim_Dialog);
	   	 dialog.setContentView(R.layout.msgwaitdialog);
	     dialog.show(); 
	     dialog.setCanceledOnTouchOutside(false);
		 	
	}
	public static void waitdialog_close(){
		dialog.dismiss();
	}
	
}
