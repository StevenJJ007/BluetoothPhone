package com.anyonavinfo.bluetoothphone.bpservice.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;

import com.anyonavinfo.bluetoothphone.bpcallback.IBPCallback;
import com.anyonavinfo.bluetoothphone.bpcallback.IBPCallbackImpl;
import com.anyonavinfo.bluetoothphone.bpclient.api.TelApi;
import com.anyonavinfo.bluetoothphone.bpclient.bean.TelPlace;
import com.anyonavinfo.bluetoothphone.bpclient.utils.HttpUtil;
import com.anyonavinfo.bluetoothphone.bpclient.utils.JsonUtil;
import com.anyonavinfo.bluetoothphone.bpclient.utils.NetWorkUtil;
import com.anyonavinfo.bluetoothphone.bpservice.database.dao.PhoneBookDao;
import com.anyonavinfo.bluetoothphone.bpservice.database.dao.PhoneCallDao;
import com.anyonavinfo.bluetoothphone.bpservice.database.dao.PhoneDeviceDao;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneCall;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneDevice;
import com.anyonavinfo.bluetoothphone.bpservice.utils.NormalUtils;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Drive on 2016/8/26.
 */
public class BluetoothPhoneHal {
    private static final String TAG = "BPService";
    private String mCurName = null;//本蓝牙模块名称
    private String mCurDevName = null;//连接到本蓝牙模块的名字
    private String mCurDevAddr = null;
    private String mComingPhoneNum = null;
    private String mDiaingPhoneNum = null;
    private PhoneCallDao phoneCallDao;
    private PhoneBookDao phoneBookDao;
    private PhoneDeviceDao phoneDeviceDao;
    private ArrayList<PhoneBook> bookList;
    private volatile boolean isStartDownloadPB;
    private volatile boolean isPhoneInCall;
    private volatile boolean isPhoneDialing;
    private String hfpStatus = "0";//0未连接,1连接中,2已连接，3拨号中，4来电中，5通话中
    private String a2dpStatus = "0";//0未连接,1连接中,2已连接,3播放中
    private String avrcpStatus = "0";//0未连接,1连接中,2已连接
    private String isInPair = "0";//0非配对状态,1配对状态
    private String powerStatus = "0";//0关机状态,1开机状态

    private PBDownloadThread pbDownloadThread;


    private OnMcuOutput onMcuOutput;
    private IBPCallback callback;
    private Context mContext;

    public BluetoothPhoneHal(Context context) {

        phoneCallDao = PhoneCallDao.getInstance(context);
        phoneBookDao = PhoneBookDao.getInstance(context);
        phoneDeviceDao = PhoneDeviceDao.getInstance(context);
        this.mContext = context;
        callback = IBPCallbackImpl.getCallback(context);
    }


    /**
     * 读取电源状态
     */
    public void command_getPowerState() {
        outputMcuCommand("PWR");
    }


    /**
     * 读取软件版本
     */
    public void command_getSoftwareVersion() {
        outputMcuCommand("APP");
    }


    /**
     * 读取当前工作状态
     */
    public void command_getWorkState() {
        outputMcuCommand("ST");
    }

    /**
     * 读取设备名称
     */
    public void command_getDeviceName() {
        outputMcuCommand("NAME");
    }

    /**
     * 设置设备名称
     */
    public void command_setDeviceName(String deviceName) {
        outputMcuCommand("NAME=" + deviceName);
    }

    /**
     * 读取当前连接设备名称
     */
    public void command_getCurDeviceName() {
        outputMcuCommand("PNAME");
    }

    /**
     * 读取当前连接设备地址
     */
    public void command_getCurDeviceAddr() {
        outputMcuCommand("PADDR");
    }


    /**
     * 音量加1级
     */
    public void command_incVolume() {
        outputMcuCommand("VS+");
    }

    /**
     * 音量加step级
     */
    public void command_incVolume(int step) {
        outputMcuCommand("VS+" + step);
    }

    /**
     * 音量减1级
     */
    public void command_decVolume() {
        outputMcuCommand("VS-");
    }

    /**
     * 音量减step级
     */
    public void command_decVolume(int step) {
        outputMcuCommand("VS-" + step);
    }

    /**
     * 读取当前音量
     */
    public void command_getVolume() {
        outputMcuCommand("VS");
    }

    /**
     * 设置音量 av音乐 hfp电话
     */
    public void command_setVolume(int aVolume, int hVolume) {
        outputMcuCommand("VS=" + aVolume + "," + hVolume);
    }

    /**
     * 音乐暂停/播放
     */
    public void command_MusicPlay() {
        outputMcuCommand("PP");
    }

    /**
     * 音乐停止播放
     */
    public void command_MusicStop() {
        outputMcuCommand("STP");
    }

    /**
     * 上一曲
     */
    public void command_MusicPre() {
        outputMcuCommand("BWD");
    }

    /**
     * 下一曲
     */
    public void command_MusicNext() {
        outputMcuCommand("FWD");
    }

    /**
     * 末位重拨
     */
    public void command_dialLast() {
        outputMcuCommand("DL");
    }


    /**
     * 接听电话
     */
    public void command_callAnswer() {
        outputMcuCommand("CA");
    }

    /**
     * 挂断电话
     */
    public void command_callHang() {
        outputMcuCommand("CH");
    }

    /**
     * 拒接电话
     */
    public void command_callReject() {
        outputMcuCommand("CR");
    }


    /**
     * 拨打电话
     */
    public void command_dialPhone(String phoneNumber) {
        outputMcuCommand("D" + phoneNumber);
    }


    /**
     * 语音切换
     */
    public void command_transform() {
        outputMcuCommand("TRN");
    }

    /**
     * 麦克风静音关闭/打开 0关闭 1打开静音
     */
    public void command_mute(String onOrOff) {
        outputMcuCommand("MUTE=" + onOrOff);
    }


    /**
     * 连接最近手机
     */
    public void command_connectLast() {
        command_connect(phoneDeviceDao.queryDevices(1).get(0).getBdaddr());
    }

    /**
     * 连接指定手机
     */
    public void command_connect(String bdaddr) {
        outputMcuCommand("LH" + bdaddr);
    }

    /**
     * 断开手机连接
     */
    public void command_disConnect() {
        outputMcuCommand("LH-");
    }


    /**
     * 开始下载电话本
     */
    public void command_beginDownloadPhonebook() {
        outputMcuCommand("PBDN");
    }

    /**
     * 停止下载电话本
     */
    public void command_stopDownloadPhonebook() {
        outputMcuCommand("PBST");
    }


    public ArrayList<PhoneDevice> getPairList(int number) {
        return phoneDeviceDao.queryDevices(number);
    }

    public void deletePair(String addr) {
        phoneDeviceDao.deleteDevice(addr);
    }

    public void updatePhoneBook(PhoneBook book) {
        phoneBookDao.updatePhoneBook(book);
    }

    public void deletePhoneCall(String number, String time) {
        if (!TextUtils.isEmpty(mCurDevAddr)) {
            if (number == null && time == null) {
                phoneCallDao.deleteAllPhoneCalls(mCurDevAddr);
            } else {
                phoneCallDao.deletePhoneCall(mCurDevAddr, number, time);
            }
        }
    }
    public void deletePhoneBook(String number){
        if (!TextUtils.isEmpty(mCurDevAddr)) {
            phoneBookDao.deletePhoneBook(mCurDevAddr,number);
        }
    }

    public ArrayList<PhoneCall> getPhoneCall(int type) {
        if (!TextUtils.isEmpty(mCurDevAddr)) {
            if (type == 0) {
                return phoneCallDao.queryAllPhoneCalls(mCurDevAddr);
            } else {
                return phoneCallDao.queryAllPhoneCalls(mCurDevAddr, type);
            }
        } else {
            return null;
        }
    }

    public ArrayList<PhoneBook> getPhoneBookList() {
        if (!TextUtils.isEmpty(mCurDevAddr)) {
            return phoneBookDao.queryAllPhoneBooks(mCurDevAddr);
        }
        return null;
    }

    /**
     * 解析从蓝牙断接受过来的数据
     */
    public void inMcu(byte[] rcvFromMcu) {
        String receivedMcu = new String(rcvFromMcu);
//        Log.e(TAG, "received message is :" + receivedMcu);
        if (receivedMcu.equals("OK") || receivedMcu.equals("ERR") || receivedMcu.equals("ACK")
                ) {
        } else if (receivedMcu.length() >= 4 && receivedMcu.substring(0, 4).equals("PWR=")) {
            String power = receivedMcu.substring(4);
        } else if (receivedMcu.length() >= 4 && receivedMcu.substring(0, 4).equals("ACK=")) {
            String ack = receivedMcu.substring(4);
        } else if (receivedMcu.length() >= 4 && receivedMcu.substring(0, 4).equals("APP=")) {
            String appVersion = receivedMcu.substring(4);
        } else if (receivedMcu.length() >= 5 && receivedMcu.substring(0, 5).equals("MUTE=")) {
            String mute = receivedMcu.substring(5);
            if (mute.equals("1")) {
                callback.onMute();
            } else if (mute.equals("0")) {
                callback.onUnMute();
            }
        } else if (receivedMcu.length() >= 3 && receivedMcu.substring(0, 3).equals("ST=")) {
            powerStatus = receivedMcu.substring(3, 4);
            isInPair = receivedMcu.substring(5, 6);
            hfpStatus = receivedMcu.substring(7, 8);
            a2dpStatus = receivedMcu.substring(9, 10);
            avrcpStatus = receivedMcu.substring(11, 12);
            callback.onHfpStatus(Integer.valueOf(hfpStatus));
            callback.onA2dpStatus(Integer.valueOf(a2dpStatus));
            callback.onAvrcpStatus(Integer.valueOf(avrcpStatus));
            callback.onStatus(Integer.valueOf(powerStatus), Integer.valueOf(isInPair), Integer.valueOf(hfpStatus), Integer.valueOf(a2dpStatus), Integer.valueOf(avrcpStatus));
        } else if (receivedMcu.length() >= 4 && receivedMcu.substring(0, 4).equals("SCO=")) {
            String scoStatu = receivedMcu.substring(4);
            if (scoStatu.equals("0")) {
                callback.onVoiceDisconnected();
            } else if (scoStatu.equals("1")) {
                callback.onVoiceConnected();
            }
        } else if (receivedMcu.length() >= 5 && receivedMcu.substring(0, 5).equals("NAME=")) {
            String deviceName = receivedMcu.substring(6, receivedMcu.length() - 1);
            mCurName = deviceName;
            callback.onCurrentName(mCurName);
        } else if (receivedMcu.length() >= 4 && receivedMcu.substring(0, 4).equals("PIN=")) {
            String pinValue = receivedMcu.substring(4);
        } else if (receivedMcu.length() >= 4 && receivedMcu.substring(0, 4).equals("CLID")) {
            String number = receivedMcu.substring(4);
            isPhoneInCall = true;
            mComingPhoneNum = number;
            if (TextUtils.isEmpty(phoneBookDao.queryPhonePlace(mCurDevAddr, number))) {
                getPhoneOperator(number);
            }
        } else if (receivedMcu.length() >= 4 && receivedMcu.substring(0, 4).equals("DLID")) {
            String number = receivedMcu.substring(4);
            if (mDiaingPhoneNum == null) ;
            {
                String callName = phoneBookDao.queryPhoneName(mCurDevAddr, number);
                PhoneCall call = new PhoneCall(mCurDevAddr, callName, number, 3, phoneBookDao.queryPhonePlace(mCurDevAddr, number));
                phoneCallDao.insertPhoneCall(call);
                callback.onCalllog(call);
            }
            isPhoneDialing = true;
            mDiaingPhoneNum = number;
            if (TextUtils.isEmpty(phoneBookDao.queryPhonePlace(mCurDevAddr, number))) {
                getPhoneOperator(number);
            }
        } else if (receivedMcu.length() >= 3 && receivedMcu.substring(0, 3).equals("VS=")) {
            int avVolume = Integer.valueOf(receivedMcu.substring(3, receivedMcu.indexOf(",")));
            int hfpVolume = Integer.valueOf(receivedMcu.substring(receivedMcu.indexOf(",") + 1));
            callback.onVolume(avVolume, hfpVolume);
        } else if (receivedMcu.equals("PBST")) {
            if (bookList == null) {
                bookList = new ArrayList<PhoneBook>();
            } else if (bookList.size() > 0) {
                bookList.clear();
            }
            isStartDownloadPB = true;
            pbDownloadThread = new PBDownloadThread();
            pbDownloadThread.start();
            pbDownloadThread.lastReceivedTime = System.currentTimeMillis();
            callback.onPhoneBookStart();
        } else if (receivedMcu.equals("PBEND")) {
            if (!isStartDownloadPB || pbDownloadThread == null)
                return;
            phoneBookDao.deleteAllPhoneBooks(mCurDevAddr);
            for (PhoneBook phoneBook : bookList) {
                phoneBookDao.insertPhoneBook(phoneBook);
            }
            isStartDownloadPB = false;
            pbDownloadThread.isInterrupted();
            pbDownloadThread = null;
            callback.onPhoneBookDone();
        } else if (receivedMcu.length() >= 3 && receivedMcu.substring(0, 3).equals("PB=")) {
            int nameIndex = receivedMcu.indexOf(",") + 1;
            String pbNumber = receivedMcu.substring(3, nameIndex - 1);
            String pbName = "";
            StringBuffer stringBuffer = new StringBuffer();
            int nameLength = (rcvFromMcu.length - nameIndex) / 2;
            if ((rcvFromMcu.length - nameIndex) % 2 == 0) {
                for (int i = 0; i < nameLength; i++) {
                    stringBuffer.append("\\u").append(Integer.toHexString(((rcvFromMcu[nameIndex + 2 * i] & 0xff) << 8) | (rcvFromMcu[nameIndex + 2 * i + 1] & 0xff)));
                }
                try {
                    pbName = new String(NormalUtils.unicode2String(stringBuffer.toString()).getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else {
                return;
            }
            if (isStartDownloadPB && bookList != null) {
                PhoneBook book = new PhoneBook(mCurDevAddr, pbName, pbNumber, "");
                bookList.add(book);
                pbDownloadThread.lastReceivedTime = System.currentTimeMillis();
                callback.onPhoneBook(book);
            }
        } else if (receivedMcu.length() >= 4 && receivedMcu.substring(0, 4).equals("HFP=")) {
            String hfpStatu = receivedMcu.substring(4);
            switch (hfpStatu) {
                case "0":
                    if ((!hfpStatus.equals("0") || !hfpStatus.equals("1"))) {
                        callback.onHfpDisconnected();
                    }
                    mCurDevAddr = null;
                    mCurDevName = null;
                    phoneStateReset();
                    callback.onCurrentDeviceAddr(mCurDevAddr);
                    callback.onCurrentDeviceName(mCurDevName);
                    break;
                case "1":
                    callback.onHfpConnecting();
                    mCurDevAddr = null;
                    mCurDevName = null;
                    phoneStateReset();
                    break;
                case "2":
                    if (hfpStatus.equals("0") || hfpStatu.equals("1")) {
                        callback.onHfpConnected();
                    } else if (hfpStatus.equals("3") || hfpStatus.equals("4")) {
                        callback.onHangUp();
                        //未接电话记录
                        if (hfpStatus.equals("4")) {
                            if (mComingPhoneNum != null) {
                                String callName = phoneBookDao.queryPhoneName(mCurDevAddr, mComingPhoneNum);
                                PhoneCall call = new PhoneCall(mCurDevAddr, callName, mComingPhoneNum, 2, phoneBookDao.queryPhonePlace(mCurDevAddr, mComingPhoneNum));
                                phoneCallDao.insertPhoneCall(call);
                                callback.onCalllog(call);
                            }
                        }
                    }
                    phoneStateReset();
                    break;
                case "3":
                    if (hfpStatus.equals("2")) {
                        if (mDiaingPhoneNum != null && mCurDevAddr != null) {
                            callback.onDialing(phoneBookDao.queryPhoneBook(mCurDevAddr, mDiaingPhoneNum));
                        } else {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDiaingPhoneNum != null && mCurDevAddr != null) {
                                        callback.onDialing(phoneBookDao.queryPhoneBook(mCurDevAddr, mDiaingPhoneNum));
                                    } else {
                                        return;
                                    }
                                }
                            }, 200);
                        }

                    }
                    break;
                case "4":
                    if (hfpStatus.equals("2")) {
                        if (mComingPhoneNum != null && mCurDevAddr != null) {
                            callback.onIncoming(phoneBookDao.queryPhoneBook(mCurDevAddr, mComingPhoneNum));
                        } else {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mComingPhoneNum != null && mCurDevAddr != null) {
                                        callback.onIncoming(phoneBookDao.queryPhoneBook(mCurDevAddr, mComingPhoneNum));
                                    } else {
                                        return;
                                    }
                                }
                            }, 200);
                        }

                    }

                    break;
                case "5":
                    //已接电话记录
                    if (hfpStatus.equals("4")) {
                        if (mComingPhoneNum != null && mCurDevAddr != null) {
                            PhoneBook book = phoneBookDao.queryPhoneBook(mCurDevAddr, mComingPhoneNum);
                            PhoneCall call = new PhoneCall(mCurDevAddr, book.getPbname(), mComingPhoneNum, 1, book.getPbplace());
                            phoneCallDao.insertPhoneCall(call);
                            callback.onCalllog(call);
                            callback.onCallSuccessed(book);
                        }
                    } else if (hfpStatus.equals("3")) {
                        if (mDiaingPhoneNum != null && mCurDevAddr != null) {
                            callback.onCallSuccessed(phoneBookDao.queryPhoneBook(mCurDevAddr, mDiaingPhoneNum));
                        }
                    }
                    break;
                default:
                    break;
            }
            if (hfpStatus.equals("5") && !hfpStatu.equals("5")) {
                callback.onHangUp();
            }
            hfpStatus = hfpStatu;
            callback.onHfpStatus(Integer.valueOf(hfpStatus));

        } else if (receivedMcu.length() >= 5 && receivedMcu.substring(0, 5).equals("A2DP=")) {
            String a2dpStatu = receivedMcu.substring(5);
            switch (a2dpStatu) {
                case "0":
                    if (a2dpStatus.equals("2"))
                        callback.onA2dpDisconnected();
                    break;
                case "1":
                    callback.onA2dpConnecting();
                    break;
                case "2":
                    if (a2dpStatus.equals("0") || a2dpStatus.equals("1"))
                        callback.onA2dpConnected();
                    break;
                case "3":
                    break;
                default:
                    break;
            }
            a2dpStatus = a2dpStatu;
            callback.onA2dpStatus(Integer.valueOf(a2dpStatu));
        } else if (receivedMcu.length() >= 6 && receivedMcu.substring(0, 6).equals("AVRCP=")) {
            String avrcpStatu = receivedMcu.substring(6);
            callback.onAvrcpStatus(Integer.valueOf(avrcpStatu));
        } else if (receivedMcu.length() >= 6 && receivedMcu.substring(0, 6).equals("PNAME=")) {
            int nameIndex = receivedMcu.indexOf("=") + 2;
            String deviceName = "";
            StringBuffer stringBuffer = new StringBuffer();
            int nameLength = (rcvFromMcu.length - nameIndex - 1) / 2;
            if ((rcvFromMcu.length - nameIndex - 1) % 2 == 0) {
                for (int i = 0; i < nameLength; i++) {
                    stringBuffer.append("\\u").append(Integer.toHexString(((rcvFromMcu[nameIndex + 2 * i] & 0xff) << 8) | (rcvFromMcu[nameIndex + 2 * i + 1] & 0xff)));
                }
                try {
                    deviceName = new String(NormalUtils.unicode2String(stringBuffer.toString()).getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
            mCurDevName = deviceName;
            callback.onCurrentDeviceName(mCurDevName);
        } else if (receivedMcu.length() >= 7 && receivedMcu.substring(0, 7).equals("PHADDR=")) {
            String deviceAddr = receivedMcu.substring(7);
            mCurDevAddr = deviceAddr;
            callback.onCurrentDeviceAddr(mCurDevAddr);
            final PhoneDevice device = phoneDeviceDao.queryDevice(mCurDevAddr);
            if (TextUtils.isEmpty(mCurDevName)) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(mCurDevName)) {
                            if (device == null) {
                                phoneDeviceDao.insertDevice(new PhoneDevice(mCurDevAddr, mCurDevName));
                            } else {
                                phoneDeviceDao.updateDevice(new PhoneDevice(mCurDevAddr, mCurDevName));
                            }
                        }
                    }
                }, 200);
            } else {
                if (device == null) {
                    phoneDeviceDao.insertDevice(new PhoneDevice(mCurDevAddr, mCurDevName));
                } else {
                    phoneDeviceDao.updateDevice(new PhoneDevice(mCurDevAddr, mCurDevName));
                }
            }

        } else if (receivedMcu.equals("RING")) {
            if (mComingPhoneNum != null)
                callback.onTalking(phoneBookDao.queryPhoneBook(mCurDevAddr, mComingPhoneNum));
        }
    }

    private void getPhoneOperator(final String number) {
        if (NetWorkUtil.isConnected(mContext)) {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.getJson(TelApi.API + "?tel=" + number, TelApi.key, new HttpUtil.HttpCallBack() {
                @Override
                public void onSusscess(String data) {
                    Log.e(TAG, "onSusscess: "+data );
                    TelPlace place = JsonUtil.parseJsonToBean(data, TelPlace.class);
                    if(place!=null&&place.getRetData()!=null){
                        String operator = place.getRetData().getCarrier();
                        phoneBookDao.updatePhoneBookPlace(mCurDevAddr, number, operator);
                        phoneCallDao.updatePhoneCallPlace(mCurDevAddr, number, operator);
                        callback.onPhoneOperatorSuccessed(operator);
                    }

                }
            });
        }
    }

    private void phoneStateReset() {
        mComingPhoneNum = null;
        mDiaingPhoneNum = null;
        isPhoneInCall = false;
        isPhoneDialing = false;
    }


    private class PBDownloadThread extends Thread {
        private long lastReceivedTime;

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (isPhoneDialing || isPhoneInCall) {
                        lastReceivedTime = System.currentTimeMillis();
                    } else if ((System.currentTimeMillis() - lastReceivedTime) / 1000 > 10) {
                        command_stopDownloadPhonebook();//若10s内未收到电话本信息且当前未正在通话中，则停止下载
                        break;
                    }
                    sleep(3000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "run: pbdownload thread is interrupted,download is over !");
                    ;
                }
            }
        }
    }


    public interface OnMcuOutput {
        void outputMcuCommand(String command);
    }

    private void outputMcuCommand(String command) {
        if (onMcuOutput != null) {
            onMcuOutput.outputMcuCommand(command);
        }
    }


    public void setOnMcuOutput(OnMcuOutput output) {
        this.onMcuOutput = output;
    }
}
