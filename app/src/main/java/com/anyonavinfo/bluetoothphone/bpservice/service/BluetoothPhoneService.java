package com.anyonavinfo.bluetoothphone.bpservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.anyonavinfo.bluetoothphone.bpcallback.IBPCommand;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneCall;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneDevice;
import com.anyonavinfo.bluetoothphone.bpservice.imxserial.UartConnect;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Drive on 2016/8/24.
 */
public class BluetoothPhoneService extends Service implements IBPCommand {
    private static final String TAG1 = "BPService";
    static final String TAG = "UartConnect";
    private StartThread startThread;
    private UartConnect uartConnect;
    private BluetoothPhoneHal phoneHal;


    public class MyBinder extends Binder {
        public BluetoothPhoneService getService() {
            return BluetoothPhoneService.this;
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        phoneHal = new BluetoothPhoneHal(this);
        phoneHal.setOnMcuOutput(new BluetoothPhoneHal.OnMcuOutput() {

            @Override
            public void outputMcuCommand(String command) {
                sendCommand(command);
            }

        });
        startThread = new StartThread();
        startThread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.startService(new Intent(this, BluetoothPhoneService.class));
    }



    /**
     * sent command to bluetooth module
     */
    private void sendCommand(String command) {
        if (uartConnect != null) {
            uartConnect.sentCommand("AT+" + command);
            Log.d(TAG, "sendCommand: " + "AT+" + command + "\r\n");
        }
    }




    class StartThread extends Thread {

        @Override
        public void run() {
            uartConnect = new UartConnect("/dev/ttyMT2", 115200, 0, 8);
            uartConnect.setDataReceiver(new UartConnect.OnDataReceiver() {

                @Override
                public void PutData(byte[] receivedData) {
                        phoneHal.inMcu(receivedData);

                }
            });

            try {
                Thread.sleep(500);
                phoneHal.command_getWorkState();
                sleep(20);
                phoneHal.command_getCurDeviceName();
                sleep(20);
                phoneHal.command_getCurDeviceAddr();
                sleep(20);
                phoneHal.command_getDeviceName();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void getLocalName() {
        phoneHal.command_getDeviceName();
    }

    @Override
    public void setLocalName(String name) {
        phoneHal.command_setDeviceName(name);
    }

    @Override
    public void connectLast() {
        phoneHal.command_connectLast();
    }

    @Override
    public void connect(String addr) {
        phoneHal.command_connect(addr);
    }


    @Override
    public void disconnect() {
        phoneHal.command_disConnect();
    }




    @Override
    public void deletePair(String addr) {
        phoneHal.deletePair(addr);
    }

    @Override
    public void deletePhoneCall(String number, String time) {
        phoneHal.deletePhoneCall(number,time);
    }

    @Override
    public void deletePhoneBook(String number) {
        phoneHal.deletePhoneBook(number);
    }

    @Override
    public void updatePhoneBook(PhoneBook book) {
        phoneHal.updatePhoneBook(book);
    }


    @Override
    public void phoneAnswer() {
        phoneHal.command_callAnswer();
    }

    @Override
    public void phoneHangUp() {
        phoneHal.command_callHang();
        ;
    }

    @Override
    public void phoneReject() {
        phoneHal.command_callReject();
        ;
    }

    @Override
    public void phoneDail(String phonenum) {
        phoneHal.command_dialPhone(phonenum);
    }



    @Override
    public void phoneTransfer() {
        phoneHal.command_transform();
    }
    ;

    @Override
    public void phoneDialLast() {
        phoneHal.command_dialLast();
    }
    ;

    @Override
    public void setVolume(int avVolume, int hfpVolume) {
        phoneHal.command_setVolume(avVolume, hfpVolume);
    }

    @Override
    public void incVolume(int step) {
        if(step==1){
            phoneHal.command_incVolume();
        }else if(step>1){
            phoneHal.command_incVolume(step);
        }        ;
    }

    @Override
    public void decVolume(int step) {
        if(step==1){
            phoneHal.command_decVolume();
        }else if(step>1){
            phoneHal.command_decVolume(step);
        }
    }

    ;

    @Override
    public void getVolume() {
        phoneHal.command_getVolume();
    }

    ;

    @Override
    public void mute() {
        phoneHal.command_mute("1");
    }

    ;

    @Override
    public void unMute() {
        phoneHal.command_mute("0");
    }

    ;

    @Override
    public void phoneBookStartUpdate() {
        phoneHal.command_beginDownloadPhonebook();
    }

    @Override
    public void phoneBookStopUpdate() {
        phoneHal.command_stopDownloadPhonebook();
    }



    @Override
    public void musicPlayOrPause() {
        phoneHal.command_MusicPlay();
    }

    @Override
    public void musicPlay() {

    }

    @Override
    public void musicPause() {

    }

    @Override
    public void musicStop() {
        phoneHal.command_MusicStop();
    }

    @Override
    public void musicPrevious() {
        phoneHal.command_MusicPre();
    }

    @Override
    public void musicNext() {
        phoneHal.command_MusicNext();
    }

    @Override
    public void getCurrentStatus() {
        phoneHal.command_getWorkState();
    }

    ;

    @Override
    public void getCurrentDeviceAddr() {
         phoneHal.command_getCurDeviceAddr();
    }

    ;

    @Override
    public void getCurrentDeviceName() {
         phoneHal.command_getCurDeviceName();
    }

    ;

    @Override
    public ArrayList<PhoneDevice> getPairList(int number) {
        return phoneHal.getPairList(number);
    }

    @Override
    public ArrayList<PhoneBook> getPhoneBookList() {
        return phoneHal.getPhoneBookList();
    }

    @Override
    public ArrayList<PhoneCall> getPhoneCallList(int type) {
            return phoneHal.getPhoneCall(type);
        }

}
