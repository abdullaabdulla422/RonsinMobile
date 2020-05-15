package com.tristar.ronsinmobile.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.tristar.ronsinmobile.R;

@SuppressWarnings("ALL")
public class ProgressBar {

	public static Dialog commonProgressDialog;
	public static void showCommonProgressDialog(Activity activity) {
		commonProgressDialog = new Dialog(activity);
		commonProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		commonProgressDialog.setContentView(R.layout.progressbar);
		commonProgressDialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);
		commonProgressDialog.setCanceledOnTouchOutside(false);
		commonProgressDialog.setCancelable(false);	
		commonProgressDialog.show();
	}

	public static void dismiss() {
		if(commonProgressDialog != null) {
			commonProgressDialog.dismiss();
		}
	}
}