package com.tristar.ronsinmobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tristar.ronsinmobile.R;


@SuppressWarnings("ALL")
public class AlertDialogSelectType {
    private static AlertDialog.Builder builder;
    private static Dialog dialog;
    private static AlertDialog alert;

    public static void showAlertDialog(Activity activity, String message) {

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
        dialog.setContentView(R.layout.dialog_selects_scantype);


        TextView Message = (TextView) dialog.findViewById(R.id.txt_dialog);
        TextView single = (TextView) dialog.findViewById(R.id.dialog_Ok);
        TextView multiple = (TextView) dialog.findViewById(R.id.dialog_cancel);

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionData.getInstance().setScan_type("S");
                SessionData.getInstance().setScan_dialog_handler("N");


                dialog.dismiss();
            }
        });
        multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionData.getInstance().setScan_type("M");
                SessionData.getInstance().setScan_dialog_handler("D");


                dialog.dismiss();
            }
        });
        dialog.show();

    }


}
