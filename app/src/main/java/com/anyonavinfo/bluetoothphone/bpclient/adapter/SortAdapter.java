package com.anyonavinfo.bluetoothphone.bpclient.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.anyonavinfo.bluetoothphone.R;
import com.anyonavinfo.bluetoothphone.bpclient.MainActivity;
import com.anyonavinfo.bluetoothphone.bpclient.bean.MyPhoneBook;
import com.anyonavinfo.bluetoothphone.bpclient.utils.Conts;

import java.util.ArrayList;
import java.util.List;


public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<MyPhoneBook> list;
    private Context mContext;
    private boolean visibility = false;

    public SortAdapter(Context mContext, List<MyPhoneBook> list) {
        this.mContext = mContext;
        if (list == null) {
            this.list = new ArrayList<MyPhoneBook>();
        } else {
            this.list = list;
        }
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<MyPhoneBook> list) {
        if (list == null) {
            this.list = new ArrayList<MyPhoneBook>();
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }

    /**
     * 调用adapter中的更新的数据
     */
    public List<MyPhoneBook> getData() {
        return list;
    }

    /**
     * 设置checkbox显示或隐藏的方法
     */
    public void setCBVisibility(Boolean visibility) {
        this.visibility = visibility;
        if (visibility) {
            notifyDataSetChanged();
        }
    }


    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
        final int checkedIndex = position;/**初始化checkbox的监听位置*/
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_linkman, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.linkman_name);
            viewHolder.tvPhoneNumb = (TextView) view.findViewById(R.id.linkman_number);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.linkman_checked);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (visibility) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    list.get(checkedIndex).setChecked(isChecked);
                    int i=0;
                    for(MyPhoneBook book :list){
                        if(book.isChecked()){
                            i++;
                        }
                    }
                    Message msg = new Message();
                    msg.what=0x3002;
                    msg.arg1=i;
                    ((MainActivity) mContext).sendMessage(msg);

                }
            });
        } else {
            viewHolder.checkBox.setVisibility(View.INVISIBLE);
        }

        if (list.get(position).isChecked()) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }

        viewHolder.tvTitle.setText(list.get(position).getPbname());
        viewHolder.tvPhoneNumb.setText(list.get(position).getPbnumber());
        viewHolder.checkBox.setFocusable(false);

        return view;
    }

    final static class ViewHolder {
        TextView tvTitle;
        TextView tvPhoneNumb;
        CheckBox checkBox;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}