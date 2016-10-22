package com.anyonavinfo.bluetoothphone.bpservice.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.anyonavinfo.bluetoothphone.bpservice.database.SqliteHelper;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;
import com.anyonavinfo.bluetoothphone.bpservice.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by Drive on 2016/8/26.
 */
public class PhoneBookDao extends SqliteHelper {
    private SQLiteDatabase db = super.getWritableDatabase();
    private static PhoneBookDao dao;

    public PhoneBookDao(Context context) {
        super(context);
    }

    public PhoneBookDao(Context context, int Version) {
        super(context, Version);
    }

    public static PhoneBookDao getInstance(Context context) {
        if (dao == null) {
            synchronized (PhoneBookDao.class) {
                if (dao == null) {
                    dao = new PhoneBookDao(context);
                }
            }
        }
        return dao;
    }

    public void insertPhoneBook(PhoneBook book) {
        if (isPhoneBookExist(book.getBdaddr(), book.getPbnumber())) {
            return;
        } else {
            db.execSQL("insert into " + SqliteHelper.TABLE_NAME_PHONEBOOK + " values (null,?,?,?,?,?)", new Object[]{
                    book.getBdaddr(), book.getPbname(), book.getPbnumber(), book.getPbplace(), TimeUtils.getCurTime()
            });
        }


    }

    public void updatePhoneBook(PhoneBook book) {
        db.execSQL("update " + SqliteHelper.TABLE_NAME_PHONEBOOK + " set pbname=?,pbnumber=?,pbplace=?,pbtime=? where bdaddr = ?", new Object[]{
                book.getPbname(), book.getPbnumber(), book.getPbplace(), TimeUtils.getCurDate(), book.getBdaddr()
        });
    }

    public void updatePhoneBookPlace(String addr, String number, String place) {
        db.execSQL("update " + SqliteHelper.TABLE_NAME_PHONEBOOK + " set pbplace=? where bdaddr = ? and pbnumber = ?", new Object[]{
                place, addr, number
        });
    }

    public void deletePhoneBook(PhoneBook book) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_PHONEBOOK + " where bdaddr = ? and pbnumber = ?", new String[]{
                book.getBdaddr(), book.getPbnumber()
        });
    }
    public void deletePhoneBook(String addr,String number) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_PHONEBOOK + " where bdaddr = ? and pbnumber = ?", new String[]{
                addr, number
        });
    }

    public void deleteAllPhoneBooks(String bdaddr) {
        db.execSQL("delete from " + SqliteHelper.TABLE_NAME_PHONEBOOK + " where bdaddr = ?", new String[]{
                bdaddr
        });
    }

    public PhoneBook queryPhoneBook(String addr, String number) {
        PhoneBook book = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_PHONEBOOK + " where bdaddr = ? and pbnumber = ?", new String[]{addr, number});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            book = new PhoneBook();
            book.setBdaddr(addr);
            book.setPbname(cursor.getString(cursor
                    .getColumnIndex("pbname")));
            book.setPbplace(cursor.getString(cursor
                    .getColumnIndex("pbplace")));
            book.setPbnumber(number);
        } else {
            book = new PhoneBook(addr, "陌生号码", number, "");
        }
        return book;
    }

    public boolean isPhoneBookExist(String addr, String number) {
        PhoneBook book = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_PHONEBOOK + " where bdaddr = ? and pbnumber = ?", new String[]{addr, number});
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public String queryPhoneName(String addr, String number) {
        return queryPhoneBook(addr, number).getPbname();
    }

    public String queryPhonePlace(String addr, String number) {
        PhoneBook book = queryPhoneBook(addr, number);
        if (book != null && TextUtils.isEmpty(book.getPbname()))
            return book.getPbplace();
        return "";
    }

    public ArrayList<PhoneBook> queryAllPhoneBooks(String bdaddr) {
        ArrayList<PhoneBook> booklist = null;
        Cursor cursor = db.rawQuery("select * from " + SqliteHelper.TABLE_NAME_PHONEBOOK + " where bdaddr = ? order by pbname asc", new String[]{bdaddr});
        if (cursor != null && cursor.getCount() > 0) {
            booklist = new ArrayList<PhoneBook>();
            while (cursor.moveToNext()) {
                PhoneBook book = new PhoneBook();
                book.setBdaddr(bdaddr);
                book.setPbname(cursor.getString(cursor
                        .getColumnIndex("pbname")));
                book.setPbplace(cursor.getString(cursor
                        .getColumnIndex("pbplace")));
                if (TextUtils.isEmpty(book.getPbname())) {
                    book.setPbname("陌生号码");
                }
                book.setPbnumber(cursor.getString(cursor
                        .getColumnIndex("pbnumber")));
                booklist.add(book);
            }
        }
        return booklist;
    }

}
