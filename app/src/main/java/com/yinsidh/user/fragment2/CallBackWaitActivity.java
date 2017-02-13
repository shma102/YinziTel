package com.yinsidh.user.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.bean.CommonData;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;


public class CallBackWaitActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ERRORMESG = 1;
    private static final int ERRORLOGIN = 2;
    private LinearLayout hang_layout;
    private TextView callback_callphone;
    private TextView callback_callstats;
    private String number = "";
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_call_back_wait);
        initView();
        initListener();

        number = getIntent().getStringExtra("number");
        if (!TextUtils.isEmpty(number)) {
            callback_callphone.setText(number);
            callback_callstats.setText("正在接通" + number + "，请注意接听来电...");
        }


    }

    private void initListener() {
        hang_layout.setOnClickListener(this);
    }

    private void initView() {
        hang_layout = (LinearLayout) findViewById(R.id.layout_hang);
        callback_callphone = (TextView) findViewById(R.id.callback_callphone);
        callback_callstats = (TextView) findViewById(R.id.callback_callstats);
        gson = new Gson();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_hang:
                Hangcall_Http();
                break;
            default:
                break;
        }

    }

    private void Hangcall_Http() {
        MsgWaitDialog.waitdialog(this);
        VolleyRequestUtil.RequestPost(this, URL.HOST, "hangupcall", getHangCallParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                if (!TextUtils.isEmpty(result)) {
                    CommonData data = gson.fromJson(result, CommonData.class);
                    Message msg = handler.obtainMessage();
                    if (data.getStats().equals("ok")) {
                        CallBackWaitActivity.this.finish();
                    } else if (data.getStats().equals("errormsg")) {
                        msg.what = ERRORMESG;
                        msg.obj = data.getMsg();

                    } else if (data.getStats().equals("errorlogin")) {
                        msg.what = ERRORLOGIN;
                        msg.obj = data.getMsg();
                    }
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                ToastHelper.showToast(CallBackWaitActivity.this, "网络出错了");
            }
        });
    }

    private Map<String, String> getHangCallParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "hangupcall");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(this, "userpass", ""));
        return params;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ERRORMESG:
                    MyDialog.setOneDialog(CallBackWaitActivity.this, "提示", (String) msg.obj, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case ERRORLOGIN:
                    MyDialog.setDialog(CallBackWaitActivity.this, "提示", (String) msg.obj, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },"确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPrefsUtil.clearValue(CallBackWaitActivity.this);
                            Intent intent = new Intent(CallBackWaitActivity.this, LoginActivity.class);
                            startActivity(intent);
                            CallBackWaitActivity.this.finish();
                        }
                    });

                    break;
            }
        }
    };
}
