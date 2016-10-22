package com.anyonavinfo.bluetoothphone.bpclient.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anyonavinfo.bluetoothphone.bpclient.MainActivity;
import com.anyonavinfo.bluetoothphone.R;
import com.anyonavinfo.bluetoothphone.bpclient.base.BaseFragment;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;


/**
 * Created by shijj on 2016/9/26.
 */
public class CallerIDsFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private TextView caller_name;
    public TextView caller_dist;
    private Button accept;
    private Button dis;
    private OnUiReady uiReadyListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            view = View.inflate(getActivity(), R.layout.fragment_callerids, null);
            setViews();
            addListener();
            uiReadyListener.uiIsReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void addListener() {
        accept.setOnClickListener(this);
        dis.setOnClickListener(this);

    }

    public void setCallData(PhoneBook book) {
        if(book==null)
            return;
        if (book.getPbname().equals("陌生号码")) {
            caller_name.setText(book.getPbnumber());
        } else {
            caller_name.setText(book.getPbname());
        }
        caller_dist.setText(book.getPbplace());
    }

    private void setViews() {
        caller_name = (TextView) view.findViewById(R.id.caller_name);
        caller_dist = (TextView) view.findViewById(R.id.caller_dist);
        accept = (Button) view.findViewById(R.id.caller_acceptCall);
        dis = (Button) view.findViewById(R.id.caller_discall);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.caller_acceptCall:
                ((MainActivity) getActivity()).phoneService.phoneAnswer();
                break;
            case R.id.caller_discall:
                ((MainActivity) getActivity()).phoneService.phoneHangUp();
                break;
        }

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
}
