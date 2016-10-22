package com.anyonavinfo.bluetoothphone.bpclient.bean;

/**
 * Created by navinfo-21 on 2016/9/29.
 */
public class CallRecords {
    private String name;//姓名
    private String phoneNumber;//电话号码
    private String phonetime;//接通时间
    private String phoneaddress;//电话归属地
    private boolean checked;//判断是否勾选的

    public CallRecords(String name, String phoneNumber, String phonetime, String phoneaddress, boolean checked) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.phonetime = phonetime;
        this.phoneaddress = phoneaddress;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhonetime() {
        return phonetime;
    }

    public void setPhonetime(String phonetime) {
        this.phonetime = phonetime;
    }

    public String getPhoneaddress() {
        return phoneaddress;
    }

    public void setPhoneaddress(String phoneaddress) {
        this.phoneaddress = phoneaddress;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
