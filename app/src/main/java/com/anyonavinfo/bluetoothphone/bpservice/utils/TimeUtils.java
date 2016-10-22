package com.anyonavinfo.bluetoothphone.bpservice.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Drive on 2016/9/8.
 */
public class TimeUtils {
    public static String getCurDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public static String getCurTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static Date stringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date stringToDate2(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static long stringToTime(String date) {
        return stringToDate2(date).getTime();
    }

    public static String getRecentTime(String date) {
        String curTime = getCurTime();
        long curTimeMills = System.currentTimeMillis();
        int spaceIndex = curTime.indexOf(" ")+1;
        int curHour = Integer.valueOf(curTime.substring(spaceIndex,spaceIndex+2));
        int curMin = Integer.valueOf(curTime.substring(spaceIndex+3,spaceIndex+5));
        int curSec = Integer.valueOf(curTime.substring(spaceIndex+6,spaceIndex+8));
        long todayMillionSec = (curHour*3600+curMin*60+curSec)*1000;
        Log.e("1111", "getRecentTime: "+((curTimeMills-stringToTime(date))/1000/3600));
        if (getCurTime().substring(0, 10).equals(date.substring(0, 10))) {
            return date.substring(spaceIndex, spaceIndex +5);
        }else if(((curTimeMills-stringToTime(date))<(24*3600*1000+todayMillionSec))&&((curTimeMills-stringToTime(date))>todayMillionSec)){
            return "昨天";
        }else{
            return date.substring(5,10);
    }}
}
