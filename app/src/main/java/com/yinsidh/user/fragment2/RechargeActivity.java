package com.yinsidh.user.fragment2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yinsidh.android.R;
import com.yinsidh.bean.AliOrderBean;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.user.alipay.PayResult;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {


    private IWXAPI iwxApi;

    private Gson gson;

    private int recharge_int = 0;  //判断用户点击的那个重置按钮
    private ImageView backToPay_image; //支付方式的返回按钮
    private TextView money;  //支付方式的支付金额
    private RelativeLayout Ali_layout;  //支付宝支付
    private RelativeLayout WeiXin_layout;  //微信支付
    private RelativeLayout twenty_layout;
    private RelativeLayout oneHundred_layout;
    private RelativeLayout twoHundred_layout;
    private RelativeLayout fiveHundred_layout;
    private RelativeLayout eightHundred_layout;
    private RelativeLayout twoThousand_layout;

    private TextView back;

    private PopupWindow popupWindow;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Intent intent = new Intent(RechargeActivity.this, PayBackActivity.class);
                        startActivity(intent);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        ToastHelper.showToast(RechargeActivity.this, "正在处理中");
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        ToastHelper.showToast(RechargeActivity.this, "订单支付失败");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        ToastHelper.showToast(RechargeActivity.this, "取消支付");
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        ToastHelper.showToast(RechargeActivity.this, "网络连接出错");
                    } else {
                        ToastHelper.showToast(RechargeActivity.this, "支付失败");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_recharge);

        initView();
        initDate();
        initListener();
    }

    private void initDate() {
        iwxApi = WXAPIFactory.createWXAPI(this, "wxa791d98ecb40324b");
        iwxApi.registerApp("wxa791d98ecb40324b");
    }

    private void initListener() {
        back.setOnClickListener(this);

        twenty_layout.setOnClickListener(this);
        oneHundred_layout.setOnClickListener(this);
        twoHundred_layout.setOnClickListener(this);
        fiveHundred_layout.setOnClickListener(this);
        eightHundred_layout.setOnClickListener(this);
        twoThousand_layout.setOnClickListener(this);
    }

    private void initView() {
        back = (TextView) findViewById(R.id.recharge_back2my);
        twenty_layout = (RelativeLayout) findViewById(R.id.rechargeTwenty);
        oneHundred_layout = (RelativeLayout) findViewById(R.id.rechargeOneHundred);
        twoHundred_layout = (RelativeLayout) findViewById(R.id.rechargeTwoHundred);
        fiveHundred_layout = (RelativeLayout) findViewById(R.id.rechargeFiveHundred);
        eightHundred_layout = (RelativeLayout) findViewById(R.id.rechargeEightHundred);
        twoThousand_layout = (RelativeLayout) findViewById(R.id.rechargeTwoThousand);

        gson = new Gson();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rechargeTwenty:
                recharge_int = 0;
                changeRechargeBackground(0);
                showPopupWindow(v, recharge_int);
                break;
            case R.id.rechargeOneHundred:
                recharge_int = 5;
                changeRechargeBackground(5);
                showPopupWindow(v, recharge_int);
                break;
            case R.id.rechargeTwoHundred:
                recharge_int = 1;
                changeRechargeBackground(1);
                showPopupWindow(v, recharge_int);
                break;
            case R.id.rechargeFiveHundred:
                recharge_int = 2;
                changeRechargeBackground(2);
                showPopupWindow(v, recharge_int);
                break;
            case R.id.rechargeEightHundred:
                recharge_int = 3;
                changeRechargeBackground(3);
                showPopupWindow(v, recharge_int);
                break;
            case R.id.rechargeTwoThousand:
                recharge_int = 4;
                changeRechargeBackground(4);
                showPopupWindow(v, recharge_int);
                break;
            case R.id.recharge_back2my:
                RechargeActivity.this.finish();
                break;
            default:
                break;
        }
    }

    //显示底部支付框
    private void showPopupWindow(View v, int recharge_int) {

        if (popupWindow == null) {
            //初始化支付框各个组件
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.recharge_popupwindow, null, false);
            backToPay_image = (ImageView) view.findViewById(R.id.backToPayFragment);
            money = (TextView) view.findViewById(R.id.money);
            Ali_layout = (RelativeLayout) view.findViewById(R.id.payForUseAli);
            WeiXin_layout = (RelativeLayout) view.findViewById(R.id.payForUseWX);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        //设置popupwindow可点击
        popupWindow.setFocusable(true);
        //设置允许在外部点消失
        popupWindow.setOutsideTouchable(true);
//        //设置弹出和消失动画
        popupWindow.setAnimationStyle(R.style.AnimationPreview);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        //返回按钮点击事件
        backToPay_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        //支付宝支付
        Ali_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                MsgWaitDialog.waitdialog(RechargeActivity.this);
                String username = SharedPrefsUtil.getValue(RechargeActivity.this, "username", "");
                final String money_pay = money.getText().toString().substring(0, money.getText().toString().indexOf("元"));
                VolleyRequestUtil.RequestPost(RechargeActivity.this, URL.HOST, "alipay2", getAliParams(username, money_pay), new VolleyListenerInterface(RechargeActivity.this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        MsgWaitDialog.waitdialog_close();
                        AliOrderBean aliOrder = gson.fromJson(result, AliOrderBean.class);
                        if (!aliOrder.getStats().equals("")) {
                            String sign_str = aliOrder.getSign();
                            pay(money.getText().toString(), sign_str);
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        MsgWaitDialog.waitdialog_close();
                        ToastHelper.showToast(RechargeActivity.this, "网络出错了");
                    }
                });
            }
        });
        //微信支付
        WeiXin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                MsgWaitDialog.waitdialog(RechargeActivity.this);
                String username = SharedPrefsUtil.getValue(RechargeActivity.this, "username", "");
                final String money_pay = money.getText().toString().substring(0, money.getText().toString().indexOf("元"));
                VolleyRequestUtil.RequestPost(RechargeActivity.this, URL.HOST, "wxpay_combo", getWeixinPayParams(username, money_pay), new VolleyListenerInterface(RechargeActivity.this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        MsgWaitDialog.waitdialog_close();
                        if (!result.equals("")) {
                            try {
                                PayReq req = new PayReq();
                                JSONObject json = new JSONObject(result);
                                req.appId = json.getString("appid");
                                req.partnerId = json.getString("partnerid");
                                req.prepayId = json.getString("prepayid");
                                req.nonceStr = json.getString("noncestr");
                                req.timeStamp = json.getString("timestamp");
                                req.packageValue = json.getString("package");
                                req.sign = json.getString("sign");
                                req.extData = "app data";
                                if (!iwxApi.sendReq(req)) {
                                    ToastHelper.showToast(RechargeActivity.this, "请先安装微信");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        MsgWaitDialog.waitdialog_close();
                        ToastHelper.showToast(RechargeActivity.this, "网络出错了");
                    }
                });
            }
        });

        switch (recharge_int) {
            case 0:
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                money.setText("20元");
                break;
            case 1:
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                money.setText("200元");
                break;
            case 2:
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                money.setText("500元");
                break;
            case 3:
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                money.setText("800元");
                break;
            case 4:
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                money.setText("2000元");
                break;
            case 5:
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                money.setText("100元");
                break;
        }
//        money.setText("0.01元");

    }

    private void changeRechargeBackground(int i) {
        setBackgroundtransparent();
        switch (i) {
            case 0:
                twenty_layout.setBackgroundResource(R.drawable.recharge_bg);
                break;
            case 1:
                twoHundred_layout.setBackgroundResource(R.drawable.recharge_bg);
                break;
            case 2:
                fiveHundred_layout.setBackgroundResource(R.drawable.recharge_bg);
                break;
            case 3:
                eightHundred_layout.setBackgroundResource(R.drawable.recharge_bg);
                break;
            case 4:
                twoThousand_layout.setBackgroundResource(R.drawable.recharge_bg);
                break;
        }
    }

    private void setBackgroundtransparent() {
        twenty_layout.setBackgroundColor(Color.TRANSPARENT);
        twoHundred_layout.setBackgroundColor(Color.TRANSPARENT);
        fiveHundred_layout.setBackgroundColor(Color.TRANSPARENT);
        eightHundred_layout.setBackgroundColor(Color.TRANSPARENT);
        twoThousand_layout.setBackgroundColor(Color.TRANSPARENT);
    }

    private Map<String, String> getWeixinPayParams(String username, String money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "wxpay_combo");
        params.put("username", username);
        params.put("usermoney", money);
        return params;
    }

    public Map<String, String> getAliParams(String username, String money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "alipay2");
        params.put("username", username);
        params.put("usermoney", money);
        return params;
    }

    public void pay(String money, final String sign) {
        if (!money.equals("")) {
            final Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(RechargeActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(sign, true);
                    Log.e("tag", "支付结果" + result);
                    Message message = new Message();
                    message.what = 0;
                    message.obj = result;
                    handler.sendMessage(message);

                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    }
}
