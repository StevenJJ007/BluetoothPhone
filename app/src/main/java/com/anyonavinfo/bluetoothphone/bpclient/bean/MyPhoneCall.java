package com.anyonavinfo.bluetoothphone.bpclient.bean;

import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneCall;

/**
 * Created by Drive on 2016/9/8.
 */
public class MyPhoneCall {
    private String bdaddr;//电话归属地
    private String callName;//电话姓名
    private String callNumber;//电话号码
    private String callTime;//通话时间
    private String callPlace;
    private int callType;//1 receivedcall,2 missedcall,3 dialedcall

    private boolean checked;//判断是否勾选的

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

    public MyPhoneCall(){}

    public MyPhoneCall(String bdaddr, String callName, String callNumber, int callType) {
        this.bdaddr = bdaddr;
        this.callName = callName;
        this.callNumber = callNumber;
        this.callType = callType;
    }

    public String getCallPlace() {
        return callPlace;
    }

    public void setCallPlace(String callPlace) {
        this.callPlace = callPlace;
    }

    public MyPhoneCall(PhoneCall call) {
        this.bdaddr = call.getBdaddr();
        this.callName = call.getCallName();
        this.callNumber = call.getCallNumber();
        this.callType = call.getCallType();
        this.callTime = call.getCallTime();
        this.callPlace = call.getCallPlace();

    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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
