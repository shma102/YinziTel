package com.yinsidh.user.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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

import static com.yinsidh.android.R.id.shts_submit;


public class ConfirmRedemptionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView confirm_back;

    private TextView confirem_huhang;
    private TextView confirm_mingcheng;
    private TextView confirm_zhanghu;
    private TextView confirm_chikaren;
    private Button shts_btn;
    private static final int BANK_EDIT_NULL = 1;
    private static final int BANK_EDIT_HTTP = 2;
    private static final int BANK_EDIT_ERROR = 3;
    private static final int EDIT_ERROR_LOGIN = 4;

    private String ord_id;
    private Gson gson;
    private int select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_confirm_redemption);

        initView();

        ord_id = getIntent().getStringExtra("ord_id");
        select = getIntent().getIntExtra("select", 0);
        if (select == 1) {
            shts_btn.setText("领取收益");
        } else if (select == 2) {
            shts_btn.setText("确认赎回");
        } else {

        }

        initListener();
    }

    private void initListener() {
        confirm_back.setOnClickListener(this);
        shts_btn.setOnClickListener(this);
    }

    private void initView() {
        confirm_back = (TextView) findViewById(R.id.confirm_back);

        confirem_huhang = (TextView) findViewById(R.id.confirem_huhang);
        confirm_mingcheng = (TextView) findViewById(R.id.confirm_mingcheng);
        confirm_zhanghu = (TextView) findViewById(R.id.confirm_zhanghu);
        confirm_chikaren = (TextView) findViewById(R.id.confirm_chikaren);
        shts_btn = (Button) findViewById(shts_submit);
        gson = new Gson();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent(ConfirmRedemptionActivity.this, MyFinanceActivity.class);
            startActivity(intent);
            ConfirmRedemptionActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_back:
                ConfirmRedemptionActivity.this.finish();
                Intent intent = new Intent(ConfirmRedemptionActivity.this, MyFinanceActivity.class);
                startActivity(intent);
                ConfirmRedemptionActivity.this.finish();
                break;
            case shts_submit:
                String huhang = confirem_huhang.getText().toString();
                String mingcheng = confirm_mingcheng.getText().toString();
                String zhanghu = confirm_zhanghu.getText().toString();
                String chikaren = confirm_chikaren.getText().toString();
                if (TextUtils.isEmpty(huhang) ||
                        TextUtils.isEmpty(zhanghu) ||
                        TextUtils.isEmpty(mingcheng) ||
                        TextUtils.isEmpty(chikaren)) {
                    handler.sendEmptyMessage(BANK_EDIT_NULL);

                } else {
                    MsgWaitDialog.waitdialog(ConfirmRedemptionActivity.this);
                    if (select == 1) {  //领取分红
                        VolleyRequestUtil.RequestPost(this, URL.HOST_LC, "profit", getProfitParams(huhang, mingcheng, zhanghu, chikaren), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                            @Override
                            public void onMySuccess(String result) {
                                Log.e("tag", result);
                                MsgWaitDialog.waitdialog_close();
                                if (!TextUtils.isEmpty(result)) {
                                    Log.e("tag", result);
                                    CommonData commonData = gson.fromJson(result, CommonData.class);
                                    if (commonData.getStats().equals("ok")) {
                                        Message message = handler.obtainMessage();
                                        message.what = BANK_EDIT_HTTP;
                                        message.obj = commonData.getMsg();
                                        handler.sendMessage(message);
                                    } else if (commonData.getStats().equals("errorlogin")) {
                                        Message message = new Message();
                                        message.what = EDIT_ERROR_LOGIN;
                                        message.obj = commonData.getMsg();
                                        handler.sendMessage(message);
                                    } else if (commonData.getStats().equals("errormsg")) {
                                        Message message = new Message();
                                        message.what = BANK_EDIT_ERROR;
                                        message.obj = commonData.getMsg();
                                        handler.sendMessage(message);
                                    }
                                }
                            }

                            @Override
                            public void onMyError(VolleyError error) {
                                MsgWaitDialog.waitdialog_close();
                                ToastHelper.showToast(ConfirmRedemptionActivity.this, "网络出错了");
                            }
                        });

                    } else if (select == 2) {  // 赎回
                        VolleyRequestUtil.RequestPost(this, URL.HOST_LC, "ransom", getRansomParams(huhang, mingcheng, zhanghu, chikaren), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                            @Override
                            public void onMySuccess(String result) {
                                Log.e("tag", result);
                                MsgWaitDialog.waitdialog_close();
                                if (!TextUtils.isEmpty(result)) {
                                    Log.e("tag", result);
                                    CommonData commonData = gson.fromJson(result, CommonData.class);
                                    if (commonData.getStats().equals("ok")) {
                                        Message message = new Message();
                                        message.what = BANK_EDIT_HTTP;
                                        message.obj = commonData.getMsg();
                                        handler.sendMessage(message);
                                    } else {
                                        Message message = handler.obtainMessage();
                                        message.what = BANK_EDIT_ERROR;
                                        message.obj = commonData.getMsg();
                                        handler.sendMessage(message);
                                    }
                                }
                            }

                            @Override
                            public void onMyError(VolleyError error) {
                                MsgWaitDialog.waitdialog_close();
                                ToastHelper.showToast(ConfirmRedemptionActivity.this, "网络出错了");
                            }
                        });

                    }
                }
                break;
            default:
                break;
        }
    }

    private Map<String, String> getRansomParams(String huhang, String mingcheng, String zhanghu, String chikaren) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "ransom");
        params.put("username", SharedPrefsUtil.getValue(ConfirmRedemptionActivity.this,"username",""));
        params.put("ord_id", ord_id);
        params.put("cash_list", huhang);
        params.put("cash_user", mingcheng);
        params.put("cash_code", zhanghu);
        params.put("cash_address", chikaren);
        return params;
    }

    private Map<String, String> getProfitParams(String huhang, String mingcheng, String zhanghu, String chikaren) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "profit");
        params.put("username", SharedPrefsUtil.getValue(ConfirmRedemptionActivity.this,"username",""));
        params.put("ord_id", ord_id);
        params.put("cash_list", huhang);
        params.put("cash_user", mingcheng);
        params.put("cash_code", zhanghu);
        params.put("cash_address", chikaren);
        return params;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case BANK_EDIT_HTTP:
                    String mess = (String) msg.obj;
                    MyDialog.setDialog(ConfirmRedemptionActivity.this, "提示", mess, "取消", new DialogInterface.OnClickListener() {
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
                    break;
                case BANK_EDIT_NULL:
                    MyDialog.setDialog(ConfirmRedemptionActivity.this, "提示", "请输入完整的信息", "取消", new DialogInterface.OnClickListener() {
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
                    break;
                case BANK_EDIT_ERROR:
                    MyDialog.setDialog(ConfirmRedemptionActivity.this, "提示", (String) msg.obj, "取消", new DialogInterface.OnClickListener() {
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
                    break;
                case EDIT_ERROR_LOGIN:
                    SharedPrefsUtil.clearValue(ConfirmRedemptionActivity.this);

                    ToastHelper.showToast(ConfirmRedemptionActivity.this, "登陆信息错误");
                    Intent intent = new Intent(ConfirmRedemptionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    ConfirmRedemptionActivity.this.finish();
                    break;
            }
        }
    };
}
