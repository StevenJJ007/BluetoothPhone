package com.anyonavinfo.bluetoothphone.bpclient.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public  class BaseFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	/**
	 * 不能后退到上一个Fragment
	 * 
	 * @param fragment
	 */
	public void replaceFragment(Fragment fragment) {
		((BaseFragmentActivity) getActivity()).replaceFragment(fragment);
	}
	
	/**
	 * 可以后退到上一个Fragment
	 * 
	 * @param fragment
	 */
	public void addFragment(Fragment fragment) {
		((BaseFragmentActivity) getActivity()).addFragment(fragment);
	}
	
	/**
	 * 不能后退到上一个Fragment
	 * 
	 * @param fragment
	 */
	public void addFragmentWithoutBack(Fragment fragment) {
		((BaseFragmentActivity) getActivity()).addFragmentWithoutBack(fragment);
	}
	
	/**
	 * 后退到后退栈中的某个Fragment
	 * 
	 * @param fragmentName
	 */
	public void backToFragment(String fragmentName) {
		((BaseFragmentActivity) getActivity()).backToFragment(fragmentName);
	}
	
	/**
	 * 后退到第一个Fragment
	 * 
	 */
	public void backToTop() {
		((BaseFragmentActivity) getActivity()).backToTop();
	}
	
}
