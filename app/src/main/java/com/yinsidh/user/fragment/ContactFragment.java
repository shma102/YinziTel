package com.yinsidh.user.fragment;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yinsidh.adapter.ContactListAdapter;
import com.yinsidh.android.R;
import com.yinsidh.bean.ContactBean;
import com.yinsidh.helper.OnNumberItemClickListener;
import com.yinsidh.helper.QuickIndexBar;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;

public class ContactFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象

    private List<ContactBean> contacts;
    private Map<Integer, ContactBean> contactIdMap = null;  //查询通讯录
    private QuickIndexBar QIBar;  //右侧索引
    private LinearLayoutManager manager;
    private RecyclerView recyclerView;
    private ContactListAdapter adapter;
    private TextWatcher textWatch;
    private EditText et_search;
    private ImageView ivClearText;
    private TextView refresh_contact;
    private int listSize = 0;  //联系人集合的大小
    private List<ContactBean> lists;
    private CallFragment callFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fg2, container, false);
        }
        PackageManager pm = getActivity().getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_CONTACTS", "com.yinsidh.android"));

        initView(rootView);
        initDate();
        //判断搜索框输入情况
        textWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_str = et_search.getText().toString();
                if (!search_str.equals("")) {
                    ivClearText.setVisibility(View.VISIBLE);
                } else {
                    ivClearText.setVisibility(View.INVISIBLE);
                }
                if (contacts.size() > 0 && contacts != null) {
                    contacts.clear();
                }
                for (int i = 0; i < lists.size(); i++) {
                    ContactBean bean = lists.get(i);
                    if (bean.getName().contains(search_str) || bean.getNumber().contains(search_str)) {
                        contacts.add(bean);
                    }
                }
                if (contacts.size() <= 0) {
                    ToastHelper.showToast(getActivity(), "条目为空");
                }
                adapter.updateRecyclerView(contacts);
                Log.e("search_contacts", contacts.size() + "");
                recyclerView.setAdapter(adapter);
            }
        };
        if (permission) {
            asyncQueryHandler = new MyAsyncQueryHandler(getActivity().getContentResolver());
            init();
            et_search.addTextChangedListener(textWatch);
            refresh_contact.setOnClickListener(this);

        } else {
            ToastHelper.showToast(getActivity(), "请同意软件的权限，才能继续提供服务");
        }


        ivClearText.setOnClickListener(this);

        return rootView;
    }

    private void initDate() {
        recyclerView.setHasFixedSize(true); //确保item改变时recyclerView尺寸不变
        recyclerView.setLayoutManager(manager);
    }

    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById((R.id.id_recyclerview));
        QIBar = (QuickIndexBar) rootView.findViewById(R.id.indexBar);
        et_search = (EditText) rootView.findViewById(R.id.et_search);
        ivClearText = (ImageView) rootView.findViewById(R.id.ivClearText);
        refresh_contact = (TextView) rootView.findViewById(R.id.refresh_contact);

        lists = new ArrayList<ContactBean>();
        contacts = new ArrayList<ContactBean>();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        callFragment = new CallFragment();
    }

    /**
     * 初始化查询参数
     */
    private void init() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = {ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY};
        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //搜索框清空按钮
            case R.id.ivClearText:
                et_search.setText("");
                break;
            //刷新按钮
            case R.id.refresh_contact:
                if (et_search.getText().toString().length() > 0) {
                    et_search.setText("");
                } else {
                    contacts.clear();
                    lists.clear();
                    asyncQueryHandler = new MyAsyncQueryHandler(getActivity().getContentResolver());
                    init();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 异步的查询操作帮助类（帮助查询联系人）
     */
    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

            if (lists.size() > 0 && lists != null) {
                lists.clear();
            }
            if (contacts.size() > 0 && contacts != null) {
                contacts.clear();
            }
            if (cursor != null && cursor.getCount() > 0) {

                contactIdMap = new HashMap<Integer, ContactBean>();
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    int contactId = cursor.getInt(4);

                    if (contactIdMap.containsKey(contactId)) {
                        // 无操作
                    } else {
                        // 创建联系人对象
                        ContactBean contact = new ContactBean(name, number);
                        lists.add(contact);
                        contactIdMap.put(contactId, contact);
                    }
                }
                for (int i = 0; i < lists.size(); i++) {
                    contacts.add(lists.get(i));
                }
                if ((contacts.size() > 0) && (listSize != lists.size())) {
                    listSize = contacts.size();
                    Log.e("tag", "listSize" + lists.size());
                    setAdapter(lists);
                }
            }
            super.onQueryComplete(token, cookie, cursor);
            cursor.close();
        }

    }

    private void setAdapter(final List<ContactBean> list) {
        //对联系人集合进行排序
        Collections.sort(list, new Comparator<ContactBean>() {
            @Override
            public int compare(ContactBean lhs, ContactBean rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });
        //条目间的间隔线
        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();

        recyclerView.addItemDecoration(divider); //为recyclerView添加间隔线

        adapter = new ContactListAdapter(getActivity(), lists);
        adapter.setOnItemClickListener(new OnNumberItemClickListener() {
            @Override
            public void onNumberClick(View view, String string) {
                /**
                 * 点击联系人跳转到拨号页面原理：
                 * 1.调用IndexActivity。class的switchFragment方法将页面设置为拨号页面
                 * 2.在联系人页面获取到Activity对象的EditText，并直接为其赋值
                 * 3.最后显示拨打电话按钮
                 */
                String number = string;
                ((IndexActivity) getActivity()).switchFragment(0);
                EditText call_number = (EditText) getActivity().findViewById(R.id.callnumber);
                call_number.setText(number);
                getActivity().findViewById(R.id.keyboard_layout).setVisibility(View.VISIBLE);
                ((IndexActivity) getActivity()).isShowKeyboar = true;
                getActivity().findViewById(R.id.LinearLayout_main_bottom_call).setVisibility(View.VISIBLE);
                KeyboardUtils.hideSoftInput(getActivity(), et_search);

            }
        });

        adapter.updateRecyclerView(list);  //刷新通讯录
        recyclerView.setAdapter(adapter);

        //设置右侧索引栏
        QIBar.setOnTouchingLetterChangedListener(new QuickIndexBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Log.e("tag", "点击的字母" + s.toLowerCase());
                int position = adapter.getPositionForSection(s.toCharArray()[0]);
                if (list.size() > 0) { //判断联系人是否为空
                    if (position != -1) {
                        //滑动到指定位置
                        manager.scrollToPositionWithOffset(position, 0);
                    }
                    if (s.toLowerCase().equals("#")) {
                        manager.scrollToPositionWithOffset(0, 0);
                    }
                }
            }
        }, list);
    }


}
