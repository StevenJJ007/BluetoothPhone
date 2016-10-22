package com.anyonavinfo.bluetoothphone.bpclient.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Stack;

/**
 * 袁加强
 * FragmentActivity基类
 *
 * @author admin
 */
public class BaseFragmentActivity extends FragmentActivity {

    private Stack<Fragment> mFragmentStack = new Stack<>();
    private Fragment mContentFragment,MainFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);

    }

    /**
     * 不能后退到上一个Fragment
     *
     * @param fragment
     */
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.commit();
    }

    /**
     * 可以后退到上一个Fragment
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    /**
     * 不能后退到上一个Fragment
     *
     * @param fragment
     */
    public void addFragmentWithoutBack(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.commit();
    }

    /**
     * 后退到后退栈中的某个Fragment
     *
     * @param fragmentName
     */
    public void backToFragment(String fragmentName) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStackImmediate(fragmentName, 0);
        transaction.commit();
    }

    /**
     * 后退到第一个Fragment
     */
    public void backToTop() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        // fragmentManager.popBackStack(null,
        // FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commit();
    }


}
