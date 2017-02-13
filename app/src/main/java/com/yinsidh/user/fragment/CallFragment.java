package com.yinsidh.user.fragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.adapter.MyCallLogAdapter;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.application.MyApplication;
import com.yinsidh.bean.CallLogBean;
import com.yinsidh.bean.CommonData;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.OnChildItemClickListener;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.user.fragment2.CallBackWaitActivity;
import com.yinsidh.user.fragment2.CallSettingActivity;
import com.yinsidh.user.fragment2.RechargeActivity;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;


public class CallFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private static final int ERRORMESG = 1;
    private static final int ERRORMONEY = 2;
    private static final int ERRORLOGIN = 3;
    private View rootView;
    private EditText callNumber; //显示号码的输入框
    private ImageButton num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, hint_keyBoard, num_0, del_key; //键盘上的按键
    public LinearLayout call_layout;  //拨打电话按钮布局
    public LinearLayout Keyboard_layout; //  键盘布局
    private MyAsyncQueryHandler asyncQuery;
    private List<CallLogBean> callLogs;
    private RecyclerView recyclerView;
    private MyCallLogAdapter adapter; //适配器
    private Gson gson;

    @Override
    public void onResume() {
        super.onResume();
        PackageManager pm = getActivity().getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_CALL_LOG", "com.yinsidh.android"));
        if (permission) {
            asyncQuery = new MyAsyncQueryHandler(getActivity().getContentResolver());
            init();
        } else {
            ToastHelper.showToast(getActivity(), "请同意软件的权限，才能继续提供服务");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fg1, container, false);
        }
        initView(rootView);
        //使号码输入框失去焦点
        callNumber.setInputType(InputType.TYPE_NULL);
        callNumber.clearFocus();
        //未各按钮设置点击事件
        setListener();

        return rootView;
    }

    private void setListener() {
        num_0.setOnClickListener(this);
        num_1.setOnClickListener(this);
        num_2.setOnClickListener(this);
        num_3.setOnClickListener(this);
        num_4.setOnClickListener(this);
        num_5.setOnClickListener(this);
        num_6.setOnClickListener(this);
        num_7.setOnClickListener(this);
        num_8.setOnClickListener(this);
        num_9.setOnClickListener(this);
        hint_keyBoard.setOnClickListener(this);
        del_key.setOnClickListener(this);
        del_key.setOnLongClickListener(this);
        call_layout.setOnClickListener(this);
    }

    //初始化查询方法
    public void init() {
        Uri uri = CallLog.Calls.CONTENT_URI;
        // 查询的列
        String[] projection = {CallLog.Calls.DATE, // 日期
                CallLog.Calls.NUMBER, // 号码
                CallLog.Calls.TYPE, // 类型
                CallLog.Calls.CACHED_NAME, // 名字
                CallLog.Calls._ID, // id
        };
        asyncQuery.startQuery(0, null, uri, projection, null, null,
                CallLog.Calls.DEFAULT_SORT_ORDER);
    }

    //对象的初始化
    private void initView(View view) {

        callNumber = (EditText) view.findViewById(R.id.callnumber);
        num_1 = (ImageButton) view.findViewById(R.id.button_key1);
        num_2 = (ImageButton) view.findViewById(R.id.button_key2);
        num_3 = (ImageButton) view.findViewById(R.id.button_key3);
        num_4 = (ImageButton) view.findViewById(R.id.button_key4);
        num_5 = (ImageButton) view.findViewById(R.id.button_key5);
        num_6 = (ImageButton) view.findViewById(R.id.button_key6);
        num_7 = (ImageButton) view.findViewById(R.id.button_key7);
        num_8 = (ImageButton) view.findViewById(R.id.button_key8);
        num_9 = (ImageButton) view.findViewById(R.id.button_key9);
        hint_keyBoard = (ImageButton) view.findViewById(R.id.hint_keyBoard);
        num_0 = (ImageButton) view.findViewById(R.id.button_key11);
        Keyboard_layout = (LinearLayout) view.findViewById(R.id.keyboard_layout);
        del_key = (ImageButton) view.findViewById(R.id.button_delKey);
        recyclerView = (RecyclerView) view.findViewById(R.id.record_recyclerView);

        call_layout = (LinearLayout) getActivity().findViewById(R.id.LinearLayout_main_bottom_call);
        gson = new Gson();


    }

    //各按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //按键1
            case R.id.button_key1:
                callNumber.setText(callNumber.getText() + "1");
                iscallphonelen();
                break;
            //按键2
            case R.id.button_key2:
                callNumber.setText(callNumber.getText() + "2");
                iscallphonelen();
                break;
            //按键3
            case R.id.button_key3:
                callNumber.setText(callNumber.getText() + "3");
                iscallphonelen();
                break;
            //按键4
            case R.id.button_key4:
                callNumber.setText(callNumber.getText() + "4");
                iscallphonelen();
                break;
            //按键5
            case R.id.button_key5:
                callNumber.setText(callNumber.getText() + "5");
                iscallphonelen();
                break;
            //按键6
            case R.id.button_key6:
                callNumber.setText(callNumber.getText() + "6");
                iscallphonelen();
                break;
            //按键7
            case R.id.button_key7:
                callNumber.setText(callNumber.getText() + "7");
                iscallphonelen();
                break;
            //按键8
            case R.id.button_key8:
                callNumber.setText(callNumber.getText() + "8");
                iscallphonelen();
                break;
            //按键9
            case R.id.button_key9:
                callNumber.setText(callNumber.getText() + "9");
                iscallphonelen();
                break;
            //隐藏键盘
            case R.id.hint_keyBoard:
                Intent intent = new Intent(getActivity(), CallSettingActivity.class);
                startActivity(intent);
                break;
            //按键0
            case R.id.button_key11:
                callNumber.setText(callNumber.getText() + "0");
                iscallphonelen();
                break;
            //号码删除（一个一个删除）
            case R.id.button_delKey:
                int i = callNumber.getText().length();
                if (i > 0) {
                    String a = callNumber.getText().toString().substring(0, i - 1);
                    callNumber.setText(a);
                }
                iscallphonelen();
                break;
            //拨打电话
            case R.id.LinearLayout_main_bottom_call:
                MsgWaitDialog.waitdialog(getActivity());
                if (!SharedPrefsUtil.getValue(getActivity(), "checkcall", "").equals("call")) {
                    Call_Phone();
                } else {
//                    Enhance_Phone();
                }
                break;
            default:
                break;
        }
        callNumber.setSelection(callNumber.getText().length());
    }

    private void Call_Phone() {
        new VolleyRequestUtil().RequestPost(getActivity(), URL.HOST, "callback", getCallbackParams(), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    MsgWaitDialog.waitdialog_close();
                    CommonData call = gson.fromJson(result, CommonData.class);
                    Message msg = handler.obtainMessage();
                    Log.e("tag", call.toString());
                    if (call.getStats().equals("ok")) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), CallBackWaitActivity.class);
                        intent.putExtra("number", callNumber.getText().toString());
                        startActivity(intent);

                    } else if (call.getStats().equals("errormsg")) {
                        msg.what = ERRORMESG;
                        msg.obj = call.getMsg();
                    } else if (call.getStats().equals("errormoney")) {
                        msg.what = ERRORMONEY;
                        msg.obj = call.getMsg();

                    } else if (call.getStats().equals("errorlogin")) {
                        msg.what = ERRORLOGIN;
                        msg.obj = call.getMsg();
                    }
                    handler.sendMessage(msg);
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                showToast("拨打电话功能暂时无法使用！");
            }
        });
    }

    //判断是否显示拨打电话按钮
    private void iscallphonelen() {
        String number = callNumber.getText().toString();
        if (number.length() > 0) {
            call_layout.setVisibility(View.VISIBLE);
        } else {
            call_layout.setVisibility(View.GONE);
        }
    }

    //长按删除键，清空号码
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.button_delKey:
                callNumber.setText("");
                iscallphonelen();
                break;
        }
        return false;
    }

    //自定义异步帮助类，用来获取手机通话记录
    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                callLogs = new ArrayList<CallLogBean>();
                SimpleDateFormat sfd = new SimpleDateFormat("MM-dd hh:mm");
                Date date;
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    date = new Date(cursor.getLong(cursor
                            .getColumnIndex(CallLog.Calls.DATE)));
                    String number = cursor.getString(cursor
                            .getColumnIndex(CallLog.Calls.NUMBER));
                    int type = cursor.getInt(cursor
                            .getColumnIndex(CallLog.Calls.TYPE));
                    String cachedName = cursor.getString(cursor
                            .getColumnIndex(CallLog.Calls.CACHED_NAME));// 缓存的名称与电话号码，如果它的存在
                    int id = cursor.getInt(cursor
                            .getColumnIndex(CallLog.Calls._ID));

                    CallLogBean callLogBean = new CallLogBean();
                    callLogBean.setId(id);
                    callLogBean.setNumber(number);
                    callLogBean.setName(cachedName);
                    if (null == cachedName || "".equals(cachedName)) {
                        callLogBean.setName(number);
                    }
                    callLogBean.setType(type);
                    callLogBean.setDate(sfd.format(date));

                    callLogs.add(callLogBean);

                }
                Log.e("tag", "长度" + callLogs.size());
                if (callLogs.size() > 0) {
                    setAdapter(callLogs);
                    cursor.close();
                } else {
                    showToast("未检测到通话记录！");
                }
                cursor.close();
            }
            super.onQueryComplete(token, cookie, cursor);

        }
    }

    //设置recyclerView适配器
    private void setAdapter(final List<CallLogBean> callLogs) {

        //条目间的间隔线
        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
                .setHeight(1.0f)
                .setPadding(0.0f)
                .setColorResource(R.color.gray)
                .build();
        recyclerView.addItemDecoration(divider);
        adapter = new MyCallLogAdapter(callLogs, getActivity());
        //未通话记录设置点击事件，当用户点击通话记录，如果键盘为隐藏状态则设置键盘显示
        adapter.setOnItemListener(new OnChildItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callNumber.setText(callLogs.get(position).getNumber());
                iscallphonelen();
                if (Keyboard_layout.getVisibility() == View.GONE) {
                    Animation animation2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 1,
                            Animation.RELATIVE_TO_PARENT, 0);
                    animation2.setDuration(500);
                    animation2.setInterpolator(AnimationUtils
                            .loadInterpolator(getActivity(),
                                    android.R.anim.accelerate_decelerate_interpolator));
                    Keyboard_layout.startAnimation(animation2);
                    Keyboard_layout.setVisibility(View.VISIBLE);
                    IndexActivity.isShowKeyboar = false;
                    IndexActivity.isShowAnimation = false;
                }

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    public Map<String, String> getCallbackParams() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "callback");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        params.put("phone", callNumber.getText().toString());
        return params;
    }

    public void showToast(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getRequestQueue().cancelAll(this);
        if (callLogs != null) {
            callLogs.clear();
            callLogs = null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ERRORMESG:
                    MyDialog.setOneDialog(getActivity(), "提示", (String) msg.obj, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case ERRORMONEY:
                    MyDialog.setDialog(getActivity(), "提示", (String) msg.obj, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(getActivity(), RechargeActivity.class);
                            startActivity(intent);
                        }
                    }, "继续拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callNumber.getText().toString()));
                            startActivity(intent);
                        }
                    });
                    break;
                case ERRORLOGIN:
                    MyDialog.setDialog(getActivity(), "注意", (String) msg.obj, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            SharedPrefsUtil.clearValue(getActivity());
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }
    };

}
