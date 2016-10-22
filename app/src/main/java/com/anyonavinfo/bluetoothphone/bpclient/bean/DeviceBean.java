package com.anyonavinfo.bluetoothphone.bpclient.bean;

import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneDevice;

/**
 * Created by shijj on 2016/10/18.
 */

public class DeviceBean {
    private String deviceName;
    private String deviceAddr;
    private int deviceState;//0未连接 1已连接 2正在连接
    public DeviceBean(PhoneDevice device){
        deviceName = device.getBdname();
        deviceAddr = device.getBdaddr();
    }
    public DeviceBean(){
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceAddr() {
        return deviceAddr;
    }

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceAddr(String deviceAddr) {
        this.deviceAddr = deviceAddr;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }
}
