package com.anyonavinfo.bluetoothphone.bpservice.imxserial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class UartConnect {
    static final String TAG = "UartConnect";
    private OnDataReceiver onDataReceiver = null;
    private FileOutputStream mOutputStream;
    private FileInputStream mInputStream;
    private SerialPort sp;
    private UartReadThread mUartReadThread;
    private DispQueueThread queneThread;
    private SentQueneThread sentQueneThread;
    protected byte[] cmdbuffer = new byte[256];
    protected int mRxCMDcnt = 0;
    protected int FRAMELENGTH = 0;
    int i;

    void LOG(String a, String b) {
        Log.d(a, b);
    }

    public void close() {
        try {

            if (mUartReadThread != null) {
                mUartReadThread.interrupt();//refer to think in java 706
                mUartReadThread = null;
            }
            //同static private (2)
            if (queneThread != null) {
                queneThread.interrupt();
                queneThread = null;
            }

            if (sentQueneThread != null) {
                sentQueneThread.interrupt();
                sentQueneThread = null;
            }
        } catch (Exception e) {

        }

    }

    public UartConnect(String devicename, int baudrate, int flags, int len) {

        try {
            sp = new SerialPort(new File(devicename), baudrate, flags);
            mOutputStream = (FileOutputStream) sp.getOutputStream();
            mInputStream = (FileInputStream) sp.getInputStream();
            mUartReadThread = new UartReadThread();
            mUartReadThread.start();
            queneThread = new DispQueueThread();
            queneThread.start();
            sentQueneThread = new SentQueneThread();
            sentQueneThread.start();
            FRAMELENGTH = len;
        } catch (SecurityException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void SendData(int mLen, byte[] mData) {
        byte[] sData = new byte[mLen];
        for (int i = 0; i < mLen; i++) {
            sData[i] = mData[i];
        }
        try {
            mOutputStream.write(sData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendData(String mStr) {
        try {
            mOutputStream.write((mStr + "\r\n").getBytes());
            Log.d(TAG, "SendData: " + mStr + " is send to bluetooth !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class UartReadThread extends Thread {

        @Override
        public void run() {
            // super.run();
            LOG(TAG, "URAT receive routing start.");
            while (!isInterrupted()) {
                int size;
                try {
                    if (mInputStream != null) {
                        byte[] buffer = new byte[256];
                        size = mInputStream.read(buffer);
                        if (size > 0) {
                            ComBean ComRecData = new ComBean(buffer, size);
                            if (queneThread != null) {
                                queneThread.AddQueue(ComRecData);
                            }
                        }
                        try {
                            Thread.sleep(10);// 延时10ms
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Read Thread Is Interrupted !!!");
                        }
                    } else
                        return;

                } catch (IOException e) {
                    e.printStackTrace();
                    LOG(TAG, "URAT received error.");
                    return;
                }
            }
            LOG(TAG, "URAT receive routing quit.");
        }
    }

    void UartRcv(int size, byte[] rbuffer) {
        i = 0;

        if (size > 0) {
            while (i < size) {

                switch (mRxCMDcnt) {
                    case 0:
                        if (rbuffer[i] == 0x0D) {
                            cmdbuffer[mRxCMDcnt++] = rbuffer[i];
                            // LOG(TAG, "CMDLINE BYTE 0, 0x10");
                        } else {
                            mRxCMDcnt = 0;
                            // LOG(TAG,
                            // "CMDLINE BYTE 0, NOT 0x10, cleared.");
                        }

                        break;
                    case 1:
                        if (rbuffer[i] == (byte) 0x0A) {
                            cmdbuffer[mRxCMDcnt++] = rbuffer[i];
                            // LOG(TAG, "CMDLINE BYTE 1, 0xfe");
                        } else {
                            mRxCMDcnt = 0;
                            // LOG(TAG,
                            // "CMDLINE BYTE 1, NOT 0xfe, cleared.");
                        }
                        break;

                    default:

                        cmdbuffer[mRxCMDcnt++] = rbuffer[i];
                        if (mRxCMDcnt > 3) {
                            if (mRxCMDcnt >= ((cmdbuffer[2]) + 6)) {
                                onDataReceiver.PutData(parseData(cmdbuffer, mRxCMDcnt));
                                mRxCMDcnt = 0;
                            }
                        }
                        break;
                }

                i++;
            }
        }
    }

    private byte[] parseData(byte[] datas, int size) {
        byte[] newDatas = new byte[size - 6];
        for (int i = 3, j = 0; i < size - 3; i++, j++) {
            newDatas[j] = datas[i];
        }
        return newDatas;
    }


    //接受数据线程
    private class DispQueueThread extends Thread {
        private Queue<ComBean> QueueList = new LinkedList<ComBean>();

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                final ComBean ComData;
                while ((ComData = QueueList.poll()) != null) {
                    UartRcv(ComData.bRec.length, ComData.bRec);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Received Thread Is Interrupted !!!");
                    }
                    break;
                }
            }
        }

        public synchronized void AddQueue(ComBean ComData) {
            QueueList.add(ComData);
        }
    }


    public void sentCommand(String command) {
        sentQueneThread.addQuene(command);
    }

    //发送数据线程
    private class SentQueneThread extends Thread {
        private ArrayBlockingQueue<String> sentQuene = new ArrayBlockingQueue<String>(100);

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    final String command = sentQuene.take();
                    if (!TextUtils.isEmpty(command)) {
                        SendData(command);
                    }
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "SentQueneThread is interrupted !");
                }

            }
        }

        public synchronized void addQuene(String command) {
            sentQuene.add(command);
            Log.d(TAG, "addQuene: " + command + " is added to sentQueneThread !");
        }
    }

    public void setDataReceiver(OnDataReceiver onDataReceiver) {
        this.onDataReceiver = onDataReceiver;
    }

    public interface OnDataReceiver {
        void PutData(byte[] receivedData);
    }

}
