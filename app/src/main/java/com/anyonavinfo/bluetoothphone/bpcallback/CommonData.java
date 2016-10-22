package com.anyonavinfo.bluetoothphone.bpcallback;

import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;

/**
 * Created by Drive on 2016/10/17.
 */

public class CommonData {
    public static final int HFP_CONNECTED = 0x1001;
    public static final int HFP_DISCONNECTED = 0x1002;
    public static final int HFP_CONNECTING = 0x1003;
    public static final int A2DP_CONNECTED = 0x1004;
    public static final int A2DP_DISCONNECTED = 0x1005;
    public static final int A2DP_CONNECTING = 0x1006;
    public static final int AVRCP_CONNECTED = 0x1007;
    public static final int AVRCP_DISCONNECTED = 0x1008;
    public static final int AVRCP_CONNECTING = 0x1009;
    public static final int HFP_STATU = 0x1010;
    public static final int A2DP_STATU = 0x1011;
    public static final int AVRCP_STATU = 0x1012;
    public static final int BLUETOOTH_STATUS = 0x1013;
    public static final int PHONE_DIALING = 0x1014;
    public static final int PHONE_INCOMING = 0x1015;
    public static final int PHONE_TALKING = 0x1016;
    public static final int PHONE_HANGUP = 0x1017;
    public static final int PHONE_CALL_SUCCESSED = 0x1018;
    public static final int VOICE_CONNECTED = 0x1019;
    public static final int VOICE_DISCONNECTED = 0x1020;
    public static final int CURRENT_DEVICE_NAME = 0x1021;
    public static final int CURRENT_DEVICE_ADDR = 0x1022;
    public static final int CURRENT_BLUETOOTH_NAME = 0x1023;
    public static final int PHONEBOOK_DOWNLOAD_START = 0x1024;
    public static final int PHONEBOOK = 0x1025;
    public static final int PHONEBOOK_DOWNLOAD_DONE = 0x1026;
    public static final int PHONECALL=0x1027;
    public static final int VOLUME = 0x1028;
    public static final int VOLUME_MUTE = 0x1029;
    public static final int VOLUME_UNMUTE = 0x1030;
    public static final int PHONEOPERATOR_SUCCESSED = 0x1031;

    public static final int UPDATE_TALKING_TIME = 0X2001;

    public static int hfpStatu;
    public static int a2dpStatu;
    public static int avrcpStatu;
    public static int voiceStatu;//0未连接,1已连接
    public static int hfpVolume;
    public static int avVolume;
    public static int mute;
    public static String curDeviceName;
    public static String curDeviceAddr;//和hfp=2有时序差
    public static String localName;

    //通话信息
    public static PhoneBook talkingContact;
    public static int talkingTime;//通话时间
}
