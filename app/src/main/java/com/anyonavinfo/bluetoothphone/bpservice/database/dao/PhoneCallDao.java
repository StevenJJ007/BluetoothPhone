package com.anyonavinfo.bluetoothphone.bpservice.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anyonavinfo.bluetoothphone.bpservice.database.SqliteHelper;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneCall;
import com.anyonavinfo.bluetoothphone.bpservice.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by Drive on 2016/9/8.
 */
public class PhoneCallDao extends SqliteHelper {
    private static PhoneCallDao dao;
    private SQLiteDatabase db = super.getWritableDatabase();
    private Context mContext;

    private PhoneCallDao(Context context) {
        super(context);
        this.mContext=context;
    }

    private PhoneCallDao(Context context, int Version) {
        super(context, Version);
        this.mContext=context;
    }

    public static PhoneCallDao getInstance(Context context) {
        if (dao == null) {
            synchronized (PhoneCallDao.class) {
                if (dao == null) {
                    dao = new PhoneCallDao(context);
                }
            }
        }
        return dao;
    }

    public void insertPhoneCall(PhoneCall call) {
        db.execSQL("insert into " + SqliteHelper.TABLE_NAME_PHONECALL + " values (null,?,?,?,?,?)", new Object[]{
                call.getBdaddr(), call.getCallNumber(), call.getCallType(),call.getCallPlace(), TimeUtils.getCurTime()
        });
    }
    public void updatePhoneCallPlace(String addr,String number,String place) {
        db.execSQL("update " + SqliteHelper.TABLE_NAME_PHONECALL + " set callplace = ? where bdaddr =? and callnumber= ?", new Object[]{
                place,addr,number
        });
    }

    public void deletePhoneCall(PhoneCall call) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ? and callnumber=? and calltype = ? and calltime=?", new Object[]{
                call.getBdaddr(), call.getCallNumber(), call.getCallType(), call.getCallTime()
        });
    }

    public void deletePhoneCall(String addr, String number, String time) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ? and callnumber=? and calltime=?", new Object[]{
                addr, number, time
        });
    }

    public void deletePhoneCalls(String bdaddr, String number, int callType) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ? and callnumber=? and calltype = ?", new String[]{
                bdaddr, number, String.valueOf(callType)
        });
    }

    public void deletePhoneCalls(String bdaddr, String number) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ? and callnumber=?", new String[]{
                bdaddr, number
        });
    }

    public void deleteAllPhoneCalls(String bdaddr) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ?", new String[]{
                bdaddr
        });
    }

    public ArrayList<PhoneCall> queryPhoneCall(String bdaddr, String number, int callType) {
        ArrayList<PhoneCall> calllist = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ? and callnumber=? and calltype = ? order by calltime desc", new String[]{
                bdaddr, number, String.valueOf(callType)
        });
        if (cursor != null && cursor.getCount() > 0) {
            calllist = new ArrayList<PhoneCall>();
            while (cursor.moveToNext()) {
                PhoneCall call = new PhoneCall();
                call.setBdaddr(bdaddr);
                call.setCallNumber(number);
                call.setCallTime(cursor.getString(cursor
                        .getColumnIndex("calltime")));
                call.setCallName(PhoneBookDao.getInstance(mContext).queryPhoneName(bdaddr,number));
                call.setCallPlace(cursor.getString(cursor
                        .getColumnIndex("callplace")));
                call.setCallType(callType);
                calllist.add(call);
            }
        }
        return calllist;
    }

    public ArrayList<PhoneCall> queryPhoneCalls(String bdaddr, String number) {
        ArrayList<PhoneCall> calllist = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ? and callnumber=? order by calltime desc", new String[]{
                bdaddr, number
        });
        if (cursor != null && cursor.getCount() > 0) {
            calllist = new ArrayList<PhoneCall>();
            while (cursor.moveToNext()) {
                PhoneCall call = new PhoneCall();
                call.setBdaddr(bdaddr);
                call.setCallNumber(number);
                call.setCallTime(cursor.getString(cursor
                        .getColumnIndex("calltime")));
                call.setCallType(cursor.getInt(cursor
                        .getColumnIndex("calltype")));
                call.setCallName(PhoneBookDao.getInstance(mContext).queryPhoneName(bdaddr,call.getCallNumber()));
                call.setCallPlace(cursor.getString(cursor
                        .getColumnIndex("callplace")));
                calllist.add(call);
            }
        }
        return calllist;
    }

    public ArrayList<PhoneCall> queryAllPhoneCalls(String bdaddr) {
        ArrayList<PhoneCall> calllist = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ?  order by calltime desc", new String[]{
                bdaddr
        });
        if (cursor != null && cursor.getCount() > 0) {
            calllist = new ArrayList<PhoneCall>();
            while (cursor.moveToNext()) {
                PhoneCall call = new PhoneCall();
                call.setBdaddr(bdaddr);
                call.setCallNumber(cursor.getString(cursor
                        .getColumnIndex("callnumber")));
                call.setCallTime(cursor.getString(cursor
                        .getColumnIndex("calltime")));
                call.setCallType(cursor.getInt(cursor
                        .getColumnIndex("calltype")));
                call.setCallName(PhoneBookDao.getInstance(mContext).queryPhoneName(bdaddr,call.getCallNumber()));
                call.setCallPlace(cursor.getString(cursor
                        .getColumnIndex("callplace")));
                calllist.add(call);
            }
        }
        return calllist;
    }
    public ArrayList<PhoneCall> queryAllPhoneCalls(String bdaddr,int type) {
        ArrayList<PhoneCall> calllist = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_PHONECALL + " where bdaddr = ? and calltype = ? order by calltime desc", new String[]{
                bdaddr,String.valueOf(type)
        });
        if (cursor != null && cursor.getCount() > 0) {
            calllist = new ArrayList<PhoneCall>();
            while (cursor.moveToNext()) {
                PhoneCall call = new PhoneCall();
                call.setBdaddr(bdaddr);
                call.setCallNumber(cursor.getString(cursor
                        .getColumnIndex("callnumber")));
                call.setCallTime(cursor.getString(cursor
                        .getColumnIndex("calltime")));
                call.setCallType(cursor.getInt(cursor
                        .getColumnIndex("calltype")));
                call.setCallName(PhoneBookDao.getInstance(mContext).queryPhoneName(bdaddr,call.getCallNumber()));
                call.setCallPlace(cursor.getString(cursor
                        .getColumnIndex("callplace")));
                calllist.add(call);
            }
        }
        return calllist;
    }
}
