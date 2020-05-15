package com.tristar.ronsinmobile.object;

public class WorkOrderObject {

    private String WorkOrder ="";
    private String FacilityLineItem ="";
    private String Lineitem ="";
    private String StatusDate ="";
    private String StatusTime ="";
    private String Report ="";
    private String ServerCode ="";
    private String DateTimeSubmitted ="";
    private String result ="";


    private String PseudoFacility ="";

    public String getWorkOrder() {
        return WorkOrder;
    }

    public void setWorkOrder(String workOrder) {
        WorkOrder = workOrder;
    }

    public String getFacilityLineItem() {
        return FacilityLineItem;
    }

    public void setFacilityLineItem(String facilityLineItem) {
        FacilityLineItem = facilityLineItem;
    }

    public String getLineitem() {
        return Lineitem;
    }

    public void setLineitem(String lineitem) {
        Lineitem = lineitem;
    }

    public String getStatusDate() {
        return StatusDate;
    }

    public void setStatusDate(String statusDate) {
        StatusDate = statusDate;
    }

    public String getStatusTime() {
        return StatusTime;
    }

    public void setStatusTime(String statusTime) {
        StatusTime = statusTime;
    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }

    public String getServerCode() {
        return ServerCode;
    }

    public void setServerCode(String serverCode) {
        ServerCode = serverCode;
    }

    public String getDateTimeSubmitted() {
        return DateTimeSubmitted;
    }

    public void setDateTimeSubmitted(String dateTimeSubmitted) {
        DateTimeSubmitted = dateTimeSubmitted;
    }

    public String getPseudoFacility() {
        return PseudoFacility;
    }

    public void setPseudoFacility(String pseudoFacility) {
        PseudoFacility = pseudoFacility;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
