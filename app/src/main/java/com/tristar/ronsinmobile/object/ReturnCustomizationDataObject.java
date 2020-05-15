package com.tristar.ronsinmobile.object;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;

import com.tristar.ronsinmobile.Webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

public class ReturnCustomizationDataObject extends SoapUtils {

    public static String TAG_HeaderBackgroundColor = "HeaderBackgroundColor";
    public static String TAG_HeaderTextColor = "HeaderTextColor";
    public static String TAG_ButtonColor = "ButtonColor";
    public static String TAG_ButtonTextColor = "ButtonTextColor";
    public static String TAG_TextColor = "TextColor";
    public static String TAG_BackgroundImage = "BackgroundImage";

    private String HeaderBackgroundColor;
    private String HeaderTextColor;
    private String ButtonColor;
    private String ButtonTextColor;
    private String TextColor;
    private String BackgroundImage;


    public String getHeaderBackgroundColor() {
        return HeaderBackgroundColor;
    }

    public void setHeaderBackgroundColor(String headerBackgroundColor) {
        HeaderBackgroundColor = headerBackgroundColor;
    }

    public String getHeaderTextColor() {
        return HeaderTextColor;
    }

    public void setHeaderTextColor(String headerTextColor) {
        HeaderTextColor = headerTextColor;
    }

    public String getButtonColor() {
        return ButtonColor;
    }

    public void setButtonColor(String buttonColor) {
        ButtonColor = buttonColor;
    }

    public String getButtonTextColor() {
        return ButtonTextColor;
    }

    public void setButtonTextColor(String buttonTextColor) {
        ButtonTextColor = buttonTextColor;
    }

    public String getTextColor() {
        return TextColor;
    }

    public void setTextColor(String textColor) {
        TextColor = textColor;
    }

    public String getBackgroundImage() {
        return BackgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        BackgroundImage = backgroundImage;
    }

    public static ReturnCustomizationDataObject parseObjectOld(SoapObject soapObject) {
        ReturnCustomizationDataObject object = new ReturnCustomizationDataObject();

        if (getProperty(soapObject, TAG_HeaderBackgroundColor).length() == 0) {
            object.setHeaderBackgroundColor("#007AE0");
        } else {
            object.setHeaderBackgroundColor("#" + getProperty(soapObject, TAG_HeaderBackgroundColor));
        }

        if (getProperty(soapObject, TAG_HeaderTextColor).length() == 0) {
            object.setHeaderTextColor("#ffffff");
        } else {
            object.setHeaderTextColor("#" + getProperty(soapObject, TAG_HeaderTextColor));
        }

        if (getProperty(soapObject, TAG_ButtonColor).length() == 0) {
            object.setButtonColor("#007AE0");
        } else {
            object.setButtonColor("#" + getProperty(soapObject, TAG_ButtonColor));
        }

        if (getProperty(soapObject, TAG_ButtonTextColor).length() == 0) {
            object.setButtonTextColor("#ffffff");
        } else {
            object.setButtonTextColor("#" + getProperty(soapObject, TAG_ButtonTextColor));
        }

        if (getProperty(soapObject, TAG_TextColor).length() == 0) {
            object.setTextColor("#000000");
        } else {
            object.setTextColor("#" + getProperty(soapObject, TAG_TextColor));
        }

        if (getProperty(soapObject, TAG_BackgroundImage).length() == 0) {
            object.setBackgroundImage("#ffffff");
        } else {
            object.setBackgroundImage("#" + getProperty(soapObject, TAG_BackgroundImage));
        }

//        object = checkValidColor(object);

        return object;
    }

    public static ReturnCustomizationDataObject parseObject(SoapObject soapObject) {

        ReturnCustomizationDataObject object = new ReturnCustomizationDataObject();


        try {
            object.setHeaderBackgroundColor("#" + getProperty(soapObject, TAG_HeaderBackgroundColor));
            int color = Color.parseColor(object.getHeaderBackgroundColor());
        } catch (Exception e) {
            object.setHeaderBackgroundColor("#007AE0");
            e.printStackTrace();
        }

        try {
            object.setHeaderTextColor("#" + getProperty(soapObject, TAG_HeaderTextColor));
            int color = Color.parseColor(object.getHeaderTextColor());
        } catch (Exception e) {
            object.setHeaderTextColor("#ffffff");
            e.printStackTrace();
        }

        try {
            object.setButtonColor("#" + getProperty(soapObject, TAG_ButtonColor));
            int color = Color.parseColor(object.getButtonColor());
        } catch (Exception e) {
            object.setButtonColor("#007AE0");
            e.printStackTrace();
        }
        try {
            object.setButtonTextColor("#" + getProperty(soapObject, TAG_ButtonTextColor));
            int color = Color.parseColor(object.getButtonTextColor());
        } catch (Exception e) {
            object.setButtonTextColor("#ffffff");
            e.printStackTrace();
        }
        try {
            object.setTextColor("#" + getProperty(soapObject, TAG_TextColor));
            int color = Color.parseColor(object.getTextColor());
        } catch (Exception e) {
            object.setTextColor("#000000");
            e.printStackTrace();
        }
        try {
            object.setBackgroundImage("" + getProperty(soapObject, TAG_BackgroundImage));

            byte[] decodedString = Base64.decode(object.getBackgroundImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            object.setBackgroundImage("#ffffff");
            e.printStackTrace();
        }



        return object;
    }

}