package com.anyonavinfo.bluetoothphone.bpcallback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.anyonavinfo.bluetoothphone.bpclient.MainActivity;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneCall;

/**
 * Created by Drive on 2016/10/11.
 */

public class IBPCallbackImpl implements IBPCallback {

    static final String TAG = "BPService";
    private Handler mHandler;
    private Context mContext;
    private static IBPCallbackImpl callback;
    private TalkingThread talkingThread ;


    private IBPCallbackImpl(Context context) {
        mContext = context;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    ;

    public static IBPCallbackImpl getCallback(Context context) {
        if (callback == null) {
            synchronized (IBPCallbackImpl.class) {
                if (callback == null) {
                    callback = new IBPCallbackImpl(context);
                }
            }
        }
        return callback;
    }

    ;

    private void Log(String log) {
        Log.e(TAG, "From IBPCallbackImpl:" + log);
    }

    private void sendMessage(Message msg) {
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onHfpDisconnected() {
        Log("Hfp is disconnected !");
        Message msg = new Message();
        msg.what = CommonData.HFP_DISCONNECTED;
        sendMessage(msg);
    }

    @Override
    public void onHfpConnected() {
        Log("Hfp is connected !");
        Message msg = new Message();
        msg.what = CommonData.HFP_CONNECTED;
        sendMessage(msg);
    }

    @Override
    public void onIncoming(PhoneBook book) {
        Log(book.getPbname() + " " + book.getPbnumber() + " is calling you !");
        CommonData.talkingContact = book;
        Bundle bundle = new Bundle();
        bundle.putString("name", book.getPbname());
        bundle.putString("number", book.getPbnumber());
        bundle.putString("place",book.getPbplace());
        launchActivity(mContext,"com.anyonavinfo.bluetoothphone",bundle);
    }

    @Override
    public void onDialing(PhoneBook book) {
        Log("You are dialing to " + book.getPbname() + " " + book.getPbnumber());
        CommonData.talkingContact = book;
        Message msg = new Message();
        msg.what = CommonData.PHONE_DIALING;
        Bundle bundle = new Bundle();
        bundle.putString("name", book.getPbname());
        bundle.putString("number", book.getPbnumber());
        bundle.putString("place",book.getPbplace());
        msg.setData(bundle);
        sendMessage(msg);
    }

    @Override
    public void onHangUp() {
        Log("The phone is hang up !");
        if(talkingThread!=null){
            CommonData.talkingTime=-10;
            talkingThread = null;
        }
        CommonData.talkingContact = null;
        Message msg = new Message();
        msg.what = CommonData.PHONE_HANGUP;
        sendMessage(msg);
    }

    @Override
    public void onTalking(PhoneBook book) {
        Log("You are talking to " + book.getPbname() + " " + book.getPbnumber());
        CommonData.talkingContact = book;
        Message msg = new Message();
        msg.what = CommonData.PHONE_TALKING;
        Bundle bundle = new Bundle();
        bundle.putString("name", book.getPbname());
        bundle.putString("number", book.getPbnumber());
        bundle.putString("place",book.getPbplace());
        msg.setData(bundle);
        sendMessage(msg);
    }

    @Override
    public void onCallSuccessed(PhoneBook book) {
        Log("You had call "+book.getPbname()+" successfully !");
        CommonData.talkingContact = book;
        CommonData.talkingTime = 0;
        talkingThread = new TalkingThread();
        talkingThread.start();
        Message msg = new Message();
        msg.what = CommonData.PHONE_CALL_SUCCESSED;
        Bundle bundle = new Bundle();
        bundle.putString("name", book.getPbname());
        bundle.putString("number", book.getPbnumber());
        bundle.putString("place",book.getPbplace());
        msg.setData(bundle);
        sendMessage(msg);
    }

    @Override
    public void onVoiceConnected() {
        Log("The voice is connected !");
        CommonData.voiceStatu = 1;
        Message msg = new Message();
        msg.what = CommonData.VOICE_CONNECTED;
        sendMessage(msg);
    }

    @Override
    public void onVoiceDisconnected() {
        Log("The voice is disconnected !");
        CommonData.voiceStatu = 0;
        Message msg = new Message();
        msg.what = CommonData.VOICE_DISCONNECTED;
        sendMessage(msg);
    }

    @Override
    public void onCurrentDeviceAddr(String addr) {
        Log("Current device address is " + addr);
        CommonData.curDeviceAddr = addr;
        Message msg = new Message();
        msg.what = CommonData.CURRENT_DEVICE_ADDR;
        msg.getData().putString("addr", addr);
        sendMessage(msg);
    }

    @Override
    public void onCurrentDeviceName(String name) {
        Log("Current device name is " + name);
        CommonData.curDeviceName = name;
        Message msg = new Message();
        msg.what = CommonData.CURRENT_DEVICE_NAME;
        msg.getData().putString("name", name);
        sendMessage(msg);
    }

    @Override
    public void onCurrentName(String name) {
        Log("This bluetoothPhone model name is " + name);
        CommonData.localName = name;
        Message msg = new Message();
        msg.what = CommonData.CURRENT_BLUETOOTH_NAME;
        msg.getData().putString("name", name);
        sendMessage(msg);
    }

    @Override
    public void onHfpStatus(int status) {
        Log("Hfp status is " + status);
        CommonData.hfpStatu = status;
        Message msg = new Message();
        msg.what = CommonData.HFP_STATU;
        msg.arg1 = status;
        sendMessage(msg);
    }

    @Override
    public void onAvrcpStatus(int status) {
        Log("Avrcp status is " + status);
        CommonData.avrcpStatu = status;
        Message msg = new Message();
        msg.what = CommonData.AVRCP_STATU;
        msg.arg1 = status;
        sendMessage(msg);
    }

    @Override
    public void onA2dpStatus(int status) {
        Log("A2dp status is " + status);
        CommonData.a2dpStatu = status;
        Message msg = new Message();
        msg.what = CommonData.A2DP_STATU;
        msg.arg1 = status;
        sendMessage(msg);
    }

    @Override
    public void onStatus(int powerStatu, int pairStatu, int hfpStatu, int a2dpStatu, int avrcpStatu) {
        Log("all status : powerStatu=" + powerStatu + " pairStatu=" + pairStatu + " hfpStatu=" + hfpStatu + " a2dpStatu=" + a2dpStatu + " avrcpStatu=" + avrcpStatu);
        CommonData.hfpStatu = hfpStatu;
        CommonData.a2dpStatu = a2dpStatu;
        CommonData.avrcpStatu = avrcpStatu;
        Message msg = new Message();
        msg.what = CommonData.BLUETOOTH_STATUS;
        Bundle bundle = new Bundle();
        bundle.putInt("powerstatu", powerStatu);
        bundle.putInt("pairstatu", pairStatu);
        bundle.putInt("hfpstatu", hfpStatu);
        bundle.putInt("a2dpstatu", a2dpStatu);
        bundle.putInt("avrcpstatu", avrcpStatu);
        msg.setData(bundle);
        sendMessage(msg);
    }

    @Override
    public void onA2dpConnected() {
        Log("A2dp is connected !");
        Message msg = new Message();
        msg.what = CommonData.A2DP_CONNECTED;
        sendMessage(msg);
    }

    @Override
    public void onA2dpDisconnected() {
        Log("A2dp is disconnected !");
        Message msg = new Message();
        msg.what = CommonData.A2DP_DISCONNECTED;
        sendMessage(msg);
    }

    @Override
    public void onAvrcpConnected() {
        Log("Avrcp is connected !");
        Message msg = new Message();
        msg.what = CommonData.AVRCP_CONNECTED;
        sendMessage(msg);
    }

    @Override
    public void onAvrcpDisconnected() {
        Log("Avrcp is disconnected !");
        Message msg = new Message();
        msg.what = CommonData.AVRCP_DISCONNECTED;
        sendMessage(msg);
    }

    @Override
    public void onVolume(int avVolume, int hfpVolume) {
        Log("Hfp Volume is " + hfpVolume + " A2dp Volume is " + avVolume);
        CommonData.avVolume = avVolume;
        CommonData.hfpStatu = hfpVolume;
        Message msg = new Message();
        msg.what = CommonData.VOLUME;
        msg.arg1 = avVolume;
        msg.arg2 = hfpVolume;
        sendMessage(msg);
    }

    @Override
    public void onPhoneBookStart() {
        Log("PhoneBook is starting download !");
        Message msg = new Message();
        msg.what = CommonData.PHONEBOOK_DOWNLOAD_START;
        sendMessage(msg);
    }

    @Override
    public void onPhoneBook(PhoneBook book) {
        Log("phonebook : name=" + book.getPbname() + " number=" + book.getPbnumber() + " had downloaded !");
        Message msg = new Message();
        msg.what = CommonData.PHONEBOOK;
        Bundle bundle = new Bundle();
        bundle.putString("name", book.getPbname());
        bundle.putString("number", book.getPbnumber());
        bundle.putString("place",book.getPbplace());
        msg.setData(bundle);
        sendMessage(msg);
    }

    @Override
    public void onPhoneBookDone() {
        Log("PhoneBook had downloaded !");
        Message msg = new Message();
        msg.what = CommonData.PHONEBOOK_DOWNLOAD_DONE;
        sendMessage(msg);
    }

    @Override
    public void onCalllog(PhoneCall call) {
        Log("call : name =" + call.getCallName() + " number=" + call.getCallNumber() + " time=" + call.getCallTime() + " type=" + call.getCallType());
        Message msg = new Message();
        msg.what = CommonData.PHONECALL;
        msg.obj = call;
        sendMessage(msg);
    }

    @Override
    public void onHfpConnecting() {
        Log("Hfp is connecting !");
        Message msg = new Message();
        msg.what = CommonData.HFP_CONNECTING;
        sendMessage(msg);
    }

    @Override
    public void onA2dpConnecting() {
        Log("A2dp is connecting !");
        Message msg = new Message();
        msg.what = CommonData.A2DP_CONNECTING;
        sendMessage(msg);
    }

    @Override
    public void onAvrcpConnecting() {
        Log("Avrcp is disconnected !");
        Message msg = new Message();
        msg.what = CommonData.AVRCP_CONNECTING;
        sendMessage(msg);
    }

    @Override
    public void onMute() {
        Log("Maikefeng is mute !");
        CommonData.mute = 1;
        Message msg = new Message();
        msg.what = CommonData.VOLUME_MUTE;
        sendMessage(msg);
    }

    @Override
    public void onUnMute() {
        Log("Maikefeng is unMute !");
        CommonData.mute = 0;
        Message msg = new Message();
        msg.what = CommonData.VOLUME_UNMUTE;
        sendMessage(msg);
    }

    @Override
    public void onPhoneOperatorSuccessed(String operator) {
        Log("Phone operator is get for network successfully !");
        if(CommonData.talkingContact!=null){
            CommonData.talkingContact.setPbplace(operator);
        }
        Message msg = new Message();
        msg.what = CommonData.PHONEOPERATOR_SUCCESSED;
        Bundle bundle = new Bundle();
        bundle.putString("operator",operator);
        msg.setData(bundle);
        sendMessage(msg);
    }

    class TalkingThread extends Thread {
        @Override
        public void run() {
            while (CommonData.talkingTime>-1){
                Message msg = new Message();
                msg.what = CommonData.UPDATE_TALKING_TIME;
                msg.arg1 = CommonData.talkingTime;
                sendMessage(msg);
                try {
                Thread.sleep(1000);
                CommonData.talkingTime++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }}
        }
    }
    public static boolean launchActivity(Context context, String packageName,Bundle bundle) {
        if (packageName != null && !packageName.equals("")
                ) {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            intent.putExtras(bundle);
            intent.setAction("PHONE_INCOMING");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(intent.resolveActivity(context.getPackageManager())!=null){
                context.startActivity(intent);
            }
            return true;
        }
        return false;
    }
}
