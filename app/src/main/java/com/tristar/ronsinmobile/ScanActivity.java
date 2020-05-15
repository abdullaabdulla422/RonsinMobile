package com.tristar.ronsinmobile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tristar.ronsinmobile.Adapter.MyRecyclerViewAdapter;
import com.tristar.ronsinmobile.Webutils.WebServiceConsumer;
import com.tristar.ronsinmobile.Webutils.WinCopyConstants;
import com.tristar.ronsinmobile.object.CopyStatusObject;
import com.tristar.ronsinmobile.object.WorkOrderObject;
import com.tristar.ronsinmobile.utils.BaseScannerActivity;
import com.tristar.ronsinmobile.utils.ProgressBar;
import com.tristar.ronsinmobile.utils.SessionData;
import com.tristar.ronsinmobile.utils.SwipeControllerActions;
import com.tristar.ronsinmobile.utils.SwipeControllerDelete;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

import me.dm7.barcodescanner.core.CameraPreview;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import top.defaults.drawabletoolbox.DrawableBuilder;


public class ScanActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler, View.OnClickListener {
    LinearLayout lnrscan;

    String JobValues;
    //    EditText Scanned_val;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //    ArrayList<ProcessAddressForServer> processOrderListArray;
    String Message;
    ViewGroup contentFrame;
    private ZBarScannerView mScannerView;
    private CameraPreview mPreview;
    private static Dialog dialog;
    private static Dialog dialogLogout;
    ArrayList<CopyStatusObject> copyStatusObjects = new ArrayList<>();

    TextView scan, next, headertitle;
    public int selectOption = 0;

    ArrayList<String> array_multiple_data = new ArrayList<String>();
    private boolean add = true;
    TextToSpeech textToSpeech;
    private TextView txtLogout, txtHeader;
    private ImageView imgLogout;
    private RelativeLayout lnrHeader/*, lnrBtscan*/;
    private LinearLayout lnrMain;
    private RelativeLayout rlFrame, rlWorkorderList;
    Button btnDone;
    SwipeControllerDelete timeswipeController = null;
    RecyclerView recyclerView;
    RelativeLayout rlSend, rlHheader;
    RelativeLayout workorderheader;


    WorkorderRecyclerAdapter workorderRecyclerAdapter;
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    private String HeaderBackgroundColor, HeaderTextColor, ButtonColor, ButtonTextColor, TextColor, BackgroundImage;
    public static final String COLOR_PREFERENCE = "color_pref";

    WorkOrderObject workOrderObject = new WorkOrderObject();
    String strWorkOrder, strFacilityCode;
    String strFacilityLineItem = null;

    private ArrayList<WorkOrderObject> scanned_WorkorderListTemp = new ArrayList<>();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan);
        Loggers.addLog("ScanActivity created");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            String[] perms = {"android.permission.CAMERA"};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
        }
        setupToolbar();
        if (!isCameraAvailable()) {
            // Cancel request if there is no rear-facing camera.
//            cancelRequest();
            return;
        }
//        BaseFileIncluder.PROCESS_DETAILSNAIGATION = BaseFileIncluder.SCANNER;

        contentFrame = (ViewGroup) findViewById(R.id.camera_preview);
        scan = (TextView) findViewById(R.id.scan);
        next = (TextView) findViewById(R.id.next);
        lnrscan = (LinearLayout) findViewById(R.id.lnr_scan);
        imgLogout = (ImageView) findViewById(R.id.img_logout);
        txtLogout = (TextView) findViewById(R.id.txt_logout);

//        lnrHeader = (RelativeLayout) findViewById(R.id.lnr_header);
        lnrMain = (LinearLayout) findViewById(R.id.lnr_main);
        rlFrame = (RelativeLayout) findViewById(R.id.rl_frame);
        rlWorkorderList = (RelativeLayout) findViewById(R.id.rl_workorderList);

        btnDone = (Button) findViewById(R.id.btn_done);


        recyclerView = findViewById(R.id.workorder_list);

//        lnrBtscan = (LinearLayout) findViewById(R.id.lnr_btscan);

        scan.setOnClickListener(this);
        next.setOnClickListener(this);
        lnrscan.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
        txtLogout.setOnClickListener(this);
//        lnrHeader.setOnClickListener(this);

        btnDone.setOnClickListener(this);

        next.setVisibility(View.GONE);

        mScannerView = new ZBarScannerView(this);

        initialize();

        getColorPreference();


        if (SessionData.getInstance().getScan_type().equalsIgnoreCase("M")) {


            lnrMain.removeView(rlFrame);
            lnrMain.removeView(rlWorkorderList);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, .5f);
            lnrMain.addView(rlFrame, params);
            lnrMain.addView(rlWorkorderList);


            recyclerView.setLayoutManager(new LinearLayoutManager(ScanActivity.this));

            workorderRecyclerAdapter = new WorkorderRecyclerAdapter(ScanActivity.this, SessionData.getInstance().getScanned_WorkorderList());
            recyclerView.setAdapter(workorderRecyclerAdapter);
            workorderRecyclerAdapter.notifyDataSetChanged();
            swipeHelper();

//            lnrMain.addView(rlWorkorderList);

//
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
////                contentFrame.getWindow().setLayout(width * 5 / 7, height * 2);
//
//            } else {
//
//
////                contentFrame.getWindow().setLayout(width, height);
//            }
        } else {


            lnrMain.removeView(rlFrame);
            lnrMain.removeView(rlWorkorderList);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
            lnrMain.addView(rlFrame, params);


        }


        contentFrame.addView(mScannerView);


        if (!SessionData.getInstance().isScanable()) {

//            lnrMain.removeView(rlFrame);
//            lnrMain.removeView(rlWorkorderList);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
//            lnrMain.addView(rlFrame, params);
//            lnrMain.addView(rlWorkorderList);
//
//
//            recyclerView.setLayoutManager(new LinearLayoutManager(ScanActivity.this));
//
//            workorderRecyclerAdapter = new WorkorderRecyclerAdapter(ScanActivity.this, SessionData.getInstance().getScanned_Workorder());
//            recyclerView.setAdapter(workorderRecyclerAdapter);
//            workorderRecyclerAdapter.notifyDataSetChanged();
//            swipeHelper();

//            lnrBtscan.setBackgroundColor(Color.GREEN);
//            showNextDialog();
//            next.setVisibility(View.VISIBLE);

        } else {
//            lnrBtscan.setBackgroundColor(Color.RED);

        }


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Message = bundle.getString("Message");
            if (Message.length() != 0) {
                Toast.makeText(this, Message, Toast.LENGTH_LONG).show();
            } else {
            }
        }

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        if (SessionData.getInstance().getScan_dialog_handler().contains("N")) { // nORMAL SCAN

            if (!SessionData.getInstance().isReScan()) {


                dialog = new Dialog(ScanActivity.this);

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.message);

                TextView txtMessage = (TextView) dialog.findViewById(R.id.txt_dialog);
                TextView txtTitle = (TextView) dialog.findViewById(R.id.title);
                TextView single = (TextView) dialog.findViewById(R.id.dialog_Ok);
                TextView multiple = (TextView) dialog.findViewById(R.id.dialog_cancel);
                RelativeLayout rl_header = dialog.findViewById(R.id.rl_header);
                LinearLayout lnr_root = dialog.findViewById(R.id.lnr_root);
                View view_ok = (View) findViewById(R.id.view_ok);
                View view_cancel = (View) findViewById(R.id.view_cancel);

                ImageView closeImg = dialog.findViewById(R.id.close_img);

                txtMessage.setVisibility(View.GONE);

                txtTitle.setText("Select Scan Type");
                single.setText("single");
                multiple.setText("multiple");


                setColorDynamicallyForNormalTextView(single, TextColor);
                setColorDynamicallyForNormalTextView(multiple, TextColor);

                setColorDynamicallyForLayout(lnr_root, HeaderBackgroundColor);
                setColorDynamicallyForHeaderLayout(rl_header, HeaderBackgroundColor);
                setColorDynamicallyForHeaderLayout(view_ok, HeaderBackgroundColor);
                setColorDynamicallyForHeaderLayout(view_cancel, HeaderBackgroundColor);

//        setColorDynamicallyForButton(btnAddProfile, ButtonColor, ButtonTextColor);

                setColorDynamicallyForHeaderTextView2(txtTitle, HeaderBackgroundColor, HeaderTextColor);

                single.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        lnrscan.setVisibility(View.GONE);


                        SessionData.getInstance().setScan_type("S");
                        SessionData.getInstance().setScan_dialog_handler("N");


                        lnrMain.removeView(rlFrame);
                        lnrMain.removeView(rlWorkorderList);
//                    contentFrame.removeView(mScannerView);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
                        lnrMain.addView(rlFrame, params);

//                    contentFrame.addView(mScannerView);


                        mScannerView.setResultHandler(ScanActivity.this);
                        mScannerView.startCamera();
//                    lnrMain.addView(rlWorkorderList);

//                    Intent i = new Intent(ScanActivity.this, ScanActivity.class);
//                    startActivity(i);

                        dialog.dismiss();
                    }
                });
                multiple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        SessionData.getInstance().setScan_type("M");
                        SessionData.getInstance().setScan_dialog_handler("D");

//                    lnrMain.removeView(rlFrame);
//                    lnrMain.removeView(rlWorkorderList);
//
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 0.5f);
//                    lnrMain.addView(rlFrame, params);
//                    lnrMain.addView(rlWorkorderList);

                        lnrMain.removeView(rlFrame);
                        lnrMain.removeView(rlWorkorderList);
                        contentFrame.removeView(mScannerView);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, .5f);
                        lnrMain.addView(rlFrame, params);
                        lnrMain.addView(rlWorkorderList);

                        contentFrame.addView(mScannerView);

                        mScannerView.setResultHandler(ScanActivity.this);
                        mScannerView.startCamera();

                        recyclerView.setLayoutManager(new LinearLayoutManager(ScanActivity.this));

                        workorderRecyclerAdapter = new WorkorderRecyclerAdapter(ScanActivity.this, SessionData.getInstance().getScanned_WorkorderList());
                        recyclerView.setAdapter(workorderRecyclerAdapter);
                        workorderRecyclerAdapter.notifyDataSetChanged();
                        swipeHelper();

//                    Intent i = new Intent(ScanActivity.this, ScanActivity.class);
//                    startActivity(i);

                        dialog.dismiss();

                    }
                });
                closeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customAlertNew("Are you sure you want to logout?");
//                    AlertDialogLogoutCustom.showAlertDialog(ScanActivity.this, false, "Are you sure you want to logout?", "Logout");
                        dialog.dismiss();

                    }
                });


                dialog.show();
            } else {

                lnrscan.setVisibility(View.GONE);


                SessionData.getInstance().setScan_type("S");
                SessionData.getInstance().setScan_dialog_handler("N");


                lnrMain.removeView(rlFrame);
                lnrMain.removeView(rlWorkorderList);
//                    contentFrame.removeView(mScannerView);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
                lnrMain.addView(rlFrame, params);

//                    contentFrame.addView(mScannerView);


                mScannerView.setResultHandler(ScanActivity.this);
                mScannerView.startCamera();
//                    lnrMain.addView(rlWorkorderList);

//                    Intent i = new Intent(ScanActivity.this, ScanActivity.class);
//                    startActivity(i);

                dialog.dismiss();
            }


        }
    }

    public void initialize() {

        workorderheader = (RelativeLayout) findViewById(R.id.workorder_header);
        rlSend = (RelativeLayout) findViewById(R.id.rl_send);
        rlHheader = (RelativeLayout) findViewById(R.id.rl_header);
        lnrHeader = (RelativeLayout) findViewById(R.id.lnr_header);
        headertitle = (TextView) findViewById(R.id.header_title);
        txtLogout = (TextView) findViewById(R.id.txt_logout);
        btnDone = (Button) findViewById(R.id.btn_done);
        rlWorkorderList = (RelativeLayout) findViewById(R.id.rl_workorderList);
        txtHeader = (TextView) findViewById(R.id.txt_header);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

//    public void cancelRequest() {
//        Intent dataIntent = new Intent();
//        dataIntent.putExtra(ERROR_INFO, "Camera unavailable");
//        setResult(Activity.RESULT_CANCELED, dataIntent);
//        finish();
//    }

    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    @Override
    public void onBackPressed() {

//        super.onBackPressed();
        customAlertNew("Are you sure you want to logout?");

//        AlertDialogLogout.showAlertDialog(ScanActivity.this, false, "Are you sure you want to logout?", "Logout");

    }

    @Override
    public void handleResult(final Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getContents() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
                if (SessionData.getInstance().isValidate_record_Deligence_CheckBox()
                        || SessionData.getInstance().getValidate_record_Deligence_Scanned_result() == 0) {

                    rawResult.setContents(removeDot(rawResult.getContents())); //TODO: To remove dot from result

                    Log.d("Scan Type", "" + rawResult.getBarcodeFormat().getName());
                    Log.d("Scan Value", "" + rawResult.getContents());
                    String Scanned_result = rawResult.getContents();


                    if (rawResult.getContents() != null && rawResult.getContents().length() != 0) {
                        try {

                            String workOrder = SessionData.getInstance().getSingleRawResult().split("-")[0];
                            String pseudoFacility = SessionData.getInstance().getSingleRawResult().split("-")[1];
                            strWorkOrder = workOrder;
                            strFacilityCode = pseudoFacility;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (SessionData.getInstance().getScan_type().contains("S")) {
                            startSingleScan(rawResult);
//                            playAudio();
//                            SessionData.getInstance().setValidate_record_Deligence_Scanned_result(1);
//                            Intent intent = new Intent(ScanActivity.this, StatusActivity.class);
//                            intent.putExtra("value", rawResult.getContents());
//                            contentFrame.removeAllViews();
//                            rawResult.setContents(null);
//                            startActivity(intent);
                        } else if (SessionData.getInstance().getScan_type().contains("M")) {


                            startMultipleScan(Scanned_result);


                        }
                    } else {
                        return;
                    }

                }


            }
        }, 1000);
    }


    public class AsyncLoginTask extends AsyncTask<Void, Void, Void> {

        String sessionId = "", errorString = null;

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(ScanActivity.this
            );

        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {


                String strUserName = SessionData.getInstance().getUsername();
                String strPassword = SessionData.getInstance().getPassword();
                sessionId = WebServiceConsumer.getInstance()
                        .signOn(WinCopyConstants.SOAP_ADDRESS, strUserName,
                                strPassword);

                copyStatusObjects = WebServiceConsumer.getInstance().ReturnCopyStatus(sessionId);
                // returnCustomizationDataObject = WebServiceConsumer.getInstance().ReturnCustomizationData(sessionId);

            } catch (Exception e) {
                if (e instanceof ConnectException
                        || e instanceof MalformedURLException) {
                    errorString = "No network Connection. Enable Wi-fi or turn on Mobile Data!";
                } else if (e instanceof StringIndexOutOfBoundsException) {
                    errorString = "Bad input: Either Company Code or credential is wrong.";
                } else if (e instanceof UnknownHostException) {
                    errorString = "The Company URL is not found! Please contact your Administrator.";
                } else if (e instanceof SocketTimeoutException) {
                    errorString = "The Company URL is not found! Please contact your Administrator.";
                } else {
                    errorString = "Unable to access Profile: Please contact your Administrator.";
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressBar.dismiss();

            if (copyStatusObjects != null) {

                SessionData.getInstance().setCopyStatusObjects(copyStatusObjects);
                Intent intent = new Intent(ScanActivity.this, StatusActivity.class);
                startActivity(intent);

            } else {

                copyStatusObjects = new ArrayList<>();

                SessionData.getInstance().setCopyStatusObjects(copyStatusObjects);


                Intent intent = new Intent(ScanActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        }
    }

    public class AsyncLoginTask2 extends AsyncTask<Void, Void, Void> {

        String sessionId = "", errorString = null;


        protected void onPreExecute() {


            ProgressBar.showCommonProgressDialog(ScanActivity.this
            );

        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String strUserName = SessionData.getInstance().getUsername();
                String strPassword = SessionData.getInstance().getPassword();
                sessionId = WebServiceConsumer.getInstance()
                        .signOn(WinCopyConstants.SOAP_ADDRESS, strUserName,
                                strPassword);


                try {
                    String workOrder = SessionData.getInstance().getSingleRawResult().split("-")[0];
                    String pseudoFacility = SessionData.getInstance().getSingleRawResult().split("-")[1];
                    workOrderObject.setWorkOrder(workOrder);
                    workOrderObject.setPseudoFacility(pseudoFacility);
                    strFacilityLineItem = null;

                    strFacilityLineItem = WebServiceConsumer.getInstance().CheckCopyOrderResult(workOrder, pseudoFacility);


                } catch (Exception e) {
                    Loggers.addLog("Single AsyncLoginTask2"+e.getMessage());
                    e.printStackTrace();
                    if (e instanceof IllegalArgumentException) {
                        errorString = "invalid Workorder";
                    }
                }

                try {
                    if (strFacilityLineItem != null && Integer.parseInt(strFacilityLineItem) != 0 && !strFacilityLineItem.equalsIgnoreCase("-1")) {

                        playAudio();
                        copyStatusObjects = WebServiceConsumer.getInstance().ReturnCopyStatus(sessionId);
                    }else{
                        Loggers.addLog("Rejected from Web Service :"+ SessionData.getInstance().getSingleRawResult());
                    }
                } catch (Exception e) {
                    Loggers.addLog("Single AsyncLoginTask2 Exception :"+e.getMessage());

                    e.printStackTrace();
                }
                // returnCustomizationDataObject = WebServiceConsumer.getInstance().ReturnCustomizationData(sessionId);

            } catch (Exception e) {
                if (e instanceof ConnectException
                        || e instanceof MalformedURLException) {
                    errorString = "No network Connection. Enable Wi-fi or turn on Mobile Data!";
                } else if (e instanceof StringIndexOutOfBoundsException) {
                    errorString = "Bad input: Either Company Code or credential is wrong.";
                } else if (e instanceof UnknownHostException) {
                    errorString = "The Company URL is not found! Please contact your Administrator.";
                } else if (e instanceof SocketTimeoutException) {
                    errorString = "The Company URL is not found! Please contact your Administrator.";
                } else {
                    errorString = "Unable to access Profile: Please contact your Administrator.";
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressBar.dismiss();

            if (strFacilityLineItem != null && Integer.parseInt(strFacilityLineItem) != 0 && !strFacilityLineItem.equalsIgnoreCase("-1")) {

                SessionData.getInstance().setValidate_record_Deligence_Scanned_result(1);
                SessionData.getInstance().setReScan(false);


                if (copyStatusObjects != null) {


                    workOrderObject.setFacilityLineItem(strFacilityLineItem);
                    SessionData.getInstance().setWorkOrderObject(workOrderObject);
                    SessionData.getInstance().setCopyStatusObjects(copyStatusObjects);
                    Loggers.addLog("copystatusObject root1: "+copyStatusObjects);
                    Loggers.addLog("strFacilityLineItem root1: "+strFacilityLineItem);
                    Loggers.addLog("root1 Intent to StatusActivity");
                    Intent intent = new Intent(ScanActivity.this, StatusActivity.class);
                    startActivity(intent);
                } else {
                    copyStatusObjects = new ArrayList<>();


                    workOrderObject.setFacilityLineItem(strFacilityLineItem);
                    SessionData.getInstance().setWorkOrderObject(workOrderObject);
                    SessionData.getInstance().setCopyStatusObjects(copyStatusObjects);
                    Loggers.addLog("copystatusObject root2 : "+copyStatusObjects);
                    Loggers.addLog("strFacilityLineItem root2 : "+strFacilityLineItem);
                    Loggers.addLog("root2 Intent to StatusActivity");

                    Intent intent = new Intent(ScanActivity.this, StatusActivity.class);
                    startActivity(intent);
                }

            } else {
                SessionData.getInstance().setValidate_record_Deligence_Scanned_result(0);
                SessionData.getInstance().setReScan(true);
                Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
                startActivity(intent);
                Loggers.addLog("AsyncLoginTask2 (Intent to ScanActivity) scanning workorder is not valid");
                Loggers.addLog("strFacilityLineItem :"+strFacilityLineItem);
                Toast.makeText(getApplicationContext(), "Please Scan a valid Workorder", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public class AsyncCheckCopyOrderResult extends AsyncTask<Void, Void, Void> {

        String errorString = null;

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(ScanActivity.this
            );

        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {

                errorString = null;

                try {
                    String workOrder = SessionData.getInstance().getMultipleRawResult().split("-")[0];
                    String pseudoFacility = SessionData.getInstance().getMultipleRawResult().split("-")[1];
                    workOrderObject.setWorkOrder(workOrder);
                    workOrderObject.setPseudoFacility(pseudoFacility);
                    workOrderObject.setResult(SessionData.getInstance().getMultipleRawResult());

                    strFacilityLineItem = WebServiceConsumer.getInstance().CheckCopyOrderResult(workOrder, pseudoFacility);


                    //TODO : add to Multiple workOrder arrayList (session)

                    if (!strFacilityLineItem.equalsIgnoreCase("0") && !strFacilityLineItem.equalsIgnoreCase("-1")) {
                        workOrderObject.setFacilityLineItem(strFacilityLineItem);
                        SessionData.getInstance().getScanned_Workorder().add(SessionData.getInstance().getMultipleRawResult());
                        SessionData.getInstance().getScanned_WorkorderList().add(workOrderObject);
                        Loggers.addLog("added to list  : " + SessionData.getInstance().getMultipleRawResult());
                        Loggers.addLog("added to list size : " + SessionData.getInstance().getScanned_WorkorderList().size());

                        playAudio();

                    } else {
                        Loggers.addLog("Workorder rejected from service :" + SessionData.getInstance().getMultipleRawResult());

                        errorString = "Please Scan a valid Workorder";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Loggers.addLog("Multiple AsyncCheckCopyOrderResult Exception :" + e.getMessage());

                    if (e instanceof IllegalArgumentException || e instanceof ArrayIndexOutOfBoundsException) {
                        errorString = "Please Scan a valid Workorder";

                    }
                }


                // returnCustomizationDataObject = WebServiceConsumer.getInstance().ReturnCustomizationData(sessionId);

            } catch (Exception e) {

                if (e instanceof IllegalArgumentException) {
                    errorString = "Please Scan a valid Workorder";
                    Loggers.addLog("Multiple AsyncCheckCopyOrderResult Exception :" + e.getMessage());

                } else {
                    e.printStackTrace();
                    errorString = e.getMessage();
                    Loggers.addLog("Multiple AsyncCheckCopyOrderResult Exception :" + e.getMessage());


                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressBar.dismiss();

            if (errorString != null) {
                Toast.makeText(getApplicationContext(), "" + errorString, Toast.LENGTH_SHORT).show();
            }
            Loggers.addLog("AsyncCheckCopyOrderResult Intent to ScanActivity");
            Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
            startActivity(intent);

        }
    }


    public void playAudio() {

        try {
            textToSpeech.speak("Item scanned", TextToSpeech.QUEUE_FLUSH, null);
        } catch (Exception e) {
            Loggers.addLog("playAudio Exception :" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        if (v == scan) {
            if (!SessionData.getInstance().isScanable()) {
                SessionData.getInstance().setScanable(true);
//                lnrBtscan.setBackgroundColor(Color.RED);
            } else {
//                SessionData.getInstance().setScanable(false);
            }
        }

        if (v == btnDone) {

            if (SessionData.getInstance().getScanned_WorkorderList().size() > 0) {

                new AsyncLoginTask().execute();
//                Intent intent = new Intent(ScanActivity.this, StatusActivity.class);
//                startActivity(intent);
            }

        }
//        if (v == lnrHeader) {
//
//            AlertDialogLogout.showAlertDialog(ScanActivity.this, false, "Are you sure you want to logout?", "Logout");
//        }
        if (v == imgLogout) {
            customAlertNew("Are you sure you want to logout?");

//            AlertDialogLogout.showAlertDialog(ScanActivity.this, false, "Are you sure you want to logout?", "Logout");

        }
        if (v == txtLogout) {
            customAlertNew("Are you sure you want to logout?");

//            AlertDialogLogout.showAlertDialog(ScanActivity.this, false, "Are you sure you want to logout?", "Logout");

        }

        if (v == next) {
            showNextDialog();
        }
    }

    public void showNextDialog() {

        if (SessionData.getInstance().getScanned_Workorder().size() > 0) {

            dialog = new Dialog(ScanActivity.this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_multiple_scan);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        int width = (int) (getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
            int width = (int) (ScanActivity.this.getResources().getDisplayMetrics().widthPixels * 0.90);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                dialog.getWindow().setLayout(width * 5 / 7, height * 2);

            } else {
                dialog.getWindow().setLayout(width, height);
            }
//                }else {
//                    dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
//                }

            recyclerView = dialog.findViewById(R.id.workorder_list);

            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) recyclerView.getLayoutParams();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                lp.height = height * 2 * 5 / 7;
            } else {
                lp.height = height * 5 / 7;
            }
            recyclerView.setLayoutParams(lp);

            TextView Header_title = dialog.findViewById(R.id.header_title);
            ImageView close_img = dialog.findViewById(R.id.close_img);
            Button Done = (Button) dialog.findViewById(R.id.btn_done);
            Button scan = (Button) dialog.findViewById(R.id.btn_scan);

            close_img.setVisibility(View.GONE);

            Done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncLoginTask().execute();
//                    Intent intent = new Intent(ScanActivity.this, StatusActivity.class);
//                    startActivity(intent);
                }
            });
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionData.getInstance().setScanable(true);


                    Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
                    startActivity(intent);
                }
            });
            close_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

            workorderRecyclerAdapter = new WorkorderRecyclerAdapter(dialog.getContext(), SessionData.getInstance().getScanned_WorkorderList());
            recyclerView.setAdapter(workorderRecyclerAdapter);
            workorderRecyclerAdapter.notifyDataSetChanged();
            swipeHelper();

            dialog.show();
        }
    }


    public void startMultipleScan(String scannedResult) {

        Loggers.addLog("Multiple Scan : Rawresult :" + scannedResult);

//        if (SessionData.getInstance().isScanable()) {
//                            SessionData.getInstance().setScan_dialog_handler("D");
//        array_multiple_data.add(scannedResult);
        scanned_WorkorderListTemp.clear();
        scanned_WorkorderListTemp.addAll(SessionData.getInstance().getScanned_WorkorderList());

        for (int i = 0; i < SessionData.getInstance().getScanned_Workorder().size(); i++) {
            if (SessionData.getInstance().getScanned_Workorder().get(i).equals(scannedResult)) {
                add = false;

            }
        }
        for (int i = 0; i < scanned_WorkorderListTemp.size(); i++) {
            if ((scanned_WorkorderListTemp.get(i).getWorkOrder() + "-" + scanned_WorkorderListTemp.get(i).getPseudoFacility()).equals(scannedResult)) {
                add = false;
            }

        }

        if (add) {
            if (SessionData.getInstance().isScanable()) {
                SessionData.getInstance().setScanable(false);
                scan.setBackgroundColor(Color.GREEN);
            }

//            playAudio();
//            SessionData.getInstance().getScanned_Workorder().add(scannedResult);


//            SessionData.getInstance().getScanned_WorkorderList().add(workOrderObject);

            SessionData.getInstance().setMultipleRawResult(scannedResult);
            System.out.println("Multiple Size " + SessionData.getInstance().getScanned_Workorder().size());
            System.out.println("Multiple Size 1" + SessionData.getInstance().getScanned_WorkorderList().size());
            int m_size = SessionData.getInstance().getScanned_Workorder().size();
            for (int i = 0; i < m_size; i++) {
                System.out.println("data " + i + " " + SessionData.getInstance().getScanned_Workorder().get(i));
            }
            System.out.println("Multiple");

            new AsyncCheckCopyOrderResult().execute();
        } else {
            Toast.makeText(ScanActivity.this, "Work order already exist", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
            startActivity(intent);
        }

//        new AsyncLoginTask().execute();
//        Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
//        startActivity(intent);
//        }else {

//        }

    }

    public void startSingleScan(Result rawResult) {
//        playAudio();
//        contentFrame.removeAllViews();
        SessionData.getInstance().setSingleRawResult(rawResult.getContents());
        Loggers.addLog("Single Scan : Rawresult :" + rawResult.getContents());
        rawResult.setContents(null);


        new AsyncLoginTask2().execute();
//        Intent intent = new Intent(ScanActivity.this, StatusActivity.class);
//        intent.putExtra("value", rawResult.getContents());
//        startActivity(intent);

    }

    public void swipeHelper() {
        timeswipeController = new SwipeControllerDelete(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);


                {
                    super.onRightClicked(position);

//                    array_multiple_data.remove(position);
                    SessionData.getInstance().getScanned_Workorder().remove(position);
                    SessionData.getInstance().getScanned_WorkorderList().remove(position);
                    recyclerView.setAdapter(workorderRecyclerAdapter);
                    workorderRecyclerAdapter.notifyItemChanged(position);

                }

            }
        }, ScanActivity.this);


        ItemTouchHelper itemTouchhelper3 = new ItemTouchHelper(timeswipeController);
        itemTouchhelper3.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                timeswipeController.onDraw(c);
            }
        });

    }

    private class WorkorderRecyclerAdapter extends RecyclerView.Adapter<WorkorderRecyclerAdapter.Viewholder> {

        Context context;
        ArrayList<WorkOrderObject> arrayList;

        public WorkorderRecyclerAdapter(Context context, ArrayList<WorkOrderObject> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public WorkorderRecyclerAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.chilld_workorder, parent, false);
            return new Viewholder(v);

        }

        @Override
        public void onBindViewHolder(WorkorderRecyclerAdapter.Viewholder holder, int position) {

            if (holder != null) {

                holder.txtWorkorder.setText(arrayList.get(position).getResult());

            }

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class Viewholder extends RecyclerView.ViewHolder {

            TextView txtWorkorder;

            Viewholder(View itemView) {
                super(itemView);

                txtWorkorder = (TextView) itemView.findViewById(R.id.txt_wo);

            }
        }
    }


    public String removeDot(String scannedvalue) {

        if (scannedvalue.contains(".")) {
            try {
                String[] parts = scannedvalue.trim().split("\\."); // escape .
                String part1 = parts[0];
                String part2 = parts[1];

                if (part1.length() == 0) {
                    return part2;
                } else {
                    return part1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return scannedvalue;
            }
        } else {
            return scannedvalue;
        }

    }

    public void setBackgroundImageDynamically(View view, String image) {

        try {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            view.setBackground(new BitmapDrawable(ScanActivity.this.getResources(), decodedByte));

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
        setBackgroundImageDynamically(rlHheader, BackgroundImage);


        setColorDynamicallyForHeaderLayout(lnrHeader, HeaderBackgroundColor);
        setColorDynamicallyForHeaderLayout(workorderheader, HeaderBackgroundColor);

        setColorDynamicallyForHeaderTextView(txtHeader, HeaderTextColor);
        setColorDynamicallyForHeaderTextView(txtLogout, HeaderTextColor);
        setColorDynamicallyForHeaderTextView(headertitle, HeaderTextColor);

        setColorDynamicallyForButton(btnDone, ButtonColor, ButtonTextColor);
        //        setColorDynamicallyForNormalTextView(txtLogout, TextColor);

        //  setColorDynamicallyForButton(btnCompanyNewName, ButtonColor, ButtonTextColor);

        //setColorDynamicallyForEditText(edtCompanyCode, ButtonColor, ButtonTextColor);
        // setColorDynamicallyForEditText(editUserId, ButtonColor, ButtonTextColor);
        // setColorDynamicallyForEditText(editPassword, ButtonColor, ButtonTextColor);
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

    public void setColorDynamicallyForHeaderTextView2(TextView textView, String bgColor, String txtColor) {


        try {
            textView.setTextColor(Color.parseColor(txtColor));
            textView.setBackgroundColor(Color.parseColor(bgColor));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void customAlertNew(String message) { // nORMAL SCAN

        dialogLogout = new Dialog(ScanActivity.this);

        dialogLogout.setCancelable(false);
        dialogLogout.setCanceledOnTouchOutside(false);

        dialogLogout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogout.setContentView(R.layout.message);

        TextView txtMessage = (TextView) dialogLogout.findViewById(R.id.txt_dialog);
        TextView txtTitle = (TextView) dialogLogout.findViewById(R.id.title);
        TextView ok = (TextView) dialogLogout.findViewById(R.id.dialog_Ok);
        TextView cancel = (TextView) dialogLogout.findViewById(R.id.dialog_cancel);
        RelativeLayout rl_header = dialogLogout.findViewById(R.id.rl_header);
        LinearLayout lnr_root = dialogLogout.findViewById(R.id.lnr_root);
        View view_ok = (View) findViewById(R.id.view_ok);
        View view_cancel = (View) findViewById(R.id.view_cancel);

        ImageView closeImg = dialogLogout.findViewById(R.id.close_img);

        txtMessage.setVisibility(View.GONE);

        txtTitle.setText(message);
        ok.setText("OK");
        cancel.setText("Cancel");


        setColorDynamicallyForNormalTextView(ok, TextColor);
        setColorDynamicallyForNormalTextView(cancel, TextColor);

        setColorDynamicallyForLayout(lnr_root, HeaderBackgroundColor);
        setColorDynamicallyForHeaderLayout(rl_header, HeaderBackgroundColor);
        setColorDynamicallyForHeaderLayout(view_ok, HeaderBackgroundColor);
        setColorDynamicallyForHeaderLayout(view_cancel, HeaderBackgroundColor);

//        setColorDynamicallyForButton(btnAddProfile, ButtonColor, ButtonTextColor);

        setColorDynamicallyForHeaderTextView2(txtTitle, HeaderBackgroundColor, HeaderTextColor);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnrscan.setVisibility(View.GONE);

                Intent intent = new Intent(ScanActivity.this, LoginActivity.class);
                startActivity(intent);
                dialogLogout.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
                startActivity(intent);

                dialogLogout.dismiss();

            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScanActivity.this, ScanActivity.class);
                startActivity(intent);

                dialogLogout.dismiss();
            }
        });


        dialogLogout.show();
    }


}





