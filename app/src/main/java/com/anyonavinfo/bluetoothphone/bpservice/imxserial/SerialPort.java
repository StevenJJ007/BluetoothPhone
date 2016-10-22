package com.anyonavinfo.bluetoothphone.bpservice.imxserial;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Drive on 2016/8/23.
 */
public class SerialPort {
    private static final String TAG_NAME = "SerialPort";
    private static final String TAG = "E3HWService";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private static FileDescriptor mFd;
    private static FileInputStream mFileInputStream;
    private static FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
        Log.d(TAG, "Try to open serial port:"+device.getAbsolutePath());
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }

        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);
    public native void close();

    static {
        System.loadLibrary("imxserial");
    }
}
