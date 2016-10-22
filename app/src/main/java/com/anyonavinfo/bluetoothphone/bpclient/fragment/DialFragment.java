package com.anyonavinfo.bluetoothphone.bpclient.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.anyonavinfo.bluetoothphone.bpcallback.CommonData;
import com.anyonavinfo.bluetoothphone.bpclient.MainActivity;
import com.anyonavinfo.bluetoothphone.R;
import com.anyonavinfo.bluetoothphone.bpclient.base.BaseFragment;

import java.lang.reflect.Method;


/**
 * Created by navinfo-21 on 2016/9/8.
 */
public class DialFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private EditText etNumb;
    private ImageButton ibtnDeleteNumb;
    private ImageButton dialNum1;
    private ImageButton dialNum2;
    private ImageButton dialNum3;
    private ImageButton dialNum4;
    private ImageButton dialNum5;
    private ImageButton dialNum6;
    private ImageButton dialNum7;
    private ImageButton dialNum8;
    private ImageButton dialNum9;
    private ImageButton dialx;
    private ImageButton dialNum0;
    private ImageButton dialj;
    private Button dialCall;
    private FragmentTransaction transaction;
    private FragmentManager fm;
    private ConnectingFragment connectingFragment;
    private DialFragment dialFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = View.inflate(getActivity(), R.layout.fragment_dial, null);

            setViews();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void addListener() {
        dialCall.setOnClickListener(this);
        dialNum1.setOnClickListener(this);
        dialNum2.setOnClickListener(this);
        dialNum3.setOnClickListener(this);
        dialNum4.setOnClickListener(this);
        dialNum5.setOnClickListener(this);
        dialNum6.setOnClickListener(this);
        dialNum7.setOnClickListener(this);
        dialNum8.setOnClickListener(this);
        dialNum9.setOnClickListener(this);
        dialNum0.setOnClickListener(this);
        dialx.setOnClickListener(this);
        dialj.setOnClickListener(this);
        ibtnDeleteNumb.setOnClickListener(this);

        ibtnDeleteNumb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etNumb.setText("");
                return false;
            }
        });
    }

    private void setViews() {
        etNumb = (EditText) view.findViewById(R.id.et_numb);
        /** edittext不显示软键盘,要显示光标 */
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            etNumb.setInputType(InputType.TYPE_NULL);
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(etNumb, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ibtnDeleteNumb = (ImageButton) view.findViewById(R.id.ibtn_delete_numb);

        dialNum1 = (ImageButton) view.findViewById(R.id.dialNum1);
        dialNum2 = (ImageButton) view.findViewById(R.id.dialNum2);
        dialNum3 = (ImageButton) view.findViewById(R.id.dialNum3);

        dialNum4 = (ImageButton) view.findViewById(R.id.dialNum4);
        dialNum5 = (ImageButton) view.findViewById(R.id.dialNum5);
        dialNum6 = (ImageButton) view.findViewById(R.id.dialNum6);

        dialNum7 = (ImageButton) view.findViewById(R.id.dialNum7);
        dialNum8 = (ImageButton) view.findViewById(R.id.dialNum8);
        dialNum9 = (ImageButton) view.findViewById(R.id.dialNum9);

        dialx = (ImageButton) view.findViewById(R.id.dialx);
        dialNum0 = (ImageButton) view.findViewById(R.id.dialNum0);
        dialj = (ImageButton) view.findViewById(R.id.dialj);

        dialCall = (Button) view.findViewById(R.id.dialCall);

        fm = getFragmentManager();
        connectingFragment = new ConnectingFragment();
        dialFragment = new DialFragment();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialNum1:
                keyPressed(KeyEvent.KEYCODE_1);
                break;
            case R.id.dialNum2:
                keyPressed(KeyEvent.KEYCODE_2);
                break;
            case R.id.dialNum3:
                keyPressed(KeyEvent.KEYCODE_3);
                break;
            case R.id.dialNum4:
                keyPressed(KeyEvent.KEYCODE_4);
                break;
            case R.id.dialNum5:
                keyPressed(KeyEvent.KEYCODE_5);
                break;
            case R.id.dialNum6:
                keyPressed(KeyEvent.KEYCODE_6);
                break;
            case R.id.dialNum7:
                keyPressed(KeyEvent.KEYCODE_7);
                break;
            case R.id.dialNum8:
                keyPressed(KeyEvent.KEYCODE_8);
                break;
            case R.id.dialNum9:
                keyPressed(KeyEvent.KEYCODE_9);
                break;
            case R.id.dialNum0:
                keyPressed(KeyEvent.KEYCODE_0);
                break;
            case R.id.dialj:
                keyPressed(KeyEvent.KEYCODE_POUND);
                break;
            case R.id.dialx:
                keyPressed(KeyEvent.KEYCODE_STAR);
                break;
            case R.id.ibtn_delete_numb:
                keyPressed(KeyEvent.KEYCODE_DEL);
                break;
            case R.id.dialCall:
                if (CommonData.hfpStatu < 2) {
                    Toast.makeText(getActivity(), "蓝牙未连接", Toast.LENGTH_SHORT).show();
                } else {
                    String number = etNumb.getText().toString();//获取号码待用
                    ((MainActivity) getActivity()).phoneService.phoneDail(number);
                }
                break;
        }

    }

    private void keyPressed(int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        etNumb.onKeyDown(keyCode, event);
    }

}
