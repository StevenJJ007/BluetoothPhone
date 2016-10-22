package com.anyonavinfo.bluetoothphone.bpclient.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyonavinfo.bluetoothphone.bpcallback.CommonData;
import com.anyonavinfo.bluetoothphone.bpclient.MainActivity;
import com.anyonavinfo.bluetoothphone.R;
import com.anyonavinfo.bluetoothphone.bpclient.base.BaseFragment;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;

import java.lang.reflect.Method;

/**
 * Created by shijj on 2016/9/12.
 */
public class ConnectingFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    public TextView call_name;
    public TextView call_dist;
    public TextView call_connect;
    public TextView call_time;
    private RelativeLayout call_status_name;
    private RelativeLayout call_status_place;
    private LinearLayout in_call;
    private RelativeLayout in_call_keyboard;
    private RelativeLayout ring_call;
    private RelativeLayout cancel_call;
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
    private Button dis_call;
    private Button in_cancel_btn;
    private DialFragment dialFragment;
    private FragmentTransaction transaction;
    private FragmentManager fm;
    private OnUiReady uiReadyListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            view = View.inflate(getActivity(), R.layout.fragment_connecting, null);
            setViews();
            addListener();
            initStates();
            uiReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    private void addListener() {
        dis_call.setOnClickListener(this);
        in_cancel_btn.setOnClickListener(this);
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
        Log.d("sjj", "1");
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

        call_connect = (TextView) view.findViewById(R.id.R_connect);
        dis_call = (Button) view.findViewById(R.id.R_btnCall);
        call_name = (TextView) view.findViewById(R.id.R_name);
        call_dist = (TextView) view.findViewById(R.id.R_dist);
        call_time = (TextView) view.findViewById(R.id.connect_time);

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

        call_status_name = (RelativeLayout) view.findViewById(R.id.call_status_name);
        call_status_place = (RelativeLayout) view.findViewById(R.id.call_status_place);
        in_call = (LinearLayout) view.findViewById(R.id.in_call);
        in_call_keyboard = (RelativeLayout) view.findViewById(R.id.in_call_keyboard);
        ring_call = (RelativeLayout) view.findViewById(R.id.ring_call);
        cancel_call = (RelativeLayout) view.findViewById(R.id.cancel_call);
        in_cancel_btn = (Button) view.findViewById(R.id.in_cancel_btn);

        fm = getFragmentManager();
        dialFragment = new DialFragment();

    }

    private void initStates() {
        if (CommonData.talkingContact == null)
            return;
        if (CommonData.hfpStatu == 3) {
            call_connect.setText("拨号中...");
        }
    }

    /**
     * 显示正在通话输入中布局
     */
    public void showCallLayoutIn() {
        call_status_name.setVisibility(View.GONE);
        in_call.setVisibility(View.VISIBLE);
        call_status_place.setVisibility(View.GONE);
        in_call_keyboard.setVisibility(View.VISIBLE);
        call_time.setVisibility(View.VISIBLE);
        cancel_call.setVisibility(View.GONE);
        in_cancel_btn.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏正在通话输入中布局
     */
    public void showCallLayoutOut() {
        call_status_name.setVisibility(View.VISIBLE);
        in_call.setVisibility(View.GONE);
        call_status_place.setVisibility(View.VISIBLE);
        in_call_keyboard.setVisibility(View.GONE);
        call_connect.setVisibility(View.VISIBLE);
        cancel_call.setVisibility(View.VISIBLE);
        in_cancel_btn.setVisibility(View.GONE);
        call_time.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.R_btnCall:
                ((MainActivity) getActivity()).phoneService.phoneHangUp();
                break;
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
            case R.id.in_cancel_btn:
                etNumb.setText("");
                break;
        }
    }

    private void keyPressed(int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        etNumb.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("test", "destroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("test", "detach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test", "detroy");
    }
    public interface  OnUiReady{
        void uiIsReady();
    }
    public void setOnUiReadyListener(OnUiReady uiReady){
        this.uiReadyListener = uiReady;
    }
    private void uiReady(){
        if(this.uiReadyListener!=null){
            this.uiReadyListener.uiIsReady();
        }
    }
    public void setCallData(PhoneBook book) {
        if(book==null){
            return;
        }
        if (book.getPbname().equals("陌生号码")) {
            call_name.setText(book.getPbnumber());
        } else {
            call_name.setText(book.getPbname());
        }
        call_dist.setText(book.getPbplace());
        if(CommonData.hfpStatu==3){
            call_connect.setText("拨号中");
        }
    }

}
