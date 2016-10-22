package com.anyonavinfo.bluetoothphone.bpclient.bean;

public class SortModel {

	private String name;//赋值的
	private String sortLetters;//名字转字母用来排序的
	private boolean checked;//判断是否勾选的

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
