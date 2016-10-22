package com.anyonavinfo.bluetoothphone.bpservice.entity;

/**
 * Created by Drive on 2016/9/8.
 */
public class PhoneCall {
    private String bdaddr;
    private String callName;
    private String callNumber;
    private String callTime;
    private String callPlace;
    private int callType;//1 receivedcall,2 missedcall,3 dialedcall

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public PhoneCall(){}

    public String getCallPlace() {
        return callPlace;
    }

    public void setCallPlace(String callPlace) {
        this.callPlace = callPlace;
    }

    public PhoneCall(String bdaddr, String callName, String callNumber, int callType, String callPlace) {
        this.bdaddr = bdaddr;
        this.callName = callName;
        this.callNumber = callNumber;
        this.callType = callType;
        this.callPlace=callPlace;

    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }



    public String getCallTime() {
        return callTime;
    }

    public String getBdaddr() {
        return bdaddr;
    }

    public void setBdaddr(String bdaddr) {
        this.bdaddr = bdaddr;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }
}
