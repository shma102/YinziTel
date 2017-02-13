package com.yinsidh.user.fragment2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.application.MyApplication;
import com.yinsidh.bean.CommonData;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.KeyboardUtils;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class CallSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CALL = 0;
    private static final int VOICE = 1;

    private RelativeLayout mCallSettingVoice;
    private RelativeLayout mCallSettingNumber;
    private RelativeLayout mCallSettingLevel;
    private RelativeLayout mCallSettingRecharge;
    private TextView mCallSettingBack;

    private PopupWindow callPopupWindow;
    private TextView callTitleBack;
    private WindowManager manager;
    private RadioButton benji, suiji, ziding;
    private EditText zidingNumber;
    private Button call_submit;
    private String callId;

    private PopupWindow voicePopupWindow;
    private TextView voiceTitleBack;
    private RadioButton myself, woman, man, child, old, machine;
    private Button voiceSubmit;
    private String voiceId;

    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_call_setting2);
        initView();

        addListener();
    }

    private void addListener() {
        mCallSettingVoice.setOnClickListener(this);
        mCallSettingNumber.setOnClickListener(this);
        mCallSettingLevel.setOnClickListener(this);
        mCallSettingRecharge.setOnClickListener(this);
        mCallSettingBack.setOnClickListener(this);
    }

    private void initView() {
        mCallSettingVoice = (RelativeLayout) findViewById(R.id.call_setting_voice);
        mCallSettingNumber = (RelativeLayout) findViewById(R.id.call_setting_number);
        mCallSettingLevel = (RelativeLayout) findViewById(R.id.call_setting_level);
        mCallSettingRecharge = (RelativeLayout) findViewById(R.id.call_setting_recharge);
        mCallSettingBack = (TextView) findViewById(R.id.call_setting_back);

        manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        gson = new Gson();

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.call_setting_voice:
                showVoicePopupWindow(v);
                break;
            case R.id.call_setting_number:
                MsgWaitDialog.waitdialog(this);
                VolleyRequestUtil.RequestPost(this, URL.HOST, "checklevel", getCheckLevelParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        MsgWaitDialog.waitdialog_close();
                        CommonData commonData = gson.fromJson(result, CommonData.class);
                        if (commonData.getStats().equals("supermode")) {
                            showCallPopupWindow(v);
                        } else if (commonData.getStats().equals("errorlogin")) {
                            DidExit(commonData.getMsg());

                        } else if (commonData.getStats().equals("normalmode")) {
                            ziding.setVisibility(View.GONE);
                            showCallPopupWindow(v);
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        MsgWaitDialog.waitdialog_close();
                        ToastHelper.showToast(CallSettingActivity.this, "网络出错了！");
                    }
                });

                break;
            case R.id.call_setting_level:
                Intent intent_jibie = new Intent(this, LevelQueryActivity.class);
                startActivity(intent_jibie);
                break;
            case R.id.call_setting_recharge:
                Intent intent_jifen = new Intent(this, RechargeActivity.class);
                startActivity(intent_jifen);
                break;
            case R.id.call_setting_back:
                CallSettingActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private Map<String, String> getCheckLevelParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "checklevel");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(this, "userpass", ""));
        return params;
    }

    private void Voice_Http() {
        MsgWaitDialog.waitdialog(CallSettingActivity.this);

        VolleyRequestUtil.RequestPost(CallSettingActivity.this, URL.HOST, "setvoice", getVoiceParams(), new VolleyListenerInterface(CallSettingActivity.this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                if (!TextUtils.isEmpty(result)) {
                    CommonData commonData = gson.fromJson(result, CommonData.class);
                    if (commonData.getStats().equals("ok")) {
                        Message message = new Message();
                        message.obj = commonData;
                        message.what = VOICE;
                        mHandler.sendMessage(message);
                    } else if (commonData.getStats().equals("errorlogin")) {
                        DidExit(commonData.getMsg());
                    } else if (commonData.getStats().equals("errormsg")) {
                        setErrorMess(commonData.getMsg());
                    }
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                ToastHelper.showToast(CallSettingActivity.this, "网络出错了");
            }

        });
    }

    private Map<String, String> getVoiceParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "setvoice");
        params.put("username", SharedPrefsUtil.getValue(CallSettingActivity.this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(CallSettingActivity.this, "userpass", ""));
        params.put("uservoice", voiceId);
        return params;
    }

    private void showVoicePopupWindow(View v) {
        if (voicePopupWindow == null) {
            View view = LayoutInflater.from(CallSettingActivity.this).inflate(R.layout.activity_voice_setting, null, false);
            voiceTitleBack = (TextView) view.findViewById(R.id.voice_title_back);
            voiceSubmit = (Button) view.findViewById(R.id.voice_submit);
            myself = (RadioButton) view.findViewById(R.id.voice_myself);
            woman = (RadioButton) view.findViewById(R.id.voice_woman);
            man = (RadioButton) view.findViewById(R.id.voice_man);
            child = (RadioButton) view.findViewById(R.id.voice_child);
            old = (RadioButton) view.findViewById(R.id.voice_old);
            machine = (RadioButton) view.findViewById(R.id.voice_machine);

            int width = (int) (manager.getDefaultDisplay().getWidth() * (0.7));
            voicePopupWindow = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        //设置popupwindow可点击
        voicePopupWindow.setFocusable(true);
        //设置允许在外部点消失
        voicePopupWindow.setOutsideTouchable(false);
        //设置弹出和消失动画
        voicePopupWindow.setAnimationStyle(R.style.AnimationMy);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = CallSettingActivity.this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        CallSettingActivity.this.getWindow().setAttributes(lp);
        voicePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = CallSettingActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                CallSettingActivity.this.getWindow().setAttributes(lp);
            }
        });
        voicePopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        //退出当前popupWindow
        voiceTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(CallSettingActivity.this);
                voicePopupWindow.dismiss();

            }
        });
        //自己的声音
        myself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                myself.setChecked(true);

            }
        });
        //女士的声音
        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                woman.setChecked(true);

            }
        });
        //男士的声音
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                man.setChecked(true);

            }
        });
        //小孩的声音
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                child.setChecked(true);

            }
        });
        //老人的声音
        old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                old.setChecked(true);

            }
        });
        //机器的声音
        machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                machine.setChecked(true);

            }
        });
        //提交
        voiceSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myself.isChecked() || woman.isChecked() || man.isChecked() || old.isChecked() || child.isChecked() || machine.isChecked()) {
                    if (myself.isChecked()) {
                        voiceId = "0";
                    }
                    if (woman.isChecked()) {
                        voiceId = "1";
                    }
                    if (man.isChecked()) {
                        voiceId = "2";
                    }
                    if (child.isChecked()) {
                        voiceId = "3";
                    }
                    if (old.isChecked()) {
                        voiceId = "4";
                    }
                    if (machine.isChecked()) {
                        voiceId = "5";
                    }
                    Voice_Http();
                } else {
                    MyDialog.setOneDialog(CallSettingActivity.this, "提示", "请选择变音", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

    }

    private void unclickVoiceRadioButton() {
        myself.setChecked(false);
        woman.setChecked(false);
        man.setChecked(false);
        child.setChecked(false);
        old.setChecked(false);
        machine.setChecked(false);
    }

    private void Number_Http() {
        VolleyRequestUtil.RequestPost(CallSettingActivity.this, URL.HOST, "setcallerid", getCallerIdParams(), new VolleyListenerInterface(CallSettingActivity.this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                if (!TextUtils.isEmpty(result)) {
                    CommonData commonData = gson.fromJson(result, CommonData.class);
                    if (commonData.getStats().equals("ok")) {
                        Message message = new Message();
                        message.obj = commonData;
                        message.what = CALL;
                        mHandler.sendMessage(message);
                    } else if (commonData.getStats().equals("errorlogin")) {
                        DidExit(commonData.getMsg());

                    } else if (commonData.getStats().equals("errormsg")) {
                        setErrorMess(commonData.getMsg());
                    }
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                ToastHelper.showToast(CallSettingActivity.this, "网络出错了");
            }

        });
    }

    private Map<String, String> getCallerIdParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "setcallerid");
        params.put("username", SharedPrefsUtil.getValue(CallSettingActivity.this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(CallSettingActivity.this, "userpass", ""));
        params.put("callerid", callId);
        return params;
    }

    private void showCallPopupWindow(View v) {
        if (callPopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.activity_call_setting, null, false);
            callTitleBack = (TextView) view.findViewById(R.id.button_title_back);
            benji = (RadioButton) view.findViewById(R.id.benji);
            suiji = (RadioButton) view.findViewById(R.id.suiji);
            ziding = (RadioButton) view.findViewById(R.id.ziding);
            zidingNumber = (EditText) view.findViewById(R.id.haoma);
            call_submit = (Button) view.findViewById(R.id.setting_submit);
            int width = (int) (manager.getDefaultDisplay().getWidth() * (0.7));
            callPopupWindow = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        callPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        callPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow可点击
        callPopupWindow.setFocusable(true);
        //设置允许在外部点消失
        callPopupWindow.setOutsideTouchable(false);
        //设置弹出和消失动画
        callPopupWindow.setAnimationStyle(R.style.AnimationMy);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = CallSettingActivity.this.getWindow().getAttributes();
        lp.alpha = 0.7f;
        CallSettingActivity.this.getWindow().setAttributes(lp);
        callPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                KeyboardUtils.hideSoftInput(CallSettingActivity.this, zidingNumber);
                WindowManager.LayoutParams lp = CallSettingActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                CallSettingActivity.this.getWindow().setAttributes(lp);
            }
        });
        callPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        //退出当前popupWindow
        callTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(CallSettingActivity.this, zidingNumber);
                callPopupWindow.dismiss();

            }
        });
        //本机号码
        benji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickCallRadioButton();
                benji.setChecked(true);
                callId = "callerid";
            }
        });
        //随机号码
        suiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickCallRadioButton();
                suiji.setChecked(true);
                callId = "";
            }
        });
        //自定号码
        ziding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickCallRadioButton();
                ziding.setChecked(true);
                zidingNumber.setVisibility(View.VISIBLE);
            }
        });
        //提交
        call_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否点击变号按钮
                if (benji.isChecked() || suiji.isChecked() || ziding.isChecked()) {
                    //如果用户点击的是自定义，则判断是否填入号码
                    if (ziding.isChecked()) {
                        if (TextUtils.isEmpty(zidingNumber.getText().toString())) {
                            MyDialog.setOneDialog(CallSettingActivity.this, "提示", "请输入自定义号码", "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            callId = zidingNumber.getText().toString();
                            MsgWaitDialog.waitdialog(CallSettingActivity.this);
                            Number_Http();
                        }
                    } else {
                        MsgWaitDialog.waitdialog(CallSettingActivity.this);
                        Number_Http();
                    }
                } else {
                    MyDialog.setOneDialog(CallSettingActivity.this, "提示", "您未做任何选择", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }


            }
        });
    }

    private void unclickCallRadioButton() {
        suiji.setChecked(false);
        ziding.setChecked(false);
        benji.setChecked(false);
        zidingNumber.setVisibility(View.INVISIBLE);
    }

    public void setErrorMess(String s) {
        MyDialog.setOneDialog(CallSettingActivity.this, "提示", s, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CALL:
                    callPopupWindow.dismiss();
                    String stats = ((CommonData) msg.obj).getStats();
                    String mess = ((CommonData) msg.obj).getMsg();
                    if (stats.equals("errorlogin")) {
                        DidExit(mess);

                    } else if (stats.equals("errormsg")) {
                        MyDialog.setOneDialog(CallSettingActivity.this, "提示", mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    } else if (stats.equals("ok")) {
                        MyDialog.setOneDialog(CallSettingActivity.this, "提示", mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    }
                    break;
                case VOICE:
                    voicePopupWindow.dismiss();
                    String voice_stats = ((CommonData) msg.obj).getStats();
                    String voice_mess = ((CommonData) msg.obj).getMsg();
                    if (voice_stats.equals("errorlogin")) {
                        DidExit(voice_mess);
                    } else if (voice_stats.equals("errormsg")) {
                        MyDialog.setOneDialog(CallSettingActivity.this, "提示", voice_mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    } else if (voice_stats.equals("ok")) {
                        MyDialog.setOneDialog(CallSettingActivity.this, "提示", voice_mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    }
                    break;
            }
        }
    };

    private void DidExit(String s) {
        MyDialog.setDialog(CallSettingActivity.this, "提示", s, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPrefsUtil.clearValue(CallSettingActivity.this);

                Intent intent = new Intent(CallSettingActivity.this, LoginActivity.class);
                startActivity(intent);
                CallSettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (callPopupWindow != null && callPopupWindow.isShowing()) {
            callPopupWindow.dismiss();
            callPopupWindow = null;

        }
        if (voicePopupWindow != null && voicePopupWindow.isShowing()) {
            voicePopupWindow.dismiss();
            voicePopupWindow = null;

        }
        MyApplication.getRequestQueue().cancelAll(CallSettingActivity.this);
    }
}
