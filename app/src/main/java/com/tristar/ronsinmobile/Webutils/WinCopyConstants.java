package com.tristar.ronsinmobile.Webutils;

public class WinCopyConstants {


//    public static String SOAP_BASE_ADDRESS = "";

//    public static String SOAP_ADDRESS = SOAP_BASE_ADDRESS

//            + "";

//    public static final String NAME_SPACE = "";

    public static String SOAP_ADDRESS = "";
    public static String NAME_SPACE = "http://tempuri.org/";


    public static final String METHOD_GET_RETURN_CUSTOMIZATION_DATA = "ReturnCustomizationData";

    public static final String METHOD_GET_CHECK_COPY_ORDER = "CheckCopyOrder";

    public static final String METHOD_GET_RETURN_COPY_STATUS = "ReturnCopyStatus";

    public static final String METHOD_GET_SUBMIT_COPY_STATUS = "SubmitCopyStatus";



    public static final String SOAP_ACTION_GET_RETURN_CUSTOMIZATION_DATA= NAME_SPACE
            + METHOD_GET_RETURN_CUSTOMIZATION_DATA;

    public static final String SOAP_ACTION_GET_CHECK_COPY_ORDER= NAME_SPACE
            + METHOD_GET_CHECK_COPY_ORDER;


    public static final String SOAP_ACTION_GET_RETURN_COPY_STATUS= NAME_SPACE
            + METHOD_GET_RETURN_COPY_STATUS;

    public static final String SOAP_ACTION_GET_SUBMIT_COPY_STATUS = NAME_SPACE
            + METHOD_GET_SUBMIT_COPY_STATUS;



    public static String METHOD_CLOUD_TWO = "CloudTwo";
    public static String SOAP_METHOD_CLOUD_TWO = NAME_SPACE + METHOD_CLOUD_TWO;

    public static String METHOD_SIGNON = "Signon";
    public static String SOAP_METHOD_SIGNON = NAME_SPACE + METHOD_SIGNON;

    public static String METHOD_SIGNOFF = "Signoff";
    public static String SOAP_METHOD_SIGNOFF = NAME_SPACE + METHOD_SIGNOFF;

    public static String METHOD_RETURN_CUSTOMIZATION_DATA = "ReturnCustomizationData";
    public static String SOAP_METHOD_RETURN_CUSTOMIZATION_DATA = NAME_SPACE + METHOD_RETURN_CUSTOMIZATION_DATA;

    public static String METHOD_RETURN_SUBMIT_COPY_STATUS = "SubmitCopyStatus";
    public static String SOAP_METHOD_RETURN_SUBMIT_COPY_STATUS = NAME_SPACE + METHOD_RETURN_SUBMIT_COPY_STATUS;

    public static String METHOD_RETURN_COPY_STATUS = "ReturnCopyStatus";
    public static String SOAP_METHOD_RETURN_COPY_STATUS = NAME_SPACE + METHOD_RETURN_COPY_STATUS;

    public static String METHOD_RETURN_CHECK_COPY_ORDER = "CheckCopyOrder";
    public static String SOAP_METHOD_RETURN_CHECK_COPY_ORDER = NAME_SPACE + METHOD_RETURN_CHECK_COPY_ORDER;
}
