package com.tristar.ronsinmobile.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static DataBaseHelper instance;
    private static String DB_PATH = "";
    private static String DB_NAME = "Winson.sqlite";
    private SQLiteDatabase database;
    private final Context context;

    public static DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context);
            instance.databaseConnection(instance);
            instance.getReadableDatabase();
        }
        return instance;

    }
    public static DataBaseHelper getInstance() {
        return instance;
    }
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 4);
        DB_PATH = context.getFilesDir().getParent() + "/databases/";
        this.context = context;

    }

    public void databaseConnection(DataBaseHelper eb) {
        try {
            eb.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            eb.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
    }
    private boolean isDBExists(){
        return new File(DB_PATH + DB_NAME).exists();
    }
    public void createDataBase() throws IOException {
        boolean mDataBaseExist = isDBExists();
        if (!mDataBaseExist) {
            getReadableDatabase();
            close();
            try {
                copyDataBase();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                throw ioe;

            }
        }
    }
    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        return database != null;
    }
    private void copyDataBase() throws IOException {
        InputStream mInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
