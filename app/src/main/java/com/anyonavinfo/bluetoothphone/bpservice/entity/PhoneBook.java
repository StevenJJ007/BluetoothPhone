package com.anyonavinfo.bluetoothphone.bpservice.entity;

/**
 * Created by Drive on 2016/8/26.
 */
public class PhoneBook {
    private String bdaddr;
    private String pbname;
    private String pbnumber;
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

    public PhoneBook() {
    }

    public String getPbplace() {
        return pbplace;
    }

    public void setPbplace(String pbplace) {
        this.pbplace = pbplace;
    }

    public PhoneBook(String bdaddr, String pbname, String pbnumber, String pbplace) {
        this.bdaddr = bdaddr;
        this.pbname = pbname;
        this.pbnumber = pbnumber;
        this.pbplace = pbplace;

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
}
