package com.anyonavinfo.bluetoothphone.bpclient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyonavinfo.bluetoothphone.R;
import com.anyonavinfo.bluetoothphone.bpcallback.CommonData;
import com.anyonavinfo.bluetoothphone.bpclient.MainActivity;
import com.anyonavinfo.bluetoothphone.bpclient.bean.ConnectedDeviceBean;
import com.anyonavinfo.bluetoothphone.bpclient.bean.DeviceBean;
import com.anyonavinfo.bluetoothphone.bpclient.fragment.DialFragment;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneDevice;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shijj on 2016/9/21.
 */
public class LinkedDeviceAdapter extends BaseAdapter {
    private ArrayList<DeviceBean> deviceList;
    private Context mContext;
    private LayoutInflater inflater;

    public LinkedDeviceAdapter(Context context, ArrayList<DeviceBean> data) {
        mContext = context;
        if (data == null) {
            deviceList = new ArrayList<DeviceBean>();
        } else {
            deviceList = data;
        }
        inflater = LayoutInflater.from(context);

    }

    public void setData(ArrayList<DeviceBean> data) {
        if (data != null) {
            deviceList = data;
        }
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DeviceBean deviceBean = deviceList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.connectedevice_item, null);
            holder = new ViewHolder();
            holder.pic_phone = (ImageView) convertView.findViewById(R.id.pic_phone);
            holder.device_name = (TextView) convertView.findViewById(R.id.linked_device);
            holder.device_status = (TextView) convertView.findViewById(R.id.linked_status);
            holder.delete = (Button) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (deviceBean.getDeviceState() == 0) {
            holder.device_status.setText("未连接");
        } else if (deviceBean.getDeviceState() == 1) {
            holder.device_status.setText("已连接");
        } else {
            holder.device_status.setText("连接中");
        }
        holder.pic_phone.setImageResource(R.drawable.phone);
        holder.delete.setBackgroundResource(R.drawable.delete);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deviceBean.getDeviceState() == 1) {
                    ((MainActivity) mContext).phoneService.disconnect();
                }
                ((MainActivity) mContext).phoneService.deletePair(deviceBean.getDeviceAddr());
                deviceList.remove(deviceBean);
                LinkedDeviceAdapter.this.notifyDataSetChanged();
            }
        });
        holder.device_name.setText(deviceBean.getDeviceName());
        convertView.setOnClickListener(new View.OnClickListener() {
            int time = 0;

            @Override
            public void onClick(View view) {
                if (deviceBean.getDeviceState() == 0) {
                    for(DeviceBean bean:deviceList){
                        if(bean.getDeviceState()==2)
                            return;
                    }
                    if (CommonData.hfpStatu >= 2) {
                        ((MainActivity) mContext).phoneService.disconnect();
                        time = 6000;
                    }else{
                        time=0;
                    }
                    ((MainActivity) mContext).postDelayedRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("1111", "run: is connecting");
                            ((MainActivity) mContext).phoneService.connect(deviceBean.getDeviceAddr());
                            deviceBean.setDeviceState(2);
                            setData(deviceList);
                            LinkedDeviceAdapter.this.notifyDataSetChanged();
                        }
                    }, time);
                    ;
                } else if (deviceBean.getDeviceState() == 1) {
                    ((MainActivity) mContext).phoneService.disconnect();
                }
            }
        });
        return convertView;
    }


    class ViewHolder {
        ImageView pic_phone;
        TextView device_name;
        TextView device_status;
        Button delete;
    }


}
