package com.tristar.ronsinmobile.Webutils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.tristar.ronsinmobile.Loggers;
import com.tristar.ronsinmobile.object.CopyStatusObject;
import com.tristar.ronsinmobile.object.ReturnCustomizationDataObject;
import com.tristar.ronsinmobile.object.WorkOrderObject;
import com.tristar.ronsinmobile.utils.SessionData;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class WebServiceConsumer {
    private static WebServiceConsumer instance = null;
    private SoapObject requestSoap;


    private SoapSerializationEnvelope envelope;
    private int timeout = 120 * 1000;


    public static WebServiceConsumer getInstance() {
        if (instance == null) {
            instance = new WebServiceConsumer();
        }
        return instance;
    }

    private SoapSerializationEnvelope getEnvelope(SoapObject soapObject) {
        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope1.dotNet = true;
        envelope1.setOutputSoapObject(soapObject);
        return envelope1;
    }

    @SuppressLint("LongLogTag")
    public String getDynamicUrlFromWebserviceByPassingCompanyCode(
            String companycode) throws Exception {


        SoapObject requestSoap = new SoapObject(WinCopyConstants.NAME_SPACE,
                WinCopyConstants.METHOD_CLOUD_TWO);

        requestSoap.addProperty("theFirst", companycode);
        requestSoap.addProperty("theSecond", "");
        requestSoap.addProperty("theThird", "");

// Loggers.addLog("TriStar : Dynamic Url From Webservice By Passing Company Code Request : " + requestSoap );
        Log.d("Dynamic Url companycode requestSoap", "" + requestSoap);


        SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);

        try {
            String tempWebServiceUrl = "https://tristarsoftware.net/pic/TheDongle.asmx";
            HttpTransportSE httpTransport = new HttpTransportSE(tempWebServiceUrl);

            httpTransport
                    .call(WinCopyConstants.SOAP_METHOD_CLOUD_TWO, envelope);

            String response = envelope.getResponse().toString().trim();

            Log.d("Dynamic Url companycode Responce", "" + response);

            return response;

        } catch (Exception e) {

            Log.d("Dynamic Url Responce", "" + e);
            Loggers.addLog("Wincopy :Dynamic Url Exception: " + e.toString());
            throw e;
        }
    }

    public String signOn(String soapAddress, String username, String password)
            throws Exception {

        SoapObject requestSoap = new SoapObject(WinCopyConstants.NAME_SPACE,
                WinCopyConstants.METHOD_SIGNON);

        requestSoap.addProperty("partOne", username);
        requestSoap.addProperty("partTwo", password);

        SoapSerializationEnvelope envelope = getEnvelope(requestSoap);


        try {
            HttpTransportSE httpTransport = new HttpTransportSE(soapAddress);

            httpTransport.call(WinCopyConstants.SOAP_METHOD_SIGNON, envelope);

            String response = envelope.getResponse().toString();


            return response;

        } catch (Exception e) {

            Log.d("Sign Response Exception", "" + e);
            Loggers.addLog("Wincopy :Sign Exception: " + e.toString());
            throw e;
        }
    }

    public String signOff(String sessionID) throws Exception {

        SoapObject requestSoap = new SoapObject(WinCopyConstants.NAME_SPACE,
                WinCopyConstants.METHOD_SIGNOFF);

        requestSoap.addProperty("connectionString", sessionID);
        SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
        HttpTransportSE httpTransport = new HttpTransportSE(
                WinCopyConstants.SOAP_ADDRESS);

        try {
            httpTransport.call(WinCopyConstants.SOAP_METHOD_SIGNOFF, envelope);

            String signoffResult = envelope.getResponse().toString();
            Log.d("Signoff", "" + signoffResult);

// long elapsedTime = System.currentTimeMillis() - startTime;
// System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
// long millis = elapsedTime; // obtained from StopWatch
// long minutes = (millis / 1000) / 60;
// int seconds = (int) ((millis / 1000) % 60);
// System.out.println("Sync_ReturnSignoff :" + seconds + " seconds.");

            return signoffResult;


        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public ArrayList<CopyStatusObject> ReturnCopyStatus(
            String sessionID) throws Exception {

        SoapObject requestSoap = new SoapObject(WinCopyConstants.NAME_SPACE,
                WinCopyConstants.METHOD_GET_RETURN_COPY_STATUS);

        requestSoap.addProperty("sessionID", sessionID);

//        Log.d("SubmitCopyStatus requestSoap", "" + requestSoap.toString());

        Log.d("ReturnCopyStatus:request", requestSoap.toString());
        Loggers.addLog("Wincopy :ReturnCopyStatus:request: " + requestSoap.toString());


        SoapSerializationEnvelope envelope = getEnvelope(requestSoap);

        HttpTransportSE httpTransport = new HttpTransportSE(
                WinCopyConstants.SOAP_ADDRESS);
        try {
            httpTransport.call(WinCopyConstants.SOAP_ACTION_GET_RETURN_COPY_STATUS,
                    envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            Log.d("ReturnCopyStatus:response", "" + response.toString());
            Loggers.addLog("Wincopy :ReturnCopyStatus:response: " + response.toString());

            int numProps = response.getPropertyCount();


            ArrayList<CopyStatusObject> object = new ArrayList<CopyStatusObject>();
            for (int i = 0; i < numProps; i++) {
                CopyStatusObject addressForServer = CopyStatusObject
                        .parseObject((SoapObject) response.getProperty(i));
                object.add(addressForServer);
            }

// long elapsedTime = System.currentTimeMillis() - startTime;
// System.out.println("Method_execution time: " + elapsedTime + " milliseconds.");
// long millis = elapsedTime; // obtained from StopWatch
// long minutes = (millis / 1000) / 60;
// int seconds = (int) ((millis / 1000) % 60);
// System.out.println("Sync_ReturnCourtOpenAddresses :" + seconds + " seconds.");

            return object;

        } catch (Exception e) {
            Loggers.addLog("Wincopy :ReturnCopyStatus:Exception: " + e.toString());
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public ReturnCustomizationDataObject ReturnCustomizationData(String sessionID) throws Exception {

        SoapObject requestSoap = new SoapObject(WinCopyConstants.NAME_SPACE,
                WinCopyConstants.METHOD_RETURN_CUSTOMIZATION_DATA);

        requestSoap.addProperty("sessionID", sessionID);

        SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
        Log.d("ReturnCustomizationData requestSoap", "" + requestSoap.toString());
        Loggers.addLog("Wincopy :ReturnCustomizationData requestSoap: " + requestSoap.toString());


        HttpTransportSE httpTransport = new HttpTransportSE(
                WinCopyConstants.SOAP_ADDRESS);
        try {
            httpTransport.call(
                    WinCopyConstants.SOAP_METHOD_RETURN_CUSTOMIZATION_DATA,
                    envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            Log.d("ReturnCustomizationData response", "" + response.toString());
//            Loggers.addLog("Wincopy :ReturnCustomizationData response: " + response.toString());

            ReturnCustomizationDataObject object = ReturnCustomizationDataObject
                    .parseObject(response);

            return object;

        } catch (Exception e) {
            Loggers.addLog("Wincopy :ReturnCustomizationData Exception: " + e.toString());


            throw e;
        }
    }


    @SuppressLint("LongLogTag")
    public String CheckCopyOrderResult(String WorkOrder, String PseudoFacility) throws Exception {

        SoapObject requestSoap = new SoapObject(WinCopyConstants.NAME_SPACE,
                WinCopyConstants.METHOD_RETURN_CHECK_COPY_ORDER);

        requestSoap.addProperty("WorkOrder", WorkOrder);
        requestSoap.addProperty("PseudoFacility", PseudoFacility);

        SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
        Log.d("CheckCopyOrderResult requestSoap", "" + requestSoap.toString());
        Loggers.addLog("Wincopy :CheckCopyOrderResult requestSoap: " + requestSoap.toString());


        HttpTransportSE httpTransport = new HttpTransportSE(
                WinCopyConstants.SOAP_ADDRESS);
        try {
            httpTransport.call(
                    WinCopyConstants.SOAP_METHOD_RETURN_CHECK_COPY_ORDER,
                    envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Log.d("CheckCopyOrderResult response", "" + response.toString());
            Loggers.addLog("Wincopy :CheckCopyOrderResult response: " + response.toString());

            if (Integer.parseInt(response.toString()) != 0) {
                SessionData.getInstance().setFacilityLineItem(response.toString());
            }
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Loggers.addLog("Wincopy :CheckCopyOrderResult Exception: " + e.toString());

            return e.getMessage();
        }
    }

    @SuppressLint("LongLogTag")
    public String SubmitCopyStatus(WorkOrderObject workOrderObject, int Lineitem, String StatusDate, String StatusTime, String Report,
                                   String ServerCode, String DateTimeSubmitted, String sessionID) throws Exception {

        SoapObject requestSoap = new SoapObject(WinCopyConstants.NAME_SPACE,
                WinCopyConstants.METHOD_RETURN_SUBMIT_COPY_STATUS);

        requestSoap.addProperty("sessionID", sessionID);
        SoapObject Status = new SoapObject();
        Status.addProperty("WorkOrder", workOrderObject.getWorkOrder());
        Status.addProperty("FacilityLineItem", workOrderObject.getFacilityLineItem());
        Status.addProperty("Lineitem", Lineitem);
        Status.addProperty("StatusDate", StatusDate);
        Status.addProperty("StatusTime", StatusTime);
        Status.addProperty("Report", Report);
        Status.addProperty("ServerCode", ServerCode);
        Status.addProperty("DateTimeSubmitted", DateTimeSubmitted);
        requestSoap.addProperty("status", Status);
        SoapSerializationEnvelope envelope = getEnvelope(requestSoap);
        Log.d("SubmitCopyStatusObject requestSoap", "" + requestSoap.toString());
        Loggers.addLog("Wincopy :SubmitCopyStatusObject requestSoap: " + requestSoap.toString());

        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);

        HttpTransportSE httpTransport = new HttpTransportSE(
                WinCopyConstants.SOAP_ADDRESS);
        try {
            httpTransport.call(
                    WinCopyConstants.SOAP_METHOD_RETURN_SUBMIT_COPY_STATUS,
                    envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();


            Log.d("SubmitCopyStatusObject response", "" + response.toString());
            Loggers.addLog("Wincopy :SubmitCopyStatusObject response: " + response.toString());



            return response.toString();

        } catch (Exception e) {
            Loggers.addLog("Wincopy :SubmitCopyStatusObject Exception: " + e.toString());

            return "Exception:"+e.getMessage();

//            throw e;
        }
    }

}