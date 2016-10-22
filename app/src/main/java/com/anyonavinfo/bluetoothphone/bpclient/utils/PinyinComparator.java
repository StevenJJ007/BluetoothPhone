package com.anyonavinfo.bluetoothphone.bpclient.utils;


import com.anyonavinfo.bluetoothphone.bpclient.bean.MyPhoneBook;

import java.util.Comparator;

/**
 *
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<MyPhoneBook> {

	public int compare(MyPhoneBook o1, MyPhoneBook o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return 1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return -1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
