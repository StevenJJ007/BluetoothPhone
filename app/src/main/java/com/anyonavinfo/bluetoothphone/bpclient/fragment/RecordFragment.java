package com.anyonavinfo.bluetoothphone.bpclient.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anyonavinfo.bluetoothphone.bpclient.MainActivity;
import com.anyonavinfo.bluetoothphone.R;
import com.anyonavinfo.bluetoothphone.bpclient.adapter.RecordAdapter;
import com.anyonavinfo.bluetoothphone.bpclient.base.BaseFragment;
import com.anyonavinfo.bluetoothphone.bpclient.bean.MyPhoneCall;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneCall;
import com.anyonavinfo.bluetoothphone.bpclient.utils.Conts;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by navinfo-21 on 2016/9/8.
 */
public class RecordFragment extends BaseFragment {
    private View view;

    private TextView tvRecordDelete;
    private Button btnEditRecord;
    private TextView tvRecoedAll;
    private CheckBox cbRecordAll;
    private ImageView ivRecordDivide;
    private ListView lvRecordInfos;
    public Button btnRecordDelete;
    private RecordAdapter adapter;

    private OnUiReady uiReadyListener;

    private MainActivity mainActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = View.inflate(getActivity(), R.layout.fragment_record, null);
            setViews();
            addListener();
            uiReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void addListener() {
        btnEditRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setCBVisibility(true);
                managerView(true);
            }
        });
        btnRecordDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**准备执行删除功能*/
                for (int i = adapter.getData().size() - 1; i >= 0; i--) {
                    if (adapter.getData().get(i).isChecked()) {
                        ((MainActivity)getActivity()).phoneService.deletePhoneCall(adapter.getData().get(i).getCallNumber(),adapter.getData().get(i).getCallTime());
                        adapter.getData().remove(i);
                    }
                }
                btnRecordDelete.setText("删除（" + 0 + "）");
                adapter.setCBVisibility(false);
                adapter.updateListView(adapter.getData());
                cbRecordAll.setChecked(false);
                managerView(false);
            }

        });



        cbRecordAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < adapter.getData().size(); i++) {
                    adapter.getData().get(i).setChecked(isChecked);
                    if(isChecked)
                    btnRecordDelete.setText("删除（" + adapter.getData().size() + "）");
                }
                adapter.updateListView(adapter.getData());
            }
        });



        lvRecordInfos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** 在非编辑情况下拨打电话*/
                if (btnEditRecord.getVisibility()== View.VISIBLE){
                    String number = adapter.getData().get(position).getCallNumber();//获取号码待用
                    mainActivity.phoneService.phoneDail(number);
                }
            }
        });
    }

    private void setViews() {
        tvRecordDelete = (TextView) view.findViewById(R.id.tv_record_delete);
        btnEditRecord = (Button) view.findViewById(R.id.btn_edit_record);
        tvRecoedAll = (TextView) view.findViewById(R.id.tv_record_all);
        cbRecordAll = (CheckBox) view.findViewById(R.id.cb_record_all);
        ivRecordDivide = (ImageView) view.findViewById(R.id.iv_record_divide);
        lvRecordInfos = (ListView) view.findViewById(R.id.lv_record_infos);
        btnRecordDelete = (Button) view.findViewById(R.id.btn_record_delete);

        //通话记录
        mainActivity = (MainActivity) getActivity();
        adapter = new RecordAdapter(getActivity(),null);
        lvRecordInfos.setAdapter(adapter);
    }
    public void updatePhoneCallView(ArrayList<PhoneCall> callList){
        adapter.updateListView(wrapPhoneBookList(callList));
    }
    private ArrayList<MyPhoneCall> wrapPhoneBookList(ArrayList<PhoneCall> callList){
        ArrayList<MyPhoneCall> phoneCalls = null;
        if(callList!=null){
            phoneCalls= new ArrayList<MyPhoneCall>();
            for(PhoneCall call:callList){
                MyPhoneCall myCall = new MyPhoneCall(call);
                phoneCalls.add(myCall);
            }
        }
        return phoneCalls;
    }

    /**
     * 管理显示隐藏
     */
    public void managerView(Boolean aBoolean){
        if (aBoolean){
            btnEditRecord.setVisibility(View.GONE);
            tvRecoedAll.setVisibility(View.VISIBLE);
            cbRecordAll.setVisibility(View.VISIBLE);
            btnRecordDelete.setVisibility(View.VISIBLE);
            ivRecordDivide.setVisibility(View.VISIBLE);
            tvRecordDelete.setVisibility(View.VISIBLE);
        }else{
            btnEditRecord.setVisibility(View.VISIBLE);
            tvRecoedAll.setVisibility(View.GONE);
            cbRecordAll.setVisibility(View.GONE);
            btnRecordDelete.setVisibility(View.GONE);
            ivRecordDivide.setVisibility(View.INVISIBLE);
            tvRecordDelete.setVisibility(View.GONE);
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
