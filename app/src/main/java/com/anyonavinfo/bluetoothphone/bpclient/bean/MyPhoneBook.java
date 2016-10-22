package com.anyonavinfo.bluetoothphone.bpclient.bean;

import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;

/**
 * Created by Drive on 2016/8/26.
 */
public class MyPhoneBook {
    private String bdaddr;
    private String pbname;
    private String pbnumber;

    private String sortLetters;//名字转字母用来排序的
    private boolean checked;//判断是否勾选的
    private String pbplace;

    public String getBdaddr() {
        return bdaddr;
    }

    public void setBdaddr(String bdaddr) {
        this.bdaddr = bdaddr;
    }

    public String getPbname() {
        return pbname;
    }

    public MyPhoneBook() {
    }

    public MyPhoneBook(String bdaddr, String pbname, String pbnumber) {
        this.bdaddr = bdaddr;
        this.pbname = pbname;
        this.pbnumber = pbnumber;
    }

    public String getPbplace() {
        return pbplace;
    }

    public void setPbplace(String pbplace) {
        this.pbplace = pbplace;
    }

    public MyPhoneBook(PhoneBook book) {
        this.bdaddr = book.getBdaddr();
        this.pbname = book.getPbname();
        this.pbnumber = book.getPbnumber();
        this.pbplace=book.getPbplace();
    }

    public void setPbname(String pbname) {
        this.pbname = pbname;
    }

    public String getPbnumber() {
        return pbnumber;
    }

    public void setPbnumber(String pbnumber) {
        this.pbnumber = pbnumber;
    }

    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
