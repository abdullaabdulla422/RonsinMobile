package com.tristar.ronsinmobile.utils;

import com.tristar.ronsinmobile.object.CopyStatusObject;
import com.tristar.ronsinmobile.object.WorkOrderObject;

import java.util.ArrayList;

public class SessionData {
    private static SessionData instance;
    private String selectedserver;
    private String companyname;
    private String userlocalname;
    private String passworddname;
    private String companynewname;
    private int synchandler = 0;
    private boolean Validate_record_Deligence_CheckBox;
    private ArrayList<String> scanned_Workorder = new ArrayList<>();
    private ArrayList<WorkOrderObject> scanned_WorkorderList = new ArrayList<>();
    private WorkOrderObject LastWorkorderObject;
    private String scan_dialog_handler = "";
    private String scan_type = "";

    private int Validate_record_Deligence_Scanned_result = 0;

    private boolean scanable = true;

    private String oldUsername = "";
    private String oldPassword;
    private String username;
    private String password;
    private String sessionId;
    private ArrayList<CopyStatusObject> copyStatusObjects=new ArrayList<>();

    private String singleRawResult = "";
    private String multipleRawResult = "";

    private String facilityLineItem = "";
    private int logger = 1;


    private boolean reScan=false;
    private WorkOrderObject workOrderObject;


    public String getSelectedserver() {
        return selectedserver;
    }

    public void setSelectedserver(String selectedserver) {
        this.selectedserver = selectedserver;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getUserlocalname() {
        return userlocalname;
    }

    public void setUserlocalname(String userlocalname) {
        this.userlocalname = userlocalname;
    }

    public String getPassworddname() {
        return passworddname;
    }

    public void setPassworddname(String passworddname) {
        this.passworddname = passworddname;
    }

    public String getCompanynewname() {
        return companynewname;
    }

    public void setCompanynewname(String companynewname) {
        this.companynewname = companynewname;
    }

    public int getSynchandler() {
        return synchandler;
    }

    public void setSynchandler(int synchandler) {
        this.synchandler = synchandler;
    }

    public static SessionData getInstance() {
        if (instance == null) {
            instance = new SessionData();
        }
        return instance;

    }

    public void clearSessionData() {
        instance = new SessionData();
    }

    public boolean isValidate_record_Deligence_CheckBox() {
        return Validate_record_Deligence_CheckBox;
    }

    public void setValidate_record_Deligence_CheckBox(boolean validate_record_Deligence_CheckBox) {
        Validate_record_Deligence_CheckBox = validate_record_Deligence_CheckBox;
    }

    public int getValidate_record_Deligence_Scanned_result() {
        return Validate_record_Deligence_Scanned_result;
    }

    public void setValidate_record_Deligence_Scanned_result(int validate_record_Deligence_Scanned_result) {
        Validate_record_Deligence_Scanned_result = validate_record_Deligence_Scanned_result;
    }


    public ArrayList<String> getScanned_Workorder() {
        if (scanned_Workorder == null) {
            scanned_Workorder = new ArrayList<>();
        }
        return scanned_Workorder;
    }

    public void setScanned_Workorder(ArrayList<String> scanned_workorder) {
        this.scanned_Workorder = scanned_workorder;
    }

    public String getScan_dialog_handler() {
        return scan_dialog_handler;
    }

    public void setScan_dialog_handler(String scan_dialog_handler) {
        this.scan_dialog_handler = scan_dialog_handler;
    }

    public String getScan_type() {
        return scan_type;
    }

    public void setScan_type(String scan_type) {
        this.scan_type = scan_type;
    }

    public boolean isScanable() {
        return scanable;
    }

    public void setScanable(boolean scanable) {
        this.scanable = scanable;
    }

    public void setInstance(SessionData instance) {
        SessionData.instance = instance;
    }

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    public ArrayList<CopyStatusObject> getCopyStatusObjects() {
        return copyStatusObjects;
    }

    public void setCopyStatusObjects(ArrayList<CopyStatusObject> copyStatusObjects) {
        this.copyStatusObjects = copyStatusObjects;
    }

    public String getSingleRawResult() {
        return singleRawResult;
    }

    public void setSingleRawResult(String singleRawResult) {
        this.singleRawResult = singleRawResult;
    }

    public String getMultipleRawResult() {
        return multipleRawResult;
    }

    public void setMultipleRawResult(String multipleRawResult) {
        this.multipleRawResult = multipleRawResult;
    }

    public String getFacilityLineItem() {
        return facilityLineItem;
    }

    public void setFacilityLineItem(String facilityLineItem) {
        this.facilityLineItem = facilityLineItem;
    }

    public ArrayList<WorkOrderObject> getScanned_WorkorderList() {
        return scanned_WorkorderList;
    }

    public void setScanned_WorkorderList(ArrayList<WorkOrderObject> scanned_WorkorderList) {
        this.scanned_WorkorderList = scanned_WorkorderList;
    }

    public WorkOrderObject getLastWorkorderObject() {
        return LastWorkorderObject;
    }

    public void setLastWorkorderObject(WorkOrderObject lastWorkorderObject) {
        LastWorkorderObject = lastWorkorderObject;
    }

    public boolean isReScan() {
        return reScan;
    }

    public void setReScan(boolean reScan) {
        this.reScan = reScan;
    }

    public WorkOrderObject getWorkOrderObject() {
        return workOrderObject;
    }

    public void setWorkOrderObject(WorkOrderObject workOrderObject) {
        this.workOrderObject = workOrderObject;
    }

    public int getLogger() {
        return logger;
    }

    public void setLogger(int logger) {
        this.logger = logger;
    }
}
