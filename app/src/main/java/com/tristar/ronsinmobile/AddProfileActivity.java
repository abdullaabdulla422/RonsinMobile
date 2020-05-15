package com.tristar.ronsinmobile;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.tristar.ronsinmobile.db.DbHelper;
import com.tristar.ronsinmobile.utils.CustomAlertSingle;
import com.tristar.ronsinmobile.utils.SessionData;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class AddProfileActivity extends AppCompatActivity {

    EditText edtCompanyName, edtCompanyCode, edtUserId, edtPassword;
    Button addProfile, cancelProfile;
    private boolean isUpdate;
    private String id, strCompanyCode, strUserId, strPassword, strCompanyName;
    private DbHelper mHelper;
    private SQLiteDatabase dataBase;
    public static SharedPreferences.Editor loginPrefsEditors;
    private String HeaderBackgroundColor, HeaderTextColor, ButtonColor, ButtonTextColor, TextColor, BackgroundImage;
    public static final String COLOR_PREFERENCE = "color_pref";
    RelativeLayout rlHeader;
    LinearLayout lrHeader;
    TextView textHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_profile_activity);

        initialize();
        getColorPreference();

        mHelper = new DbHelper(this);

        isUpdate = getIntent().getExtras().getBoolean("update");
        if (isUpdate) {
            id = getIntent().getExtras().getString("ID");
            strCompanyCode = getIntent().getExtras().getString("Fname");
            strUserId = getIntent().getExtras().getString("Lname");
            strPassword = getIntent().getExtras().getString("Fpass");
            strCompanyName = getIntent().getExtras().getString("lcompanyname");
            edtCompanyCode.setText(strCompanyCode);
            edtUserId.setText(strUserId);
            edtPassword.setText(strPassword);
            edtCompanyName.setText(strCompanyName);

        }


        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strCompanyCode = edtCompanyCode.getText().toString().trim();
                Log.d("firstname", "" + strCompanyCode);
                strUserId = edtUserId.getText().toString().trim();
                Log.d("lnamename", "" + strUserId);
                strPassword = edtPassword.getText().toString().trim();
                Log.d("lpassname", "" + strPassword);
                strCompanyName = edtCompanyName.getText().toString().trim();
                Log.d("lcompanyname", "" + strCompanyName);


                if (strCompanyCode.length() > 0 && strUserId.length() > 0 && strPassword.length() > 0 && strCompanyName.length() > 0) {
                    saveData();
                    SessionData.getInstance().setSelectedserver(strCompanyCode + "," + strUserId + "," + strPassword + "," + strCompanyName);

                } else {

                    if(strCompanyCode.length()==0 && strUserId.length()==0 && strPassword.length()==0 && strCompanyName.length()==0){
                        new CustomAlertSingle(AddProfileActivity.this, "Please Enter All Details").show();

                    }

                    else if (strCompanyCode.length() == 0) {

                        new CustomAlertSingle(AddProfileActivity.this, "Please Enter Company Code").show();
// new CustomAlertSingle(AddProfileActivity.this, validationMessage).show();
                    }else if (strUserId.length()==0){
                        new CustomAlertSingle(AddProfileActivity.this, "Please Enter User ID").show();

                    }else if (strPassword.length()==0){
                        new CustomAlertSingle(AddProfileActivity.this, "Please Enter Password").show();

                    }else if (strCompanyName.length()==0){
                        new CustomAlertSingle(AddProfileActivity.this, "Please Enter Company Name").show();

                    }

                }

            }
        });

        cancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


    }

    private void initialize() {

        edtCompanyName = (EditText) findViewById(R.id.edt_company_name);
        edtCompanyCode = (EditText) findViewById(R.id.edt_company_code);
        edtUserId = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        textHeader = (TextView) findViewById(R.id.text_header);


        addProfile = (Button) findViewById(R.id.addProfile);
        cancelProfile = (Button) findViewById(R.id.cancelProfile);
        rlHeader = (RelativeLayout) findViewById(R.id.rl_header);
        lrHeader = (LinearLayout) findViewById(R.id.lnr_header);


    }

    private void saveData() {
        dataBase = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DbHelper.KEY_FNAME, strCompanyCode);
        values.put(DbHelper.KEY_LNAME, strUserId);
        values.put(DbHelper.KEY_LPASS, strPassword);
        values.put(DbHelper.KEY_LCOMPANYNAME, strCompanyName);


        System.out.println("");
        if (isUpdate) {
            dataBase.update(DbHelper.TABLE_NAME, values, DbHelper.KEY_ID + "=" + id, null);
        } else {
            dataBase.insert(DbHelper.TABLE_NAME, null, values);
        }
        dataBase.close();
        finish();
    }

    public void setBackgroundImageDynamically(View view, String image) {

        try {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            view.setBackground(new BitmapDrawable(AddProfileActivity.this.getResources(), decodedByte));
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
// in pixels
// .cornerRadii(0, 0, 20, 20) // the same as the two lines above
                    .build();


            btn.setBackground(drawable);
            btn.setTextColor(Color.parseColor(textColor));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setColorDynamicallyForNormalTextView(TextView textView, String color) {


        try {
            textView.setTextColor(Color.parseColor(color));
        } catch (Exception e) {

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


    public void setColorDynamicallyForHeaderTextView(TextView textView, String color) {


        try {
            textView.setTextColor(Color.parseColor(color));
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



    public void getColorPreference() {
        SharedPreferences prefs = getSharedPreferences(COLOR_PREFERENCE, MODE_PRIVATE);

        HeaderBackgroundColor = prefs.getString("HeaderBackgroundColor", "#007AE0");
        HeaderTextColor = prefs.getString("HeaderTextColor", "#ffffff");
        ButtonColor = prefs.getString("ButtonColor", "#007AE0");
        ButtonTextColor = prefs.getString("ButtonTextColor", "#ffffff");
        TextColor = prefs.getString("TextColor", "#000000");
        BackgroundImage = prefs.getString("BackgroundImage", "#ffffff");


        applyColorToLayout();
    }

    public void applyColorToLayout() {
        if (!BackgroundImage.equalsIgnoreCase("#ffffff")) {

            setBackgroundImageDynamically(lrHeader, BackgroundImage);
        }
        setColorDynamicallyForHeaderLayout(rlHeader, HeaderBackgroundColor);
        setColorDynamicallyForHeaderTextView(textHeader, HeaderTextColor);

        setColorDynamicallyForButton(addProfile, ButtonColor, ButtonTextColor);
        setColorDynamicallyForButton(cancelProfile, ButtonColor, ButtonTextColor);
        setColorDynamicallyForEditText(edtCompanyName, HeaderBackgroundColor, "#000000");
        setColorDynamicallyForEditText(edtCompanyCode, HeaderBackgroundColor, "#000000");
        setColorDynamicallyForEditText(edtUserId, HeaderBackgroundColor, "#000000");
        setColorDynamicallyForEditText(edtPassword, HeaderBackgroundColor, "#000000");

// setColorDynamicallyForEditText(editUserId, ButtonColor, ButtonTextColor);
// setColorDynamicallyForEditText(editPassword, ButtonColor, ButtonTextColor);
    }


    @Override
    public void onBackPressed() {

        finish();
    }
}