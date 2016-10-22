package com.anyonavinfo.bluetoothphone.bpcallback;

import android.provider.ContactsContract;

import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneCall;

import java.util.ArrayList;

/**
 * Created by Drive on 2016/9/20.
 */

public interface IBPCallback {
    void onHfpDisconnected();//hfu连接断开
    void onHfpConnected();//hfp已连接
    void onIncoming(PhoneBook book);//来电中
    void onDialing(PhoneBook book);//拨号中
    void onHangUp();//已挂断
    void onTalking(PhoneBook book);//通话中
    void onCallSuccessed(PhoneBook book);//通话成功
    void onVoiceConnected();//音频已连接
    void onVoiceDisconnected();//音频断开连接
    void onCurrentDeviceAddr(String addr);//当前已连接手机设备地址
    void onCurrentDeviceName(String name);//当前已连接设备名
    void onCurrentName(String name);//本蓝牙模块名称
    void onHfpStatus(int status);//hfp状态
    void onAvrcpStatus(int status);//avrcp状态
    void onA2dpStatus(int status);//a2dp状态
    void onStatus(int powerStatu, int pairStatu, int hfpStatu, int a2dpStatu, int avrcpStatu);//状态
    void onA2dpConnected();//a2dp已连接
    void onA2dpDisconnected();//a2dp已断开
    void onAvrcpConnected();//a2dp已连接
    void onAvrcpDisconnected();//a2dp已断开
    void onVolume(int avVolume, int hfpVolume);
    void onPhoneBookStart();//开始下载电话本
    void onPhoneBook(PhoneBook book);//当前正在下载的电话
    void onPhoneBookDone();//电话本下载完成
    void onCalllog(PhoneCall call);//通话记录
    void onHfpConnecting();//hfp连接中
    void onA2dpConnecting();//a2dp连接中
    void onAvrcpConnecting();//avrcp连接中
    void onMute();//麦克风静音已打开
    void onUnMute();//麦克风静音已关闭

    void onPhoneOperatorSuccessed(String operator);

}
