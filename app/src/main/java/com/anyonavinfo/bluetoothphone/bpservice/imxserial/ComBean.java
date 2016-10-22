package com.anyonavinfo.bluetoothphone.bpservice.imxserial;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * @author Mr Chen
 *
 */
public class ComBean {
		public byte[] bRec=null;
		public String sRecTime="";
		public ComBean(byte[] buffer,int size){
			bRec=new byte[size];
			for (int i = 0; i < size; i++)
			{
				bRec[i]=buffer[i];
			}
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			sRecTime = sDateFormat.format(new java.util.Date()); 
		}
}