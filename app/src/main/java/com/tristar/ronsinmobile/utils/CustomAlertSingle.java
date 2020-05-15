package com.tristar.ronsinmobile.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tristar.ronsinmobile.R;

import top.defaults.drawabletoolbox.DrawableBuilder;

import static android.content.Context.MODE_PRIVATE;
import static com.tristar.ronsinmobile.ScanActivity.COLOR_PREFERENCE;


@SuppressWarnings("ALL")
public class CustomAlertSingle extends Dialog implements
        android.view.View.OnClickListener{
    public boolean isConfirmDialog = false;
    public boolean haveIntent = false;
    public final Activity parentActivity;
    public TextView yes, no;
    public final String message;
    private Intent intent;
    public final static int SYNC = 101;
    public String title="";
    public ImageView imgClose;
    int sync = -1;

    TextView txtTitle;
    public LinearLayout lnrRoot,lnr_bottom;
    public RelativeLayout rlHeader;
    public View view_ok;
    public Intent toActivity;

    private String HeaderBackgroundColor, HeaderTextColor, ButtonColor, ButtonTextColor, TextColor, BackgroundImage;


    public CustomAlertSingle(Activity activity, String message) {
        super(activity);
        this.parentActivity = activity;
        this.message = message;
        isConfirmDialog = false;
    }

    public CustomAlertSingle(Activity a, String message, int sync) {
        super(a);
        this.parentActivity = a;
        this.message = message;
        //isConfirmDialog = false;
        isConfirmDialog = true;
        this.sync = sync;
    }

    public CustomAlertSingle(Activity activity, String message,
                             boolean confirmDialog, int sync) {
        super(activity);
        this.parentActivity = activity;
        this.message = message;
        this.isConfirmDialog = true;

        this.sync = sync;
    }

    public CustomAlertSingle(Activity a, String message, Intent intent) {
        super(a);
        this.parentActivity = a;
        this.message = message;
        isConfirmDialog = false;
        this.intent = intent;
    }

    public CustomAlertSingle(Activity activity, String message,
                             boolean confirmDialog) {
        super(activity);
        this.parentActivity = activity;
        this.message = message;
        this.isConfirmDialog = confirmDialog;
    }

    public CustomAlertSingle(Activity activity, String message,
                             boolean confirmDialog, Intent intent) {
        super(activity);
        this.parentActivity = activity;
        this.message = message;
        this.isConfirmDialog = confirmDialog;
        this.intent = intent;
    }

    public CustomAlertSingle(Activity activity, String title,String message,
                             boolean confirmDialog) {
        super(activity);
        this.parentActivity = activity;
        this.message = message;
        this.isConfirmDialog = confirmDialog;
        this.title = title;

    }
    public CustomAlertSingle(Activity activity, String title,String message,
                             boolean confirmDialog,Intent intent) {
        super(activity);
        this.parentActivity = activity;
        this.message = message;
        this.isConfirmDialog = confirmDialog;
        this.title = title;

    }
  public CustomAlertSingle(Activity activity, String title,String message,
                             boolean confirmDialog,boolean haveIntent,Intent toActivity) {
        super(activity);
        this.parentActivity = activity;
        this.message = message;
        this.isConfirmDialog = confirmDialog;
        this.haveIntent = haveIntent;
        this.toActivity = toActivity;
        this.title = title;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alertdialog);
        yes = (TextView) findViewById(R.id.btn_yes);
        no = (TextView) findViewById(R.id.btn_no);
        imgClose = (ImageView) findViewById(R.id.close_img);
        lnrRoot = (LinearLayout) findViewById(R.id.lnr_root);
        lnr_bottom = (LinearLayout) findViewById(R.id.lnr_bottom);
        rlHeader = (RelativeLayout) findViewById(R.id.rl_header);
        view_ok = (View) findViewById(R.id.view_ok);
        imgClose = (ImageView) findViewById(R.id.close_img);
        txtTitle = (TextView) findViewById(R.id.txt_Title);

        getColorPreference();


        if (!isConfirmDialog) {
            yes.setVisibility(View.GONE);
//            findViewById(R.id.btn_devider).setVisibility(View.GONE);
            no.setText("OK");
            lnr_bottom.removeView(no);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, .5f);
            lnr_bottom.addView(no, params);

//            if (haveIntent){
////                Intent intent=new Intent(parentActivity,toActivity.getClass());
//                parentActivity.startActivity(toActivity);
//            }
        } else {
            yes.setVisibility(View.VISIBLE);
//            findViewById(R.id.btn_devider).setVisibility(View.VISIBLE);
            no.setVisibility(View.VISIBLE);
            no.setText("Cancel");
            yes.setText("Ok");
        }

        ((TextView) findViewById(R.id.txt_dialog)).setText(message);
        ((TextView) findViewById(R.id.txt_Title)).setText(title);

        no.setOnClickListener(this);
        yes.setOnClickListener(this);
        imgClose.setOnClickListener(this);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_no:

                dismiss();
                break;
            case R.id.btn_yes:

                dismiss();

                break;

            case R.id.close_img:
               dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


    public void setBackgroundImageDynamically(View view, String image) {

        try {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            view.setBackground(new BitmapDrawable(parentActivity.getResources(), decodedByte));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setColorDynamicallyForButton(Button btn, String bgcolor, String textColor) {
        Drawable drawable = null;
        try {

            drawable = new DrawableBuilder()
                    .rectangle()
                    .solidColor(Color.parseColor(bgcolor))
                    .bottomLeftRadius(10) // in pixels
                    .bottomRightRadius(10)
                    .topLeftRadius(10)
                    .topRightRadius(10)
                    .build();

            btn.setBackground(drawable);
            btn.setTextColor(Color.parseColor(textColor));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColorDynamicallyForEditText(EditText editText, String borderColor, String textColor) {
        try {

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[]{8, 8, 8, 8, 8, 8, 8, 8});
            shape.setStroke(3, Color.parseColor(borderColor));
            editText.setBackground(shape);

            editText.setTextColor(Color.parseColor(textColor));
            editText.setHintTextColor(Color.GRAY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setColorDynamicallyForHeaderLayout(View view, String color) {
        Drawable drawable = null;
        try {

            drawable = new DrawableBuilder()
                    .rectangle()
                    .solidColor(Color.parseColor(color))

                    .build();


            view.setBackground(drawable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColorDynamicallyForLayoutList(ListView view, String color) {
        Drawable drawable = null;
        try {

            drawable = new DrawableBuilder()
                    .rectangle()
                    .solidColor(Color.parseColor(color))

                    .build();


            view.setBackground(drawable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColorDynamicallyForHeaderTextView(TextView textView, String bgColor, String txtColor) {


        try {
            textView.setTextColor(Color.parseColor(txtColor));
            textView.setBackgroundColor(Color.parseColor(bgColor));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setColorDynamicallyForNormalTextView(TextView textView, String color) {


        try {
            textView.setTextColor(Color.parseColor(color));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getColorPreference() {
        SharedPreferences prefs = parentActivity.getSharedPreferences(COLOR_PREFERENCE, MODE_PRIVATE);

        HeaderBackgroundColor = prefs.getString("HeaderBackgroundColor", "#007AE0");
        HeaderTextColor = prefs.getString("HeaderTextColor", "#ffffff");
        ButtonColor = prefs.getString("ButtonColor", "#007AE0");
        ButtonTextColor = prefs.getString("ButtonTextColor", "#ffffff");
        TextColor = prefs.getString("TextColor", "#000000");
        BackgroundImage = prefs.getString("BackgroundImage", "#ffffff");


        applyColorToLayout();
    }


    public void applyColorToLayout() {



//        setBackgroundImageDynamically(lnrRoot, BackgroundImage);
        setColorDynamicallyForNormalTextView(yes, TextColor);
        setColorDynamicallyForNormalTextView(no, TextColor);

        setColorDynamicallyForLayout(lnrRoot, HeaderBackgroundColor);
        setColorDynamicallyForHeaderLayout(lnr_bottom, HeaderBackgroundColor);
        setColorDynamicallyForHeaderLayout(view_ok, HeaderBackgroundColor);
        setColorDynamicallyForHeaderLayout(rlHeader, HeaderBackgroundColor);

//        setColorDynamicallyForButton(btnAddProfile, ButtonColor, ButtonTextColor);

        setColorDynamicallyForHeaderTextView(txtTitle, HeaderBackgroundColor, HeaderTextColor);



//        setColorDynamicallyForEditText(edtCompanyCode, HeaderBackgroundColor, "#000000");
//        setColorDynamicallyForEditText(editUserId, HeaderBackgroundColor, "#000000");
//        setColorDynamicallyForEditText(editPassword, HeaderBackgroundColor, "#000000");


    }

    public void setColorDynamicallyForLayout(View view, String borderColor) {
        try {

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.WHITE);
            shape.setCornerRadii(new float[]{8, 8, 8, 8, 8, 8, 8, 8});
            shape.setStroke(3, Color.parseColor(borderColor));
            view.setBackground(shape);

//            view.setTextColor(Color.parseColor(textColor));
//            view.setHintTextColor(Color.GRAY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






}