package com.anyonavinfo.bluetoothphone.bpclient.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anyonavinfo.bluetoothphone.R;
import com.anyonavinfo.bluetoothphone.bpcallback.CommonData;
import com.anyonavinfo.bluetoothphone.bpclient.MainActivity;
import com.anyonavinfo.bluetoothphone.bpclient.adapter.LinkedDeviceAdapter;
import com.anyonavinfo.bluetoothphone.bpclient.base.BaseFragment;
import com.anyonavinfo.bluetoothphone.bpclient.bean.ConnectedDeviceBean;
import com.anyonavinfo.bluetoothphone.bpclient.bean.DeviceBean;
import com.anyonavinfo.bluetoothphone.bpclient.custom.SwitchButton;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneDevice;
import com.anyonavinfo.bluetoothphone.bpservice.service.BluetoothPhoneService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by navinfo-21 on 2016/9/8.
 */
public class SetFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private SwitchButton aSwitch;
    public TextView ed_dev_name;
    private TextView ed_paring_code;
    private Button revise_dev_name;
    private Button async_contacts;
    private ListView devlist;
    private List<DeviceBean> deviceBeanList;
    public LinkedDeviceAdapter deviceAdapter = null;
    ArrayList<DeviceBean> deviceList;
    private OnUiReady uiReadyListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = View.inflate(getActivity(), R.layout.fragment_set, null);
            setViews();
            initDeviceData();
            addListener();
            uiReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    private void addListener() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    devlist.setVisibility(View.VISIBLE);
                } else {
                    devlist.setVisibility(View.GONE);
                    ((MainActivity) getActivity()).phoneService.disconnect();
                }
            }

        });

        revise_dev_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }


    private void initDeviceData() {
        updateDeviceData();
        deviceAdapter = new LinkedDeviceAdapter(getActivity(), deviceList);
        devlist.setAdapter(deviceAdapter);
    }

    public void updateDeviceData() {
        deviceList = transformDevicelist(((MainActivity) getActivity()).phoneService.getPairList(5));
    }

    public void updateDeviceState(String addr, int state) {
        if (deviceList != null && deviceList.size() > 0) {
            if (addr == null) {
                if (state == 0) {
                    for (DeviceBean bean : deviceList) {
                        bean.setDeviceState(0);
                    }
                }
            } else {
                for (DeviceBean bean : deviceList) {
                    if (addr.equals(bean.getDeviceAddr())) {
                        bean.setDeviceState(state);
                    } else {
                        bean.setDeviceState(0);
                    }
                }
            }
            deviceAdapter.setData(deviceList);
            deviceAdapter.notifyDataSetChanged();
        }
    }


    private void setViews() {
        aSwitch = (SwitchButton) view.findViewById(R.id.btn_switch);
        ed_dev_name = (TextView) view.findViewById(R.id.edit_dev_name);
        revise_dev_name = (Button) view.findViewById(R.id.revise_dev_name);
        ed_paring_code = (TextView) view.findViewById(R.id.edit_pairing_code);
        async_contacts = (Button) view.findViewById(R.id.async_contacts_application);
        async_contacts.setOnClickListener(this);
        devlist = (ListView) view.findViewById(R.id.dev_list);
        devlist.setVisibility(View.GONE);

        if (CommonData.localName != null)
            ed_dev_name.setText(CommonData.localName);

    }

    ArrayList<DeviceBean> transformDevicelist(ArrayList<PhoneDevice> list) {
        if (list == null) {
            return null;
        }
        ArrayList list1 = new ArrayList<DeviceBean>();
        for (PhoneDevice device : list) {
            DeviceBean bean = new DeviceBean(device);
            list1.add(bean);
        }
        return list1;
    }


    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请修改设备名称");    //设置对话框标题
        //builder.setIcon(android.R.drawable.ic_dialog_info);
        final EditText edit = new EditText(getActivity());
        edit.setPadding(0, 50, 0, 0);
        edit.setTextSize(25);
        builder.setView(edit);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ed_dev_name.setText(edit.getText().toString());
                String reviseName = edit.getText().toString();
                ((MainActivity) getActivity()).phoneService.setLocalName(reviseName);

                ((MainActivity) getActivity()).postDelayedRunnable(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) getActivity()).phoneService.getLocalName();
                    }
                }, 1000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.async_contacts_application:
                if (CommonData.hfpStatu < 2) {
                    Toast.makeText(getActivity(), "蓝牙未连接", Toast.LENGTH_SHORT).show();
                } else {
                    ((MainActivity) getActivity()).showProgressDialog();
                }
                break;
        }
    }

    public interface OnUiReady {
        void uiIsReady();
    }

    public void setOnUiReadyListener(OnUiReady uiReady) {
        this.uiReadyListener = uiReady;
    }

    private void uiReady() {
        if (this.uiReadyListener != null) {
            this.uiReadyListener.uiIsReady();
        }
    }
}
