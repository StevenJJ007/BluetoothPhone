package com.anyonavinfo.bluetoothphone.bpservice.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anyonavinfo.bluetoothphone.bpservice.database.SqliteHelper;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneDevice;
import com.anyonavinfo.bluetoothphone.bpservice.utils.NormalUtils;
import com.anyonavinfo.bluetoothphone.bpservice.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by Drive on 2016/8/26.
 */
public class PhoneDeviceDao extends SqliteHelper {
    private SQLiteDatabase db = super.getWritableDatabase();
    private static PhoneDeviceDao dao;

    private PhoneDeviceDao(Context context) {
        super(context);
    }

    private PhoneDeviceDao(Context context, int Version) {
        super(context, Version);
    }

    public static PhoneDeviceDao getInstance(Context context) {
        if (dao == null) {
            synchronized (PhoneDeviceDao.class) {
                if (dao == null) {
                    dao = new PhoneDeviceDao(context);
                }
            }
        }
        return dao;
    }


    public void insertDevice(PhoneDevice device) {
        db.execSQL("insert into " + SqliteHelper.TABLE_NAME_DEVICE + " values (null,?,?,?)", new Object[]{
                device.getBdaddr(), NormalUtils.string2Unicode(device.getBdname()), TimeUtils.getCurTime()
        });

    }

    public void updateDevice(PhoneDevice device) {
        db.execSQL("update " + SqliteHelper.TABLE_NAME_DEVICE + " set bdname=?,bdtime=? where bdaddr = ?", new Object[]{
                NormalUtils.string2Unicode(device.getBdname()), TimeUtils.getCurTime(), device.getBdaddr()
        });
    }

    public void deleteDevice(String addr) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_DEVICE + " where bdaddr = ?", new String[]{
               addr
        });
    }

    public PhoneDevice queryDevice(String addr) {
        PhoneDevice device = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_DEVICE + " where bdaddr = ?", new String[]{
                addr
        });
        if (cursor != null && cursor.getCount() >= 1) {
            cursor.moveToNext();
            device = new PhoneDevice();
            device.setBdname(NormalUtils.unicode2String(cursor.getString(cursor
                    .getColumnIndex("bdname"))));
            device.setBdaddr(addr);
        }
        return device;
    }

    public ArrayList<PhoneDevice> queryAllDevices() {
        ArrayList<PhoneDevice> devicelist = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_DEVICE + " order by bdtime desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            devicelist = new ArrayList<PhoneDevice>();
            while (cursor.moveToNext()) {
                PhoneDevice device = new PhoneDevice();
                device.setBdname(NormalUtils.unicode2String(cursor.getString(cursor
                        .getColumnIndex("bdname"))));
                device.setBdaddr(cursor.getString(cursor
                        .getColumnIndex("bdaddr")));
                devicelist.add(device);
            }
        }
        return devicelist;
    }

    public ArrayList<PhoneDevice> queryDevices(int number) {
        ArrayList<PhoneDevice> devicelist = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_DEVICE + " order by bdtime desc limit 0,"+number, null);
        if (cursor != null && cursor.getCount() > 0) {
            devicelist = new ArrayList<PhoneDevice>();
            while (cursor.moveToNext()) {
                PhoneDevice device = new PhoneDevice();
                device.setBdname(NormalUtils.unicode2String(cursor.getString(cursor
                        .getColumnIndex("bdname"))));
                device.setBdaddr(cursor.getString(cursor
                        .getColumnIndex("bdaddr")));
                devicelist.add(device);
            }
        }
        return devicelist;
    }

}
