package com.anyonavinfo.bluetoothphone.bpclient.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.anyonavinfo.bluetoothphone.bpclient.adapter.SortAdapter;
import com.anyonavinfo.bluetoothphone.bpclient.base.BaseFragment;
import com.anyonavinfo.bluetoothphone.bpclient.bean.MyPhoneBook;
import com.anyonavinfo.bluetoothphone.bpclient.bean.SortModel;
import com.anyonavinfo.bluetoothphone.bpclient.custom.ClearEditText;
import com.anyonavinfo.bluetoothphone.bpclient.custom.SideBar;
import com.anyonavinfo.bluetoothphone.bpclient.utils.PinyinComparator;
import com.anyonavinfo.bluetoothphone.bpclient.utils.CharacterParser;
import com.anyonavinfo.bluetoothphone.bpclient.utils.Conts;
import com.anyonavinfo.bluetoothphone.bpservice.entity.PhoneBook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by navinfo-21 on 2016/9/8.
 */
public class LinkmanFragment extends BaseFragment {
    private View view;
    private ClearEditText linkmanSearch;
    private SideBar linkmanSidrbar;
    private ListView testListview;
    private Button btnEditLinkman;
    private TextView linkmanTvAll;
    private CheckBox linkmanCbAll;
    public Button btnDeleteLinkman;
    private ImageView ivLinkmanDivide;
    private TextView tvLinkmanDelete;
    private TextView dialog;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    private SortAdapter adapter;
    private OnUiReady uiReadyListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = View.inflate(getActivity(), R.layout.fragment_linkman, null);
            setViews();
            addListener();
            uiReady();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void addListener() {
        linkmanSidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    testListview.setSelection(position);
                }
            }
        });

        linkmanSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnEditLinkman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setCBVisibility(true);
                managerView(true);
            }
        });

        btnDeleteLinkman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**准备执行删除功能*/
                for (int i = adapter.getData().size() - 1; i >= 0; i--) {
                    if (adapter.getData().get(i).isChecked()) {
                        ((MainActivity) getActivity()).phoneService.deletePhoneBook(adapter.getData().get(i).getPbnumber());
                        adapter.getData().remove(i);
                    }
                }
                btnDeleteLinkman.setText("删除（" + 0 + "）");
                adapter.setCBVisibility(false);
                adapter.updateListView(adapter.getData());
                linkmanCbAll.setChecked(false);
                managerView(false);
            }
        });

        linkmanCbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < adapter.getData().size(); i++) {
                    adapter.getData().get(i).setChecked(isChecked);
                }
                if (isChecked)
                    btnDeleteLinkman.setText("删除（" + adapter.getData().size() + "）");
                adapter.updateListView(adapter.getData());
            }
        });


        testListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** 在非编辑情况下拨打电话*/
                if (btnEditLinkman.getVisibility() == View.VISIBLE) {
                    String number = adapter.getData().get(position).getPbnumber();//获取号码待用
                    ((MainActivity) getActivity()).phoneService.phoneDail(number);
                }
            }
        });

    }

    private void setViews() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        linkmanSearch = (ClearEditText) view.findViewById(R.id.linkman_search);
        linkmanSidrbar = (SideBar) view.findViewById(R.id.linkman_sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        testListview = (ListView) view.findViewById(R.id.test_listview);
        btnEditLinkman = (Button) view.findViewById(R.id.btn_edit_linkman);
        linkmanTvAll = (TextView) view.findViewById(R.id.linkman_tv_all);
        linkmanCbAll = (CheckBox) view.findViewById(R.id.linkman_cb_all);
        btnDeleteLinkman = (Button) view.findViewById(R.id.btn_delete_linkman);
        ivLinkmanDivide = (ImageView) view.findViewById(R.id.iv_linkman_divide);
        tvLinkmanDelete = (TextView) view.findViewById(R.id.tv_linkman_delete);

        adapter = new SortAdapter(getActivity(), null);
        testListview.setAdapter(adapter);
        linkmanSidrbar.setTextView(dialog);
    }

    public void updatePhoneBookView(ArrayList<PhoneBook> callList) {
        adapter.updateListView(wrapPhoneBookList(callList));
    }

    private ArrayList<MyPhoneBook> wrapPhoneBookList(ArrayList<PhoneBook> callList) {
        ArrayList<MyPhoneBook> phoneCalls = null;
        if (callList != null) {
            phoneCalls = new ArrayList<MyPhoneBook>();
            for (PhoneBook call : callList) {
                MyPhoneBook myCall = new MyPhoneBook(call);
                phoneCalls.add(myCall);
            }
        }
        return phoneCalls;
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            sortModel.setChecked(false);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();/**后续要把号码也拼接上，统一搜索*/
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
//        Collections.sort(filterDateList, pinyinComparator);
//        adapter.updateListView(filterDateList);
    }

    /**
     * 管理显示隐藏
     */
    public void managerView(Boolean aBoolean) {
        if (aBoolean) {
            btnEditLinkman.setVisibility(View.GONE);
            linkmanSearch.setVisibility(View.GONE);
            linkmanSidrbar.setVisibility(View.GONE);
            linkmanTvAll.setVisibility(View.VISIBLE);
            linkmanCbAll.setVisibility(View.VISIBLE);
            btnDeleteLinkman.setVisibility(View.VISIBLE);
            ivLinkmanDivide.setVisibility(View.VISIBLE);
            tvLinkmanDelete.setVisibility(View.VISIBLE);
        } else {
            btnEditLinkman.setVisibility(View.VISIBLE);
            linkmanSearch.setVisibility(View.VISIBLE);
            linkmanSidrbar.setVisibility(View.VISIBLE);
            linkmanTvAll.setVisibility(View.GONE);
            linkmanCbAll.setVisibility(View.GONE);
            btnDeleteLinkman.setVisibility(View.GONE);
            ivLinkmanDivide.setVisibility(View.INVISIBLE);
            tvLinkmanDelete.setVisibility(View.GONE);
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
