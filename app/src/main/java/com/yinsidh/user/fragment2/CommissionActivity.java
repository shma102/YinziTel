package com.yinsidh.user.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.yinsidh.utils.KeyboardUtils;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class CommissionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ALI_EDIT_NULL = 0;
    private static final int ALI_EDIT_MONEY = 1;
    private static final int ALI_EDIT_HTTP = 2;
    private static final int BANK_EDIT_NULL = 3;
    private static final int BANK_EDIT_MONEY = 4;
    private static final int BANK_EDIT_HTTP = 5;
    private static final int ALI_EDIT_ERROR = 6;
    private static final int BANK_EDIT_ERROR = 7;
    private static final int EDIT_ERROR_LOGIN = 8;

    private TextView tixian_back2my;
    private RelativeLayout payForAli_top, payForUseBankCard_top;
    private RelativeLayout payForAli_bottom, payForUseBankCard_bottom;

    private LinearLayout alipay_Layout, bankpay_Layout;  //支付宝 银行卡提现布局

    private EditText alipay_Account_Number_Ed, alipay_Account_Name_Ed, alipay_WithDrawMoney_Ed; //支付宝信息
    private EditText BankCardPay_Account_Name_Et, Subbranch_Account_Et, BankCardPay_Account_Number_Et,  //银行卡信息
            Subbranch_Account_Money_Et, CardHolder_Name_Et;

    private boolean Card_flag = true; //true是隐藏 false 显示

    private TextView tixianMoney;
    private TextView tixian_button;

    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_commission);

        initView();

        initDate();

        initListener();
    }

    private void initDate() {
        String money = SharedPrefsUtil.getValue(this, "commission", "");
        if (!TextUtils.isEmpty(money)) {
            tixianMoney.setText(money);
        }
    }

    private void initView() {
        tixian_back2my = (TextView) findViewById(R.id.tixian_back2my);
        payForAli_top = (RelativeLayout) findViewById(R.id.payForAli_top);
        payForUseBankCard_top = (RelativeLayout) findViewById(R.id.payForUseBankCard_top);
        payForAli_bottom = (RelativeLayout) findViewById(R.id.payForAli_bottom);
        payForUseBankCard_bottom = (RelativeLayout) findViewById(R.id.payForUseBankCard_bottom);

        alipay_Layout = (LinearLayout) findViewById(R.id.alipay_Layout);
        bankpay_Layout = (LinearLayout) findViewById(R.id.bankpay_Layout);

        alipay_Account_Number_Ed = (EditText) findViewById(R.id.alipay_Account_Number_Ed);
        alipay_Account_Name_Ed = (EditText) findViewById(R.id.alipay_Account_Name_Ed);
        alipay_WithDrawMoney_Ed = (EditText) findViewById(R.id.alipay_WithDrawMoney_Ed);
        BankCardPay_Account_Name_Et = (EditText) findViewById(R.id.BankCardPay_Account_Name_Et);
        Subbranch_Account_Et = (EditText) findViewById(R.id.Subbranch_Account_Et);
        BankCardPay_Account_Number_Et = (EditText) findViewById(R.id.BankCardPay_Account_Number_Et);
        Subbranch_Account_Money_Et = (EditText) findViewById(R.id.Subbranch_Account_Money_Et);
        CardHolder_Name_Et = (EditText) findViewById(R.id.CardHolder_Name_Et);

        tixianMoney = (TextView) findViewById(R.id.tixianMoney);
        tixian_button = (TextView) findViewById(R.id.tixian_button);

        gson = new Gson();

    }

    private void initListener() {
        tixian_back2my.setOnClickListener(this);
        payForAli_top.setOnClickListener(this);
        payForAli_bottom.setOnClickListener(this);
        payForUseBankCard_top.setOnClickListener(this);
        payForUseBankCard_bottom.setOnClickListener(this);
        tixian_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tixian_back2my:
                KeyboardUtils.hideSoftInput(CommissionActivity.this);
                CommissionActivity.this.finish();
                break;
            case R.id.payForAli_top:
                if (Card_flag) {  //银行卡支付隐藏
                    payForUseBankCard_bottom.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.payForAli_bottom:
                payForAli_top.setVisibility(View.VISIBLE);
                payForUseBankCard_top.setVisibility(View.GONE);
                payForAli_bottom.setVisibility(View.GONE);
                alipay_Layout.setVisibility(View.VISIBLE);
                bankpay_Layout.setVisibility(View.GONE);
                Card_flag = true;
                break;
            case R.id.payForUseBankCard_top:
                if (!Card_flag) {  //银行卡支付显示
                    payForAli_bottom.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.payForUseBankCard_bottom:
                payForAli_top.setVisibility(View.GONE);
                payForUseBankCard_top.setVisibility(View.VISIBLE);
                payForUseBankCard_bottom.setVisibility(View.GONE);
                alipay_Layout.setVisibility(View.GONE);
                bankpay_Layout.setVisibility(View.VISIBLE);
                Card_flag = false;
                break;
            case R.id.tixian_button:
                if (Card_flag) {  //支付宝体现
                    String Alipay_Account_Number = alipay_Account_Number_Ed.getText().toString();
                    String Alipay_Account_Name = alipay_Account_Name_Ed.getText().toString();
                    String Alipay_WithDrawMoney = alipay_WithDrawMoney_Ed.getText().toString();
                    //判断输入框是否为空
                    if (Alipay_Account_Number.equals("") || Alipay_Account_Name.equals("") ||
                            Alipay_WithDrawMoney.equals("")) {
                        handler.sendEmptyMessage(ALI_EDIT_NULL);
                    } else { //不为空
                        //执行提现操作
                        MsgWaitDialog.waitdialog(CommissionActivity.this);
                        VolleyRequestUtil.RequestPost(CommissionActivity.this, URL.HOST, "cash_alipay", getCashParams(Alipay_Account_Number, Alipay_Account_Name, Alipay_WithDrawMoney), new VolleyListenerInterface(CommissionActivity.this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                            @Override
                            public void onMySuccess(String result) {
                                MsgWaitDialog.waitdialog_close();
                                if (!TextUtils.isEmpty(result)) {
                                    Log.e("tag", result);
                                    CommonData commonData = gson.fromJson(result, CommonData.class);
                                    if (commonData.getStats().equals("ok")) {
                                        Message message = new Message();
                                        message.what = ALI_EDIT_HTTP;
                                        message.obj = commonData.getMsg();
                                        handler.sendMessage(message);
                                    } else if (commonData.getStats().equals("errorlogin")) {
                                        Message message = new Message();
                                        message.what = EDIT_ERROR_LOGIN;
                                        message.obj = commonData.getMsg();
                                        handler.sendMessage(message);
                                    } else if (commonData.getStats().equals("errormsg")) {
                                        Message message = new Message();
                                        message.what = ALI_EDIT_ERROR;
                                        message.obj = commonData.getMsg();
                                        handler.sendMessage(message);
                                    }
                                }
                            }

                            @Override
                            public void onMyError(VolleyError error) {
                                MsgWaitDialog.waitdialog_close();
                                ToastHelper.showToast(CommissionActivity.this, "网络出错了！");
                            }
                        });
                    }


                } else {  //银行卡提现
                    String BankCardPay_Account_Name = BankCardPay_Account_Name_Et.getText()
                            .toString();
                    String Subbranch_Account = Subbranch_Account_Et.getText().toString();
                    String BankCardPay_Account_Number = BankCardPay_Account_Number_Et.getText()
                            .toString();
                    String Subbranch_Account_Money = Subbranch_Account_Money_Et.getText()
                            .toString();
                    String CardHolder_Name = CardHolder_Name_Et.getText().toString();
                    if (TextUtils.isEmpty(BankCardPay_Account_Name) ||
                            TextUtils.isEmpty(Subbranch_Account) ||
                            TextUtils.isEmpty(BankCardPay_Account_Number) ||
                            TextUtils.isEmpty(Subbranch_Account_Money) ||
                            TextUtils.isEmpty(CardHolder_Name)) {
                        handler.sendEmptyMessage(BANK_EDIT_NULL);

                    } else {
                        //执行提现操作
                        MsgWaitDialog.waitdialog(CommissionActivity.this);
                        VolleyRequestUtil.RequestPost(CommissionActivity.this, URL.HOST,
                                "cash_bank", getBankParams(BankCardPay_Account_Name,
                                        Subbranch_Account, BankCardPay_Account_Number,
                                        Subbranch_Account_Money, CardHolder_Name),
                                new VolleyListenerInterface(CommissionActivity.this,
                                        VolleyListenerInterface.mListener,
                                        VolleyListenerInterface.mErrorListener) {
                                    @Override
                                    public void onMySuccess(String result) {
                                        MsgWaitDialog.waitdialog_close();
                                        if (!TextUtils.isEmpty(result)) {
                                            Log.e("tag", result);
                                            CommonData commonData = gson.fromJson(result, CommonData.class);
                                            if (commonData.getStats().equals("ok")) {
                                                Message message = new Message();
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
                                        ToastHelper.showToast(CommissionActivity.this, "网络出错了");
                                    }
                                });
                    }
                }

                break;

        }
    }

    private Map<String, String> getBankParams(String bankCardPay_account_name, String subbranch_account, String bankCardPay_account_number, String subbranch_account_money, String cardHolder_name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "cash_bank");
        params.put("username", SharedPrefsUtil.getValue(CommissionActivity.this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(CommissionActivity.this, "userpass", ""));
        params.put("bankname", bankCardPay_account_name);
        params.put("bankuser", cardHolder_name);
        params.put("bankcash", subbranch_account_money);
        params.put("banktype", subbranch_account);
        params.put("banknumber", bankCardPay_account_number);
        return params;
    }

    private Map<String, String> getCashParams(String number, String name, String money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "cash_alipay");
        params.put("username", SharedPrefsUtil.getValue(CommissionActivity.this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(CommissionActivity.this, "userpass", ""));
        params.put("bankname", number);
        params.put("bankcash", money);
        params.put("bankuser", name);
        return params;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALI_EDIT_NULL:
                    MyDialog.setOneDialog(CommissionActivity.this, "提示", "请输入全部的信息", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case ALI_EDIT_HTTP:
                    String message = (String) msg.obj;
                    MyDialog.setOneDialog(CommissionActivity.this, "提示", message, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case BANK_EDIT_NULL:
                    MyDialog.setOneDialog(CommissionActivity.this, "提示", "请输入全部的信息", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;

                case BANK_EDIT_HTTP:
                    String mess = (String) msg.obj;
                    MyDialog.setOneDialog(CommissionActivity.this, "提示", mess, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case ALI_EDIT_ERROR:
                    String mess_ali_error = (String) msg.obj;
                    MyDialog.setOneDialog(CommissionActivity.this, "提示", mess_ali_error, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case BANK_EDIT_ERROR:
                    String mess_bank_error = (String) msg.obj;
                    MyDialog.setOneDialog(CommissionActivity.this, "提示", mess_bank_error, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case EDIT_ERROR_LOGIN:
                    MyDialog.setDialog(CommissionActivity.this, "提示", (String) msg.obj, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPrefsUtil.clearValue(CommissionActivity.this);

                            ToastHelper.showToast(CommissionActivity.this, "登陆信息错误");
                            Intent intent = new Intent(CommissionActivity.this, LoginActivity.class);
                            startActivity(intent);
                            CommissionActivity.this.finish();
                        }
                    });
                    break;

            }
        }
    };

}
