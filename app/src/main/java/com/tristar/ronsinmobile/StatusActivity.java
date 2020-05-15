package com.tristar.ronsinmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tristar.ronsinmobile.Webutils.WebServiceConsumer;
import com.tristar.ronsinmobile.Webutils.WinCopyConstants;
import com.tristar.ronsinmobile.object.CopyStatusObject;
import com.tristar.ronsinmobile.object.WorkOrderObject;
import com.tristar.ronsinmobile.utils.CustomAlertSingle;
import com.tristar.ronsinmobile.utils.ProgressBar;
import com.tristar.ronsinmobile.utils.SessionData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayAdapter<String> statusAdapter;
    private ArrayList<String> copyStatusTitleList = new ArrayList<>();
    TextView txtvalue;

    String Str_ScanedWorkOrder;
    LinearLayout lnrWorkorderHeader;

    RelativeLayout rlheader;
    Button btnSendStatus;
    TextView txtheader, txtworkorder;
    ImageView imgBackPress;
    TextView txtBackpress;
    RelativeLayout rltotalbg;
    ArrayList<CopyStatusObject> copyStatusObjects = new ArrayList<>();
    CopyStatusObject copyStatusObjectSelected = new CopyStatusObject();
    private String HeaderBackgroundColor, HeaderTextColor, ButtonColor, ButtonTextColor, TextColor, BackgroundImage;
    public static final String COLOR_PREFERENCE = "color_pref";
    EditText edtStatus;
    WorkOrderObject workOrderObject = new WorkOrderObject();
    WorkOrderObject workOrderObject1 = new WorkOrderObject();
    String StatusDate = "", StatusTime = "", Report = "", ServerCode = "none", DateTimeSubmitted = "";

    int Lineitem = 0;
    ListView list;
    ArrayList<WorkOrderObject> workOrderObjectList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_single_status);
        Loggers.addLog("StatusActivity Intent created");



        copyStatusObjects = SessionData.getInstance().getCopyStatusObjects();
        lnrWorkorderHeader = findViewById(R.id.workorder_header);
        imgBackPress = findViewById(R.id.imgbackpress);
        txtBackpress = findViewById(R.id.txt_backpress);
        txtvalue = (TextView) findViewById(R.id.txt_value);
        txtheader = (TextView) findViewById(R.id.txt_header);
        txtworkorder = (TextView) findViewById(R.id.txt_workorder);
        btnSendStatus = (Button) findViewById(R.id.btn_send_status);
        rltotalbg = (RelativeLayout) findViewById(R.id.rltotalbg);
        edtStatus = (EditText) findViewById(R.id.edt_status);

        rlheader = findViewById(R.id.rl_header);

        rlheader.setOnClickListener(this);


        imgBackPress.setOnClickListener(this);
        txtBackpress.setOnClickListener(this);
        btnSendStatus.setOnClickListener(this);

        getColorPreference();

        workOrderObject = SessionData.getInstance().getWorkOrderObject();
        workOrderObjectList.addAll(SessionData.getInstance().getScanned_WorkorderList());
        StatusDate = getDate(System.currentTimeMillis());
        StatusTime = getTime(System.currentTimeMillis());
        ServerCode = "none";


        for (int i = 0; i < copyStatusObjects.size(); i++) {
            copyStatusTitleList.add(copyStatusObjects.get(i).getTitle());
        }
// copyStatusTitleList.add("Test status");
// copyStatusTitleList.add("Order status");
// copyStatusTitleList.add("Delivery status");
// copyStatusTitleList.add("Transport status");
// copyStatusTitleList.add("Receive status");

        list = (ListView) findViewById(R.id.list_status);

        statusAdapter = new ArrayAdapter<String>(this, R.layout.choise_list, copyStatusTitleList);
        list.setAdapter(statusAdapter);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Selected Status", "" + i);

                Report = copyStatusObjects.get(i).getPhrase();
            }
        });

// Bundle bundle = getIntent().getExtras();
        if (SessionData.getInstance().getValidate_record_Deligence_Scanned_result() == 1) {
            SessionData.getInstance().setValidate_record_Deligence_Scanned_result(0);

            Str_ScanedWorkOrder = SessionData.getInstance().getSingleRawResult();
            txtvalue.setText(Str_ScanedWorkOrder);
//            txtvalue.setText(workOrderObject.getWorkOrder());
            txtvalue.setVisibility(View.VISIBLE);

// String scan_value = bundle.getString("value");
// Str_ScanedWorkOrder = scan_value;
// txtvalue.setText(scan_value);
// setting colours

// rlheader.setBackgroundColor(Color.RED);
// btnSendStatus.setBackgroundColor(Color.RED);

        }

        list.setItemsCanFocus(false);
// we want multiple clicks

        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if (SessionData.getInstance().getScan_type().contains("S")) {
            lnrWorkorderHeader.setVisibility(View.VISIBLE);
        } else {
            lnrWorkorderHeader.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        if (v == btnSendStatus) {
//            SessionData.getInstance().getScanned_Workorder().clear();
//            SessionData.getInstance().setScan_dialog_handler("N"); // NORMAL SCAN

            if (Report.length() == 0) {
                Report = edtStatus.getText().toString();
            }

            if (Report.length() == 0) {
                Toast.makeText(getApplicationContext(), "Select or Enter the Status", Toast.LENGTH_SHORT).show();
            } else {
                if (SessionData.getInstance().getScan_type().contains("S")) {
                    new AsyncSubmitCopyStatus().execute();

                } else {
                    if (workOrderObjectList.size() > 0) {
                        workOrderObject1 = workOrderObjectList.get(0);
                        new AsyncSubmitCopyStatusMultiple().execute();
                    }

                }

            }

        }
        if (v == imgBackPress) {
            SessionData.getInstance().setValidate_record_Deligence_Scanned_result(0);
            Intent i = new Intent(StatusActivity.this, ScanActivity.class);

            startActivity(i);
            finish();
        }
        if (v == txtBackpress) {
            SessionData.getInstance().setValidate_record_Deligence_Scanned_result(0);
            Intent i = new Intent(StatusActivity.this, ScanActivity.class);
            startActivity(i);
            finish();
        }
    }


    public void setBackgroundImageDynamically(View view, String image) {

        try {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            view.setBackground(new BitmapDrawable(StatusActivity.this.getResources(), decodedByte));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setColorDynamicallyForButton(Button btn, String bgcolor, String textColor) {
        Drawable drawable = null;
        try {
            if (bgcolor.length()!=0) {
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
            }

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

    @Override
    public void onBackPressed() {
        SessionData.getInstance().setValidate_record_Deligence_Scanned_result(0);
        Intent i = new Intent(StatusActivity.this, ScanActivity.class);
        startActivity(i);
        finish();
// super.onBackPressed();
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
            setBackgroundImageDynamically(rltotalbg, BackgroundImage);
        }
        setColorDynamicallyForNormalTextView(txtworkorder, TextColor);
        setColorDynamicallyForNormalTextView(txtvalue, TextColor);

        setColorDynamicallyForHeaderLayout(rlheader, HeaderBackgroundColor);
        setColorDynamicallyForHeaderTextView(txtheader, HeaderTextColor);
        setColorDynamicallyForHeaderTextView(txtBackpress, HeaderTextColor);

        setColorDynamicallyForButton(btnSendStatus, ButtonColor, ButtonTextColor);
// setColorDynamicallyForButton(btnCompanyNewName, ButtonColor, ButtonTextColor);

        setColorDynamicallyForEditText(edtStatus, HeaderBackgroundColor, "#000000");
// setColorDynamicallyForEditText(editUserId, ButtonColor, ButtonTextColor);
// setColorDynamicallyForEditText(editPassword, ButtonColor, ButtonTextColor);
    }

    private class AsyncSubmitCopyStatus extends AsyncTask<String, String, String> {

        String SubmitCopyStatusResult = "";
        String sessionId = "";

        @Override
        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(StatusActivity.this);
        }


        @Override
        protected String doInBackground(String... strings) {
            try {

                DateTimeSubmitted = getDateAndTime(System.currentTimeMillis());

                String strUserName = SessionData.getInstance().getUsername();
                String strPassword = SessionData.getInstance().getPassword();


                sessionId = WebServiceConsumer.getInstance()
                        .signOn(WinCopyConstants.SOAP_ADDRESS, strUserName,
                                strPassword);

                SubmitCopyStatusResult = WebServiceConsumer.getInstance().SubmitCopyStatus(workOrderObject, Lineitem, StatusDate, StatusTime, Report, ServerCode, DateTimeSubmitted, sessionId);
                Loggers.addLog("Submit copy status : workOrderObject :" + workOrderObject+" ");
                Loggers.addLog("Submit copy status : Lineitem :"  +  Lineitem+" ");
                Loggers.addLog("Submit copy status : StatusDate :" + StatusDate+"");
                Loggers.addLog("Submit copy status : StatusTime :" +  StatusTime+"");
                Loggers.addLog("Submit copy status : Report :" +  Report+"");
                Loggers.addLog("Submit copy status : ServerCode :" +  ServerCode+"");
                Loggers.addLog("Submit copy status : DateTimeSubmitted :" +  DateTimeSubmitted+"");
                Loggers.addLog("Submit copy status :  sessionId :" +  sessionId+"");

            } catch (Exception e) {
                Loggers.addLog("AsyncSubmitCopyStatus Exception "+e.getMessage());
                e.printStackTrace();
            }

            return SubmitCopyStatusResult;

        }

        @Override
        protected void onPostExecute(String s) {
            ProgressBar.dismiss();
            if (SubmitCopyStatusResult != null) {


                if (!SubmitCopyStatusResult.contains("Exception: ")) {
                    SessionData.getInstance().getScanned_Workorder().clear();
                    SessionData.getInstance().getScanned_WorkorderList().clear();
                    SessionData.getInstance().setScan_dialog_handler("N"); // NORMAL SCAN
                    SubmitCopyStatusResult = "Submitted successfully";
                }

                CustomAlertSingle customAlertSingle = new CustomAlertSingle(StatusActivity.this, "Message", SubmitCopyStatusResult, false);

                if (SubmitCopyStatusResult.contains("Exception:")) {

                    customAlertSingle
                            .getWindow()
                            .getDecorView()
                            .animate()
                            .translationX(16f)
                            .setInterpolator(new CycleInterpolator(7f));

//                    workOrderObjectList.clear();
//                    workOrderObjectList.addAll(SessionData.getInstance().getScanned_WorkorderList());

                } else {

                    customAlertSingle.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            SessionData.getInstance().getScanned_WorkorderList().clear();
                            Intent intent = new Intent(StatusActivity.this, ScanActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                customAlertSingle.setCancelable(false);
                customAlertSingle.show();


//                Toast.makeText(getApplicationContext(), "submitted successfully", Toast.LENGTH_SHORT).show();
//                Intent scanActivityIntent = new Intent(StatusActivity.this, ScanActivity.class);
//                startActivity(scanActivityIntent);
//                finish();
            } else {
                Loggers.addLog("workorder not submitted");
                Toast.makeText(getApplicationContext(), "workorder not submitted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AsyncSubmitCopyStatusMultiple extends AsyncTask<String, String, String> {

        String SubmitCopyStatusResult;
        String sessionId = "";

        @Override
        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(StatusActivity.this);
        }


        @Override
        protected String doInBackground(String... strings) {
            try {

                DateTimeSubmitted = getDateAndTime(System.currentTimeMillis());
                String strUserName = SessionData.getInstance().getUsername();
                String strPassword = SessionData.getInstance().getPassword();


                sessionId = WebServiceConsumer.getInstance()
                        .signOn(WinCopyConstants.SOAP_ADDRESS, strUserName,
                                strPassword);

                SubmitCopyStatusResult = WebServiceConsumer.getInstance().SubmitCopyStatus(workOrderObject1, Lineitem, StatusDate, StatusTime, Report, ServerCode, DateTimeSubmitted, sessionId);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return SubmitCopyStatusResult;

        }

        @Override
        protected void onPostExecute(String s) {
            ProgressBar.dismiss();

            if (SubmitCopyStatusResult != null) {
                workOrderObjectList.remove(0);
                if (workOrderObjectList.size() > 0) {
                    workOrderObject1 = workOrderObjectList.get(0);
                    new AsyncSubmitCopyStatusMultiple().execute();
                } else {

                    if (!SubmitCopyStatusResult.contains("Exception:")) {
                        SessionData.getInstance().getScanned_Workorder().clear();
                        SessionData.getInstance().getScanned_WorkorderList().clear();
                        SessionData.getInstance().setScan_dialog_handler("N"); // NORMAL SCAN
                        SubmitCopyStatusResult = "Submitted successfully";
                    }
                    CustomAlertSingle customAlertSingle = new CustomAlertSingle(StatusActivity.this, "Message", SubmitCopyStatusResult, false);

                    if (SubmitCopyStatusResult.contains("Exception:")) {

                        customAlertSingle
                                .getWindow()
                                .getDecorView()
                                .animate()
                                .translationX(16f)
                                .setInterpolator(new CycleInterpolator(7f));

                        workOrderObjectList.clear();
                        workOrderObjectList.addAll(SessionData.getInstance().getScanned_WorkorderList());


                    } else {

                        customAlertSingle.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                SessionData.getInstance().getScanned_WorkorderList().clear();
                                Intent intent = new Intent(StatusActivity.this, ScanActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    customAlertSingle.setCancelable(false);
                    customAlertSingle.show();
                }

            } else {
                Loggers.addLog("Workorder not submitted");
                Toast.makeText(getApplicationContext(), "Workorder not submitted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String getDateAndTime(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", cal).toString();
        return date;
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd", cal).toString();
        return date;
    }

    private String getTime(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("HH:mm:ss", cal).toString();
        return date;
    }

    @Override
    protected void onResume() {
        super.onResume();


//        SessionData.getInstance().setValidate_record_Deligence_Scanned_result(1);


    }
}