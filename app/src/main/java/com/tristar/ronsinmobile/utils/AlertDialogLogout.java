package com.tristar.ronsinmobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tristar.ronsinmobile.LoginActivity;
import com.tristar.ronsinmobile.R;


@SuppressWarnings("ALL")
public class AlertDialogLogout {
	private static AlertDialog.Builder builder;
	private static Dialog dialog;
	private static AlertDialog alert;
	public static void showAlertDialog(final Activity activity, boolean onlyTitle , String message, String title) {

//		builder = new AlertDialog.Builder(activity);
//		builder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//		           public void onClick(DialogInterface dialog, int id) {
//		        	   alert.dismiss();
//		           }
//		       });
//		alert = builder.create();
//		alert.show();


		dialog = new Dialog(activity);
		dialog.setCanceledOnTouchOutside(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.message);


		TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
		TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
		TextView yes = (TextView) dialog.findViewById(R.id.dialog_Ok);
		TextView no = (TextView) dialog.findViewById(R.id.dialog_cancel);
		ImageView imgClose = (ImageView) dialog.findViewById(R.id.close_img);


		if (onlyTitle){
			txtMessage.setVisibility(View.VISIBLE);
		}else {
			txtMessage.setVisibility(View.GONE);
		}

		imgClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		txtTitle.setText(title);
		txtMessage.setText(message);
		yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent= new Intent(activity, LoginActivity.class);
				activity.startActivity(intent);
				dialog.dismiss();
			}
		});
		no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});


		dialog.show();

	}

}
