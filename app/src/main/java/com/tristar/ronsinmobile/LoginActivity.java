package com.tristar.ronsinmobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tristar.ronsinmobile.Webutils.WebServiceConsumer;
import com.tristar.ronsinmobile.Webutils.WinCopyConstants;
import com.tristar.ronsinmobile.object.ReturnCustomizationDataObject;
import com.tristar.ronsinmobile.utils.CustomAlertSingle;
import com.tristar.ronsinmobile.utils.Exiter;
import com.tristar.ronsinmobile.utils.ProgressBar;
import com.tristar.ronsinmobile.utils.SessionData;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class LoginActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 234;
    Button companyName, btnCompanyNewName;
    String storecompany, storeusername, storepass, storecompanyname,
            splitfname, slpitlname, splitlpass, splitcompanyname, splitcompany;
    String id, fname, companyname, username, password, selectedcompany;
    String strCompanyCode, strUserName, strPassword;
    Context context;
    EditText edtCompanyCode, editUserId, editPassword;
    Button btn_save;
    String shared_CompanyName, shared_Companycode, shared_userid, shared_password;
    //    Button sendMail;
    private String HeaderBackgroundColor, HeaderTextColor, ButtonColor, ButtonTextColor, TextColor, BackgroundImage;
    public static final String COLOR_PREFERENCE = "color_pref";


    //    EditText editUserId, editPassword;
//    EditText edtCompanyCode;
    RelativeLayout rlRoot, header;
    TextView headerName, txtAppVersion;

    String encryptedString, hexString = "";

    String clearPassword = "";
    String hexstring = "";

    ReturnCustomizationDataObject returnCustomizationDataObject;
    public static SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Loggers.addLog("LoginActivity created");
        SessionData.getInstance().clearSessionData();
        sharedPreferences = getSharedPreferences("TRISTAR", MODE_PRIVATE);

        companyName = findViewById(R.id.company_name);
//        sendMail= findViewById(R.id.btn_sendmail);

        if (!checkPermission()) {
            requestPermission();
        }

        storecompany = SessionData.getInstance().getCompanyname();
        storeusername = SessionData.getInstance().getUserlocalname();
        storepass = SessionData.getInstance().getPassworddname();
        storecompanyname = SessionData.getInstance().getCompanynewname();
        selectedcompany = SessionData.getInstance().getSelectedserver();

        SelectProfileActivity.loginPreferencess = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SelectProfileActivity.loginPrefsEditors = SelectProfileActivity.loginPreferencess.edit();

        shared_CompanyName = SelectProfileActivity.loginPreferencess.getString("Company Name", "");
        shared_Companycode = SelectProfileActivity.loginPreferencess.getString("Company Code", "");
        shared_userid = SelectProfileActivity.loginPreferencess.getString("user Id", "");
        shared_password = SelectProfileActivity.loginPreferencess.getString("password", "");
        if (selectedcompany != null) {
            String[] str = selectedcompany.split(",");
            splitfname = str[0];
            splitlpass = str[1];
            splitcompany = str[2];
            splitcompanyname = str[3];

            Log.d("splitname", "" + selectedcompany);
            Loggers.addLog("selected company" + selectedcompany);

        }
        context = this;


        initialize();


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_save) {


                    if (isLoginFormPassedValidation()) {

                        SessionData.getInstance().setSynchandler(1);


                        SelectProfileActivity.loginPrefsEditors.putString("Company Name", btnCompanyNewName.getText().toString());
                        SelectProfileActivity.loginPrefsEditors.putString("Company Code", edtCompanyCode.getText().toString());
                        SelectProfileActivity.loginPrefsEditors.putString("user Id", editUserId.getText().toString());
                        SelectProfileActivity.loginPrefsEditors.putString("password", editPassword.getText().toString());
                        SelectProfileActivity.loginPrefsEditors.apply();


                        SessionData.getInstance().setScan_dialog_handler("N");

                        new AsyncLoginTask().execute();

//                        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
//                        startActivity(intent);

                    }

                }
            }
        });

        companyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loggers.addLog("Intent to SelectProfileActivity");
                Intent intent = new Intent(LoginActivity.this, SelectProfileActivity.class);
                startActivity(intent);
            }
        });
//        sendMail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String filename="wincopymobile_log.txt";
//                File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
//                Uri path = Uri.fromFile(filelocation);
//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//// set the type to 'email'
//                emailIntent .setType("vnd.android.cursor.dir/email");
//
//// the attachment
//                emailIntent .putExtra(Intent.EXTRA_STREAM, path);
//// the mail subject
//                emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Winserve mobile debuglog attachmant :");
//                startActivity(Intent.createChooser(emailIntent , "Send email..."));
//            }
//        });
    }

    private void initialize() {


        btnCompanyNewName = (Button) findViewById(R.id.company_name);
        edtCompanyCode = (EditText) findViewById(R.id.edt_company_code);
        editUserId = (EditText) findViewById(R.id.edt_username);
        editPassword = (EditText) findViewById(R.id.edt_password);
        btn_save = (Button) findViewById(R.id.btn_login);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        header = (RelativeLayout) findViewById(R.id.rl_header);
        headerName = (TextView) findViewById(R.id.header_name);
        txtAppVersion = (TextView) findViewById(R.id.txt_app_version);

        getColorPreference();


        if (shared_Companycode != "" || shared_userid != "" || shared_password != "") {
            edtCompanyCode.setText(shared_Companycode);
            editUserId.setText(shared_userid);
            editPassword.setText(shared_password);
            btnCompanyNewName.setText(shared_CompanyName);
        }

    }

    private boolean isLoginFormPassedValidation() {
        String validationMessage = null;
        if ((strCompanyCode = edtCompanyCode.getText().toString().trim())
                .length() == 0) {
            validationMessage = "Please enter company code";
        } else if ((strUserName = editUserId.getText().toString().trim())
                .length() == 0) {
            validationMessage = "Please enter user ID";
        } else if ((strPassword = editPassword.getText().toString().trim())
                .length() == 0) {
            validationMessage = "Please enter password";
        } else if (shared_Companycode != "" || shared_userid != "" || shared_password != "") {

//            edtCompanyCode.setText(shared_Companycode);
//            editUserId.setText(shared_userid);
//            editPassword.setText(shared_password);
//            btnCompanyNewName.setText(shared_CompanyName);
//            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
//            startActivity(intent);
        }
        if (validationMessage != null) {
            CustomAlertSingle customAlertSingle = new CustomAlertSingle(LoginActivity.this, "Message", validationMessage, false);
            customAlertSingle.show();

            return false;
        } else {
            return true;
        }

    }

//    private boolean isLoginFromGettingValidation() {
//
//        if (shared_Companycode != "" || shared_userid != "" || shared_password != "") {
//            edtCompanyCode.setText(shared_Companycode);
//            edtUserId.setText(shared_userid);
//            edtPassword.setText(shared_password);
//            edtCompanyName.setText(shared_CompanyName);
//
//        }
//        return false;
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Intent relaunch = new Intent(this, Exiter.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK // CLEAR_TASK requires
// this
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK // finish everything
// else in the task
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); // hide

        startActivity(relaunch);
        finish();
    }


    public void setBackgroundImageDynamically(View view, String image) {

        try {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            view.setBackground(new BitmapDrawable(LoginActivity.this.getResources(), decodedByte));
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

    public void setColorDynamicallyForHeaderTextView(TextView textView, String color) {


        try {
            textView.setTextColor(Color.parseColor(color));
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
        SharedPreferences prefs = getSharedPreferences(COLOR_PREFERENCE, MODE_PRIVATE);

        HeaderBackgroundColor = prefs.getString("HeaderBackgroundColor", "#007AE0");
        HeaderTextColor = prefs.getString("HeaderTextColor", "#ffffff");
        ButtonColor = prefs.getString("ButtonColor", "#007AE0");
        ButtonTextColor = prefs.getString("ButtonTextColor", "#ffffff");
        TextColor = prefs.getString("TextColor", "#000000");
        BackgroundImage = prefs.getString("BackgroundImage", "#ffffff");


        applyColorToLayout();
    }

    public void setColorToPreference(ReturnCustomizationDataObject returnCustomizationDataObject) {
        SharedPreferences.Editor editor = getSharedPreferences(COLOR_PREFERENCE, MODE_PRIVATE).edit();

        editor.putString("HeaderBackgroundColor", returnCustomizationDataObject.getHeaderBackgroundColor());
        editor.putString("HeaderTextColor", returnCustomizationDataObject.getHeaderTextColor());
        editor.putString("ButtonColor", returnCustomizationDataObject.getButtonColor());
        editor.putString("ButtonTextColor", returnCustomizationDataObject.getButtonTextColor());
        editor.putString("TextColor", returnCustomizationDataObject.getTextColor());
        editor.putString("BackgroundImage", returnCustomizationDataObject.getBackgroundImage());
        editor.apply();

        getColorPreference();

    }

    public class AsyncLoginTask extends AsyncTask<Void, Void, Void> {

        String sessionId = "", errorString = null;

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(LoginActivity.this
            );

        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                clearPassword = "";
                WinCopyConstants.SOAP_ADDRESS = decryptTheResponseFromServer(getDynamicUrlFromWebservice())
                        + "samsung.asmx";
                Log.d("URL", "" + WinCopyConstants.SOAP_ADDRESS);

                sessionId = WebServiceConsumer.getInstance()
                        .signOn(WinCopyConstants.SOAP_ADDRESS, strUserName,
                                strPassword);

                SessionData.getInstance().setPassword(strPassword);
                SessionData.getInstance().setUsername(strUserName);
                returnCustomizationDataObject = WebServiceConsumer.getInstance().ReturnCustomizationData(sessionId);

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

            if (returnCustomizationDataObject != null) {
                setColorToPreference(returnCustomizationDataObject);
            }

            if (errorString == null) {

                if (sessionId == null
                        || sessionId.equals("rejected")
                        || sessionId.length() == 0
                        || sessionId
                        .equals("Signon: server code / server password is invalid")) {
                    Toast.makeText(context, "Login information incorrect.", Toast.LENGTH_SHORT).show();

                } else {
                    signOnProcess(sessionId);
                }


            } else {
                Toast.makeText(context, errorString + " ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String decryptTheResponseFromServer(String StringToDecrypt)
            throws Exception {

        String decryptedUrl = "";
        try {
            String seed = "The quick brown fox jumped over the lazy dogs back 1234567890";
            for (int a = 0; a < StringToDecrypt.length(); a += 2) {
                int Nowis = 0;
                switch (StringToDecrypt.charAt(a)) {
                    case '0':
                        Nowis = 16 * 0;
                        break;
                    case '1':
                        Nowis = 16 * 1;
                        break;
                    case '2':
                        Nowis = 16 * 2;
                        break;
                    case '3':
                        Nowis = 16 * 3;
                        break;
                    case '4':
                        Nowis = 16 * 4;
                        break;
                    case '5':
                        Nowis = 16 * 5;
                        break;
                    case '6':
                        Nowis = 16 * 6;
                        break;
                    case '7':
                        Nowis = 16 * 7;
                        break;
                    case '8':
                        Nowis = 16 * 8;
                        break;
                    case '9':
                        Nowis = 16 * 9;
                        break;
                    case 'a':
                        Nowis = 16 * 10;
                        break;
                    case 'b':
                        Nowis = 16 * 11;
                        break;
                    case 'c':
                        Nowis = 16 * 12;
                        break;
                    case 'd':
                        Nowis = 16 * 13;
                        break;
                    case 'e':
                        Nowis = 16 * 14;
                        break;
                    case 'f':
                        Nowis = 16 * 15;
                        break;
                    default:
                        Nowis = 0;
                        break;
                }
                switch (StringToDecrypt.charAt(a + 1)) {
                    case '0':
                        Nowis = Nowis + 0;
                        break;
                    case '1':
                        Nowis = Nowis + 1;
                        break;
                    case '2':
                        Nowis = Nowis + 2;
                        break;
                    case '3':
                        Nowis = Nowis + 3;
                        break;
                    case '4':
                        Nowis = Nowis + 4;
                        break;
                    case '5':
                        Nowis = Nowis + 5;
                        break;
                    case '6':
                        Nowis = Nowis + 6;
                        break;
                    case '7':
                        Nowis = Nowis + 7;
                        break;
                    case '8':
                        Nowis = Nowis + 8;
                        break;
                    case '9':
                        Nowis = Nowis + 9;
                        break;
                    case 'a':
                        Nowis = Nowis + 10;
                        break;
                    case 'b':
                        Nowis = Nowis + 11;
                        break;
                    case 'c':
                        Nowis = Nowis + 12;
                        break;
                    case 'd':
                        Nowis = Nowis + 13;
                        break;
                    case 'e':
                        Nowis = Nowis + 14;
                        break;
                    case 'f':
                        Nowis = Nowis + 15;
                        break;
                    default:
                        Nowis = 0;
                        break;
                }
                int singleCharacter = seed.charAt((int) Math.floor(a / 2));
                hexstring = Character
                        .toString((char) (Nowis - singleCharacter));
                clearPassword = clearPassword + hexstring;
            }
        } catch (Exception e) {
            throw e;
        }
        decryptedUrl = clearPassword;

        return decryptedUrl;
    }

    public String getDynamicUrlFromWebservice() throws Exception {
        String encryptedStringForWS = encryptString(strCompanyCode);
        Log.d("Company Code", "" + strCompanyCode);

        return WebServiceConsumer.getInstance()
                .getDynamicUrlFromWebserviceByPassingCompanyCode(
                        encryptedStringForWS);
    }

    public String encryptString(String strinToEncrypt) {
        String theString = "The quick brown fox jumped over the lazy dog's back 1234567890";
        String clearPassword;
        @SuppressWarnings("unused")
        int theSum = 0;
        String returnString = "";
        clearPassword = strinToEncrypt.replaceAll("\\s", "");
        if (clearPassword.length() >= theString.length()
                || clearPassword.length() > 95) {
            returnString = "";
            return returnString;
        }

        encryptedString = "";
        for (int a = 0; a < clearPassword.length(); a++) {
            int ascii = theString.charAt(a) + clearPassword.charAt(a);
            theSum += clearPassword.charAt(a);

            hexString = String.format("%x", ascii);
            encryptedString = encryptedString.concat(hexString);
        }
        returnString = encryptedString.toLowerCase();
        return returnString;
    }

    private void signOnProcess(String sessionId) {

        SharedPreferences.Editor editor = LoginActivity.sharedPreferences.edit();
        editor.putString("webServiceUrlD", WinCopyConstants.SOAP_ADDRESS);
        editor.commit();


        Log.d("Previous User ", "" + SessionData.getInstance().getOldUsername());
        Log.d("Current User ", "" + strUserName);
        Log.d("webServiceUrlD ", "" + WinCopyConstants.SOAP_ADDRESS);


        if (sessionId != null || sessionId.length() != 0) {
            Intent i = new Intent(new Intent(LoginActivity.this, ScanActivity.class));
            startActivity(i);
        }


    }

    public void applyColorToLayout() {

        if (!BackgroundImage.equalsIgnoreCase("#ffffff")) {
            setBackgroundImageDynamically(rlRoot, BackgroundImage);
        }
        setColorDynamicallyForNormalTextView(txtAppVersion, TextColor);

        setColorDynamicallyForHeaderLayout(header, HeaderBackgroundColor);
        setColorDynamicallyForHeaderTextView(headerName, HeaderTextColor);


        setColorDynamicallyForButton(btn_save, ButtonColor, ButtonTextColor);
        setColorDynamicallyForButton(btnCompanyNewName, ButtonColor, ButtonTextColor);
//        setColorDynamicallyForButton(sendMail, ButtonColor, ButtonTextColor);

        setColorDynamicallyForEditText(edtCompanyCode, HeaderBackgroundColor, "#000000");
        setColorDynamicallyForEditText(editUserId, HeaderBackgroundColor, "#000000");
        setColorDynamicallyForEditText(editPassword, HeaderBackgroundColor, "#000000");

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
// Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }
}

