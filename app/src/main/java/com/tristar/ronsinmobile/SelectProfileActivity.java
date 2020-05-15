package com.tristar.ronsinmobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tristar.ronsinmobile.db.DbHelper;
import com.tristar.ronsinmobile.utils.SessionData;
import com.tristar.ronsinmobile.utils.SharedPreference;

import java.io.File;
import java.util.ArrayList;

import top.defaults.drawabletoolbox.DrawableBuilder;

import static com.tristar.ronsinmobile.ScanActivity.COLOR_PREFERENCE;

public class SelectProfileActivity extends AppCompatActivity {

    private DbHelper mHelper;
    private SQLiteDatabase dataBase;
    Button btnAddProfile;
    Button btnDone;
    ListView userList;
    private ArrayList<String> userId = new ArrayList<String>();
    private ArrayList<String> user_fName = new ArrayList<String>();
    private ArrayList<String> user_lName = new ArrayList<String>();
    private ArrayList<String> user_pName = new ArrayList<String>();
    private ArrayList<String> user_pcompanyname = new ArrayList<String>();
    String server, company, password, companyname, selectedserver;
    public static SharedPreferences.Editor loginPrefsEditors;
    private SharedPreference sharedPreference;
    public static SharedPreferences loginPreferencess;
    String shared_CompanyName, shared_Companycode, shared_userid, shared_password;


    private String HeaderBackgroundColor, HeaderTextColor, ButtonColor, ButtonTextColor, TextColor, BackgroundImage;

    LinearLayout lnrRoot, lnrListTable;
    TextView txtHeader, txtCompanyName, txtCompanyCode;
    Button sendMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_profile);
        Loggers.addLog("SelectProfileActivity created");
        sendMail= findViewById(R.id.btn_sendmail);

        btnAddProfile = (Button) findViewById(R.id.txt_add_profile);
        btnAddProfile.setTransformationMethod(null);
        btnDone = (Button) findViewById(R.id.btn_done);
//        btnDone.setTransformationMethod(null);
        userList = (ListView) findViewById(R.id.listView);
        lnrRoot = (LinearLayout) findViewById(R.id.lnr_root);
        lnrListTable = (LinearLayout) findViewById(R.id.lnr_list_table);
        txtHeader = (TextView) findViewById(R.id.txt_header);
        txtCompanyName = (TextView) findViewById(R.id.txt_company_name);
        txtCompanyCode = (TextView) findViewById(R.id.txt_company_code);

        getColorPreference();

        mHelper = new DbHelper(this);
        sharedPreference = new SharedPreference();

        if (userList.equals(null)) {
            selectedserver = null;
        }
        loginPreferencess = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditors = loginPreferencess.edit();

        shared_CompanyName = loginPreferencess.getString("Company Name", "");
        shared_Companycode = loginPreferencess.getString("Company Code", "");
        shared_userid = loginPreferencess.getString("user Id", "");
        shared_password = loginPreferencess.getString("password", "");


        btnAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginPrefsEditors.putString("Company Name", companyname);
                loginPrefsEditors.putString("Company Code", server);
                loginPrefsEditors.putString("user Id", company);
                loginPrefsEditors.putString("password", password);
                loginPrefsEditors.apply();
                Intent intent = new Intent(SelectProfileActivity.this, AddProfileActivity.class);
                intent.putExtra("update", false);
                SessionData.getInstance().setSelectedserver(selectedserver);
                SessionData.getInstance().setCompanynewname(companyname);
                SessionData.getInstance().setCompanyname(server);
                SessionData.getInstance().setUserlocalname(company);
                SessionData.getInstance().setPassworddname(password);
                startActivity(intent);

            }
        });
        this.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPrefsEditors.putString("Company Name", companyname);
                loginPrefsEditors.putString("Company Code", server);
                loginPrefsEditors.putString("user Id", company);
                loginPrefsEditors.putString("password", password);
                loginPrefsEditors.apply();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                SessionData.getInstance().setSelectedserver(selectedserver);
                SessionData.getInstance().setCompanynewname(companyname);
                SessionData.getInstance().setCompanyname(server);
                SessionData.getInstance().setUserlocalname(company);
                SessionData.getInstance().setPassworddname(password);

                startActivity(i);

            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                userList.setSelection(arg2);
                companyname = user_pcompanyname.get(arg2);
                server = user_fName.get(arg2);
                company = user_lName.get(arg2);
                password = user_pName.get(arg2);
                selectedserver = user_fName.get(arg2) + ","
                        + user_lName.get(arg2) + "," + user_pName.get(arg2)
                        + "," + user_pcompanyname.get(arg2);

                Log.d("serve value", "" + selectedserver);
                Toast.makeText(getApplicationContext(),
                        user_pcompanyname.get(arg2) + " selected",
                        Toast.LENGTH_LONG).show();
                try {
                    for (int ctr = 0; ctr <= user_pcompanyname.size(); ctr++) {
                        if (arg2 == ctr) {

                            userList.getChildAt(ctr).setBackgroundResource(
                                    R.color.graycolor);
                        } else {
                            userList.getChildAt(ctr).setBackgroundColor(
                                    Color.WHITE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String filename = "wincopymobile_log.txt";
                    File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
                    Uri path = Uri.fromFile(filelocation);
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
                    emailIntent.setType("vnd.android.cursor.dir/email");

// the attachment
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Winserve mobile debuglog attachmant :");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }

        });

    }

    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

    private void displayData() {
        dataBase = mHelper.getWritableDatabase();
        Cursor mCursor = dataBase.rawQuery("SELECT * FROM "
                + DbHelper.TABLE_NAME, null);

        userId.clear();
        user_pcompanyname.clear();
        user_fName.clear();
        user_lName.clear();
        user_pName.clear();

        if (mCursor.moveToFirst()) {
            do {
                userId.add(mCursor.getString(mCursor
                        .getColumnIndex(DbHelper.KEY_ID)));
                user_pcompanyname.add(mCursor.getString(mCursor
                        .getColumnIndex(DbHelper.KEY_LCOMPANYNAME)));
                user_fName.add(mCursor.getString(mCursor
                        .getColumnIndex(DbHelper.KEY_FNAME)));
                user_lName.add(mCursor.getString(mCursor
                        .getColumnIndex(DbHelper.KEY_LNAME)));
                user_pName.add(mCursor.getString(mCursor
                        .getColumnIndex(DbHelper.KEY_LPASS)));

            } while (mCursor.moveToNext());

        }
        if (!(user_fName.isEmpty())) {
            selectedserver = SessionData.getInstance().getSelectedserver();
            Log.d("server", "" + selectedserver);

        }
        DisplayAdapter disadpt = new DisplayAdapter(SelectProfileActivity.this,
                userId, user_pcompanyname, user_fName, user_lName, user_pName);
        userList.setAdapter(disadpt);
        mCursor.close();
    }

    public class DisplayAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<String> id;
        private ArrayList<String> companyname;
        private ArrayList<String> firstName;
        private ArrayList<String> lastName;
        private ArrayList<String> password;

        private AlertDialog.Builder build;

        public DisplayAdapter(Context c, ArrayList<String> id,
                              ArrayList<String> lcompanyname, ArrayList<String> fname,
                              ArrayList<String> lname, ArrayList<String> fpass) {
            this.mContext = c;

            this.id = id;
            this.companyname = lcompanyname;
            this.firstName = fname;
            this.lastName = lname;
            this.password = fpass;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return id.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @SuppressLint("InflateParams")
        public View getView(final int pos, View child, ViewGroup parent) {
            Holder mHolder;
            LayoutInflater layoutInflater;
            if (child == null) {
                layoutInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                child = layoutInflater.inflate(R.layout.child_profile, null);
                mHolder = new Holder();
                mHolder.txt_fName = (TextView) child
                        .findViewById(R.id.txt_fName);
                mHolder.txt_lName = (TextView) child
                        .findViewById(R.id.txt_lName);
                final View finalChild = child;
                ImageView delete = (ImageView) child.findViewById(R.id.delete);
                ImageView edit = (ImageView) child.findViewById(R.id.edit);
                delete.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {


                        CustomAlertSingle customAlertSingle = new CustomAlertSingle(SelectProfileActivity.this,
                                "Delete " + user_pcompanyname.get(pos)
                                        + " "
                                        + user_fName.get(pos),
                                "Do you want to delete?",
                                true,
                                pos);
                        customAlertSingle.show();

                    }
                });
                if (((user_fName.get(pos)) + "," + (user_lName.get(pos)) + ","
                        + (user_pName.get(pos)) + "," + (user_pcompanyname
                        .get(pos))).equals(selectedserver)) {
                    finalChild.setBackgroundResource(R.color.graycolor);
                } else {
                    finalChild.setBackgroundColor(Color.WHITE);
                }
                edit.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(),
                                AddProfileActivity.class);
                        i.putExtra("lcompanyname", user_pcompanyname.get(pos));
                        i.putExtra("Fname", user_fName.get(pos));
                        i.putExtra("Lname", user_lName.get(pos));
                        i.putExtra("Fpass", user_pName.get(pos));
                        i.putExtra("ID", userId.get(pos));
                        i.putExtra("update", true);
                        startActivity(i);
                    }
                });

                child.setTag(mHolder);
            } else {
                mHolder = (Holder) child.getTag();
            }
            mHolder.txt_fName.setText(companyname.get(pos));
            mHolder.txt_lName.setText(firstName.get(pos));

            return child;
        }

        public class Holder {
            TextView txt_id;
            TextView txt_fName;
            TextView txt_lName;
        }

    }

    public class CustomAlertSingle extends Dialog implements
            android.view.View.OnClickListener {
        public boolean isConfirmDialog = false;
        public final Activity parentActivity;
        public Dialog d;
        public TextView yes, no;
        public final String message;
        private Intent intent;
        public final static int SYNC = 101;
        public String title = "";
        public ImageView imgClose;
        public int pos;
        int sync = -1;

        TextView txtTitle;
        public LinearLayout lnrRoot,lnr_bottom;
        public RelativeLayout rlHeader;
        public View view_ok;

        private String HeaderBackgroundColor, HeaderTextColor, ButtonColor, ButtonTextColor, TextColor, BackgroundImage;

        public CustomAlertSingle(Activity activity, String title, String message,
                                 boolean confirmDialog, int pos) {
            super(activity);
            this.parentActivity = activity;
            this.message = message;
            this.pos = pos;
            this.isConfirmDialog = confirmDialog;
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
            imgClose = (ImageView) findViewById(R.id.close_img);
            txtTitle = (TextView) findViewById(R.id.txt_Title);
            view_ok = (View) findViewById(R.id.view_ok);


            if (!isConfirmDialog) {
                yes.setVisibility(View.GONE);
//            findViewById(R.id.btn_devider).setVisibility(View.GONE);
                no.setText("OK");
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
            getColorPreference();
        }

        @SuppressLint("WrongConstant")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_no:
                    dismiss();
//				if (sync == SYNC && no.getText().toString().equals("OK")) {
//					SyncronizeClass.instance().syncronizeMainMethod(parentActivity);
//				}
//				else if (sync == SYNC && no.getText().toString().equals("Cancel")) {
//
//				}
//				else if (intent != null && !isConfirmDialog) {
//					parentActivity.finish();
//					parentActivity.startActivity(intent);
//				}
                    break;
                case R.id.btn_yes: {

                    Toast.makeText(
                            getApplicationContext(),
                            user_pcompanyname.get(pos)
                                    + " "
                                    + user_fName.get(pos)
                                    + " is deleted.", 3000)
                            .show();

                    dataBase.delete(
                            DbHelper.TABLE_NAME,
                            DbHelper.KEY_ID + "="
                                    + userId.get(pos), null);
                    if ((user_fName.get(pos))
                            .equals(selectedserver)) {
                        SessionData.getInstance()
                                .setSelectedserver(null);
                        selectedserver = null;
                        sharedPreference.save(SelectProfileActivity.this,
                                selectedserver);
                    }
                    displayData();
//                    dialog.cancel();
                    dismiss();

                }
                break;

                case R.id.close_img:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
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

    }

    public void setBackgroundImageDynamically(View view, String image) {

        try {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            view.setBackground(new BitmapDrawable(SelectProfileActivity.this.getResources(), decodedByte));
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

            setBackgroundImageDynamically(lnrRoot, BackgroundImage);
        }
//        setColorDynamicallyForNormalTextView(txtAppVersion, TextColor);

        setColorDynamicallyForHeaderLayout(lnrListTable, HeaderBackgroundColor);
        setColorDynamicallyForLayoutList(userList, HeaderBackgroundColor);

        setColorDynamicallyForButton(btnAddProfile, ButtonColor, ButtonTextColor);
        setColorDynamicallyForButton(sendMail, ButtonColor, ButtonTextColor);

        setColorDynamicallyForHeaderTextView(txtHeader, HeaderBackgroundColor, HeaderTextColor);
        setColorDynamicallyForHeaderTextView(txtCompanyName, HeaderBackgroundColor, HeaderTextColor);
        setColorDynamicallyForHeaderTextView(txtCompanyCode, HeaderBackgroundColor, HeaderTextColor);


        setColorDynamicallyForButton(btnDone, ButtonColor, ButtonTextColor);

//        setColorDynamicallyForEditText(edtCompanyCode, HeaderBackgroundColor, "#000000");
//        setColorDynamicallyForEditText(editUserId, HeaderBackgroundColor, "#000000");
//        setColorDynamicallyForEditText(editPassword, HeaderBackgroundColor, "#000000");


    }
}
