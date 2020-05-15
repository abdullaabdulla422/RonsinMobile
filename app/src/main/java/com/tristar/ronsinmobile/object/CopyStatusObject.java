package com.tristar.ronsinmobile.object;

import com.tristar.ronsinmobile.Webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

public class CopyStatusObject extends SoapUtils {

    public static String TAG_CODE = "Code";
    public static String TAG_TITLE = "Title";
    public static String TAG_PHRASE = "Phrase";

    private int Code;
    private String Title="";
    private String Phrase="";


    public static CopyStatusObject parseObject(SoapObject soapObject) {
        CopyStatusObject object = new CopyStatusObject();
        object.setTitle(getProperty(soapObject, TAG_TITLE));
        object.setCode(Integer.parseInt(getProperty(soapObject, TAG_CODE)));
        object.setPhrase(getProperty(soapObject, TAG_PHRASE));

        return object;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPhrase() {
        return Phrase;
    }

    public void setPhrase(String phrase) {
        Phrase = phrase;
    }
}