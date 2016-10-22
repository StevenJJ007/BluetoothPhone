package com.anyonavinfo.bluetoothphone.bpservice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Drive on 2016/8/26.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "phonebook.db";
    protected static final String TABLE_NAME_DEVICE = "phonedevice";//最近连接设备列表
    protected static final String TABLE_NAME_PHONEBOOK = "phonebook";//电话簿
    protected static final String TABLE_NAME_PHONECALL = "phonecall";//通话记录
    private static int VERSION = 1;
    private final static String CREATE_TABLE_DEVICE = "create table " + TABLE_NAME_DEVICE + "(_id Integer primary key autoincrement,"
            + "bdaddr varchar(20) not null,"
            + "bdname varchar(30),"
            + "bdtime varchar(30))";
    private final static String CREATE_TABLE_PHONEBOOK = "create table " + TABLE_NAME_PHONEBOOK + "(_id Integer primary key autoincrement,"
            + "bdaddr varchar(20) not null REFERENCES " + TABLE_NAME_DEVICE + "(bdaddr) ON DELETE CASCADE ON UPDATE NO ACTION,"
            + "pbname varchar(20) not null,"
            + "pbnumber varchar(20) not null,"
            + "pbplace varchar(20),"
            + "pbtime varchar(30))";
    private final static String CREATE_TABLE_PHONECALL = "create table " + TABLE_NAME_PHONECALL + "(_id Integer primary key autoincrement,"
            + "bdaddr varchar(20) not null REFERENCES " + TABLE_NAME_DEVICE + "(bdaddr) ON DELETE CASCADE ON UPDATE NO ACTION,"
            + "callnumber varchar(20) not null REFERENCES " + TABLE_NAME_PHONEBOOK + "(pbnumber) ON DELETE CASCADE ON UPDATE NO ACTION,"
            + "calltype Integer not null,"
            + "callplace varchar(20) ,"
            + "calltime varchar(30) not null)";

    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public SqliteHelper(Context context, int Version) {
        super(context, DB_NAME, null, Version);
        VERSION = Version;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEVICE);
        db.execSQL(CREATE_TABLE_PHONEBOOK);
        db.execSQL(CREATE_TABLE_PHONECALL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_DEVICE);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_PHONEBOOK);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_PHONECALL);
        onCreate(sqLiteDatabase);
    }
}
