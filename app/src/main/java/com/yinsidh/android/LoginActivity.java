package com.yinsidh.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.yinsidh.user.fragment.IndexActivity;
import com.yinsidh.utils.NetworkUtils;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login_btn;
    private EditText username, password;
    private TextView register_tv;
    private TextView forget_tv;
    private Gson gson;
    private String register_name, register_pwd; //从注册页面跳转过来得到的用户名和密码
    private boolean checkNetWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_login);
        initView();
        //注册页面跳转过来
        register_name = getIntent().getStringExtra("username");
        register_pwd = getIntent().getStringExtra("password");
        //如果是注册页面过来的，则直接将用户名和密码填入输入框
        if (register_name != null && register_pwd != null) {
            username.setText(register_name);
            password.setText(register_pwd);
        }

        login_btn.setOnClickListener(this);
        register_tv.setOnClickListener(this);
        forget_tv.setOnClickListener(this);

    }

    private void initView() {
        login_btn = (Button) findViewById(R.id.login_new_button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register_tv = (TextView) findViewById(R.id.zhuce);
        forget_tv = (TextView) findViewById(R.id.forgetPwd);

        gson = new Gson();
    }

    @Override
    public void onClick(View v) {
        String mUserName = username.getText().toString().trim();
        String mPassWord = password.getText().toString().trim();
        switch (v.getId()) {
            case R.id.login_new_button:
                //点击登陆按钮 进行文本框的判断
                if (TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassWord)) {
                    MyDialog.setOneDialog(this, "提示", "您用户名或密码为空，请重新输入", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return;
                }
                if (mUserName.length() < 5 || mUserName.length() > 18) {
                    MyDialog.setOneDialog(this, "提示", "用户名应为5到18位数字，请重新输入", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return;
                }
                if (mPassWord.length() < 5 || mPassWord.length() > 18) {
                    MyDialog.setOneDialog(this, "提示", "密码应为5到18位数字或字母，请重新输入", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    return;
                }
                if (mUserName.length() != 11) {
                    MyDialog.setOneDialog(LoginActivity.this, "提示", "请输入正确的账号", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    MsgWaitDialog.waitdialog(LoginActivity.this);
                    Login_Http();
                }
                break;
            case R.id.zhuce:
                //这里进入注册页面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forgetPwd:
                //这里进入找回密码页面
                if (username.getText().toString().length() != 11) {
                    MyDialog.setOneDialog(LoginActivity.this, "提示", "请输入正确的账号", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    getPassWord("你确定要找回密码么？");
                }
                break;
            default:
                break;
        }
    }


    //找回密码接口的参数
    public Map<String, String> getPwdParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "getpwd");
        params.put("username", username.getText().toString());
        return params;
    }

    //登陆接口的参数
    public Map<String, String> loginParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "login");
        params.put("username", username.getText().toString());
        params.put("userpass", password.getText().toString());
        params.put("version", "3.2.1");
        return params;
    }

    private void Login_Http() {
        new VolleyRequestUtil().RequestPost(this, URL.HOST, "login", loginParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                User user = gson.fromJson(result, User.class);
                if (user.getStats().equals("ok")) {
                    //跳转到主界面
                    ToastHelper.showToast(LoginActivity.this, user.getMsg());
                    saveUser(user);
                    Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();

                } else if (user.getStats().equals("errormsg")) {
                    //给用户提示错误
                    getPassWord(user.getMsg());
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(LoginActivity.this, "请检查网络后重试");

            }
        });
    }

    private void getPassWord(String string) {
        MyDialog.setDialog(this, "提示", string, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, "找回密码", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new VolleyRequestUtil().RequestPost(LoginActivity.this, URL.HOST, "getpwd", getPwdParams(), new VolleyListenerInterface(LoginActivity.this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        //请求成功
                        CommonData data = gson.fromJson(result, CommonData.class);
                        MyDialog.setOneDialog(LoginActivity.this, "提示", data.getMsg(), "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        ToastHelper.showToast(LoginActivity.this, "请检查网络重试");

                    }
                });
            }
        });
    }

    private void saveUser(User user) {
        SharedPrefsUtil.putValue(this, "mystats", user.getStats());
        SharedPrefsUtil.putValue(this, "msg", user.getMsg());
        SharedPrefsUtil.putValue(this, "earnings", user.getEarnings());
        SharedPrefsUtil.putValue(this, "capital", user.getCapital());
        SharedPrefsUtil.putValue(this, "username", user.getUsername());
        SharedPrefsUtil.putValue(this, "userpass", password.getText().toString());
        SharedPrefsUtil.putValue(this, "callerid", user.getCallerid());
        SharedPrefsUtil.putValue(this, "money", user.getMoney());
        SharedPrefsUtil.putValue(this, "bindphone", user.getBindphone());
        SharedPrefsUtil.putValue(this, "guoqi", user.getGuoqi());
        SharedPrefsUtil.putValue(this, "point", user.getPoint());
        SharedPrefsUtil.putValue(this, "commission", user.getCommission());
        SharedPrefsUtil.putValue(this, "userlevel", user.getUserlevel());
        SharedPrefsUtil.putValue(this, "uservoice", user.getUservoice());
        SharedPrefsUtil.putValue(this, "didcall", user.getDidcall());
        SharedPrefsUtil.putValue(this, "model", user.getModel());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkNetWork) {
            checkNetWork = true;
            if (!NetworkUtils.isNetworkAvailable(this)) {
                MyDialog.setDialog(this, "提示", "亲！您的网络连接未打开哦", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }, "前往打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetworkUtils.openWirelessSettings(LoginActivity.this);
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getRequestQueue().cancelAll(this);
    }
}
