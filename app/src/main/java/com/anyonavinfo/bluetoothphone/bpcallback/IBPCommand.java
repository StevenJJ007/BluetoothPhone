package com.anyonavinfo.bluetoothphone.bpcallback;

import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneCall;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Drive on 2016/9/20.
 */

public interface IBPCommand {
    void getLocalName();//获取当前蓝牙模块名称

    void setLocalName(String name);//设置当前蓝牙模块名称

    void connectLast();//连接最近一次设备

    void connect(String addr);//连接hfp

    void disconnect();//断开连接

    void deletePair(String addr);//删除配对设备
     void deletePhoneCall(String number,String time);//删除通话记录,若number和time均为null，则删除所有
    void deletePhoneBook(String number);//删除联系人
    void updatePhoneBook(PhoneBook book);//更新联系人

    void phoneAnswer();//接听电话

    void phoneHangUp();//挂断电话

    void phoneReject();//拒接电话

    void phoneDail(String phonenum);//拨打电话

    void phoneDialLast();//拨打最近电话

    void phoneTransfer();//语音切换

    void setVolume(int avVolume, int hfpVolume);//设置音量

    void incVolume(int step);//当前声道音量增加step

    void decVolume(int step);//当前声道音量减小step

    void getVolume();//获取音量

    void mute();//设置麦克风静音

    void unMute();//取消麦克风静音状态

    void phoneBookStartUpdate();//开始下载电话本

    void phoneBookStopUpdate();//停止下载电话本

    void musicPlayOrPause();//音乐播放/暂停

    void musicPlay();//音乐播放

    void musicPause();//音乐暂停

    void musicStop();//音乐停止

    void musicPrevious();//上一曲

    void musicNext();//下一曲

    void getCurrentStatus();//获取当前状态

    void getCurrentDeviceAddr();//获取当前连接设备地址

    void getCurrentDeviceName();//获取当前连接设备名称

    ArrayList<PhoneDevice> getPairList(int number);//查询配对设备列表

    ArrayList<PhoneBook> getPhoneBookList();

    ArrayList<PhoneCall> getPhoneCallList(int type);//更新通话记录



}
