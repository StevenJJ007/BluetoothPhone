package com.anyonavinfo.bluetoothphone.bpservice.entity;

/**
 * Created by Drive on 2016/8/26.
 */
public class PhoneDevice {
    private String bdaddr;
    private String bdname;

    public PhoneDevice() {
    }

    public PhoneDevice(String bdaddr, String bdname) {

        this.bdaddr = bdaddr;
        this.bdname = bdname;
    }

    public String getBdaddr() {
        return bdaddr;
    }

    public void setBdaddr(String bdaddr) {
        this.bdaddr = bdaddr;
    }

    public String getBdname() {
        return bdname;
    }

    public void setBdname(String bdname) {
        this.bdname = bdname;
    }
}
