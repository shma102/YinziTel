package com.yinsidh.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.application.MyApplication;
import com.yinsidh.bean.CommonData;
import com.yinsidh.bean.User;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.NetworkUtils;
import com.yinsidh.utils.RegularUtils;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class RegisterMessActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_getCode; //获取验证码按钮
    private TimeCount timer;
    private EditText user_mobile, user_pwd, user_promotion, user_validation;
    private ImageView iv_back;
    private TextView btn_register;

    private String imei;
    private Gson gson;
    private TextWatcher mTextWatch;

    private TextView intoQQ;
    private boolean aBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_register_mess);

        initView();

        /**
         * 监听手机号的长度
         * 只有输入11位数字才能点击获取验证码按钮
         */
        mTextWatch = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String username = user_mobile.getText().toString();
                if (!aBoolean) {
                    if ((username.length() >= 0 && username.length() < 11) || (username.length() > 11)) {
                        unclick_btn();
                    } else {
                        click_btn();
                    }
                }
            }
        };
        btn_getCode.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        intoQQ.setOnClickListener(this);
        user_mobile.addTextChangedListener(mTextWatch);
    }

    private void initView() {
        btn_getCode = (Button) findViewById(R.id.getCode);
        user_mobile = (EditText) findViewById(R.id.user_mobile);
        user_pwd = (EditText) findViewById(R.id.user_pwd);
        user_promotion = (EditText) findViewById(R.id.user_promotion);
        user_validation = (EditText) findViewById(R.id.user_validation);
        iv_back = (ImageView) findViewById(R.id.register_back);
        btn_register = (TextView) findViewById(R.id.btn_regist);
        intoQQ = (TextView) findViewById(R.id.tv_intoQQ);

        timer = new TimeCount(60000, 1000);//构造CountDownTimer对象
        imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId(); //获取设备ID
        gson = new Gson();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getCode:

                MsgWaitDialog.waitdialog(this);
                new VolleyRequestUtil().RequestPost(this, URL.HOST, "regsms", regsmsParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        Log.e("tag", result.toString());
                        MsgWaitDialog.waitdialog_close();
                        CommonData codeGet = gson.fromJson(result, CommonData.class);
                        if (codeGet.getStats().equals("ok")) {
                            timer.start();//开始计时
                            MyDialog.setOneDialog(RegisterMessActivity.this, "提示", codeGet.getMsg(), "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            MyDialog.setOneDialog(RegisterMessActivity.this, "提示", codeGet.getMsg(), "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        MsgWaitDialog.waitdialog_close();
                        ToastHelper.showToast(RegisterMessActivity.this, "网络连接错误！");
                        click_btn();
                    }
                });
                break;
            case R.id.register_back:
                RegisterMessActivity.this.finish();
                break;
            case R.id.btn_regist:
                String mobile = user_mobile.getText().toString().trim();
                String password = user_pwd.getText().toString().trim();
                String validation = user_validation.getText().toString().trim();
                if (RegularUtils.isEmail(mobile) || RegularUtils.isEmail(password) ||
                        RegularUtils.isEmail(validation)) {
                    MyDialog.setOneDialog(RegisterMessActivity.this, "提示", "请完善您的信息", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return;
                }
                if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(mobile)) {
                    MyDialog.setOneDialog(this, "提示", "您用户名或密码为空，请重新输入", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return;
                }
                if (mobile.length() < 5 || mobile.length() > 18) {
                    MyDialog.setOneDialog(this, "提示", "用户名应为5到18位数字，请重新输入", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return;
                }
                if (password.length() < 5 || password.length() > 12) {
                    MyDialog.setOneDialog(RegisterMessActivity.this, "提示", "密码应为5到12位数字或字母，请重新输入!", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return;
                }
                if (NetworkUtils.isNetworkAvailable(RegisterMessActivity.this)) {
                    Register_Http();
                } else {
                    ToastHelper.showToast(RegisterMessActivity.this, "当前无网络连接！");
                }
                break;
            case R.id.tv_intoQQ:
                if (checkApkExist(this, "com.yinsidh.android")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=800123152&version=1")));
                } else {
                    Toast.makeText(this, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void Register_Http() {
        new VolleyRequestUtil().RequestPost(this, URL.HOST, "reg", regParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                User user = gson.fromJson(result, User.class);
                if (user.getStats().equals("ok")) {
                    //注册成功
                    ToastHelper.showToast(RegisterMessActivity.this, "注册成功");
                    Intent intent = new Intent(RegisterMessActivity.this, LoginActivity.class);
                    intent.putExtra("username", user.getUsername());
                    intent.putExtra("password", user_pwd.getText().toString());
                    startActivity(intent);
                    RegisterMessActivity.this.finish();
                } else if (user.getStats().equals("errormsg")) {
                    MyDialog.setDialog(RegisterMessActivity.this, "提示", user.getMsg(), "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                //请求失败
                ToastHelper.showToast(RegisterMessActivity.this, "注册失败！");
            }
        });
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            click_btn();
            aBoolean = false;
        }

        @SuppressLint("NewApi")
        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btn_getCode.setText(millisUntilFinished / 1000 + "秒");
            unclick_btn();
            aBoolean = true;
        }
    }

    //获取验证码按钮 可点击状态
    public void click_btn() {
        btn_getCode.setText("获取");
        btn_getCode.setEnabled(true);
        btn_getCode.setBackgroundResource(R.drawable.login_selector5);
    }

    //获取验证码按钮 不可点击状态
    public void unclick_btn() {
        btn_getCode.setBackgroundResource(R.drawable.logi);
        btn_getCode.setEnabled(false);
    }

    //获取验证码接口的参数
    public Map<String, String> regsmsParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "regsms");
        params.put("username", user_mobile.getText().toString());
        params.put("system", "fef730401cdfbd19145f94cb28c1bc1e");
        return params;
    }

    //注册接口的参数
    public Map<String, String> regParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "reg");
        params.put("username", user_mobile.getText().toString());
        params.put("userpass", user_pwd.getText().toString());
        params.put("userdistr", user_promotion.getText().toString());
        params.put("imei", imei);
        params.put("vericode", user_validation.getText().toString());
        return params;
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消队列里的所有请求
        MyApplication.getRequestQueue().cancelAll(this);
    }
}
