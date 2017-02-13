package com.yinsidh.user.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yinsidh.android.R;
import com.yinsidh.bean.AliOrderBean;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.user.alipay.PayResult;
import com.yinsidh.user.fragment2.PayBackActivity;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaocanFragment extends Fragment implements View.OnClickListener {
//    public static final String PARTNER = "2088511089141811";
//    // 商户收款账号
//    public static final String SELLER = "pay@rrtel.com";
//    // 商户私钥，pkcs8格式
//    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANVbRuPjoDXUNf130BkG5dVoPIaCgF86f142nATI+0wZINuETX/9kZohMOJRdWpFg5iFO3uxeCRvt3LDYCquBSGb1cYzHTOxbjSIH9tggpWT1NC5MhRTZR/kQ//vqwg4otQq8sjLzMwDCr1msLAIlBlPeuevEaG8lduFHpd4UMLjAgMBAAECgYBA07gpfDtsv6sb9fCz+gWRn1dso3Bv/u0+aidjyNGaYLQ8HuoRbrFz/IvG6Gtdu/ttN2ZIgjb4Ez3RUA4DSdbivjaLNFxLW0XZScn03uCqQ5/Z5i8FDZ+RHaETxylzyn8gjSZqHV3Ukz6i24ReRU2ubzZ3krG3DsG3Iqc4ZbV/wQJBAPQ/4mA4zkQcQyPbhMVCGrWgpHv7tiMnd8GQOj9YM48k7Eg/3X+t0YfceB4uZA78UIEUSl7bmL2fLecJNBgV1OECQQDfnuwar6Od39BOIDr7MFF32zdIKqp+MPIUjui31BWzauq/tJzacxu9DUFcNtXSBeLKGGooVAtHOspOpGjhD4xDAkBHBgCcvEf2dEVtutqLxaIgqHrL9rDK3iIrrfXSD6LGgBUZGUEebHveRtTC6fh8hxAdyLmrha2Pjib55Ko6SrbhAkBE2ncSRHBZhPdrhssCWr9K60mbQ9/ZGRMAt3v6VigUZWBAhMkjDfxFepZcYVn23+8TkO7m2fHbDcQK8N6GJbZnAkEAzycYXnDiur6SALUlb7wiI76JShMuUcQruqmDK602nq7qsSHdm71iJItYTw+CC0kG1PI+t0g3ipLcRrTeGh9NMA==";
//    // 支付宝公钥
//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    private String sign_str;
    private IWXAPI iwxApi;

    private Gson gson;

    private View rootView;


    private RelativeLayout twenty_layout;
    private RelativeLayout oneHundred_layout;
    private RelativeLayout twoHundred_layout;
    private RelativeLayout fiveHundred_layout;
    private RelativeLayout eightHundred_layout;
    private RelativeLayout twoThousand_layout;

    private PopupWindow popupWindow;

    private int recharge_int = 0;  //判断用户点击的那个重置按钮
    private ImageView backToPay_image; //支付方式的返回按钮
    private TextView money;  //支付方式的支付金额
    private RelativeLayout Ali_layout;  //支付宝支付
    private RelativeLayout WeiXin_layout;  //微信支付


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
                        Intent intent = new Intent(getActivity(), PayBackActivity.class);
                        startActivity(intent);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        ToastHelper.showToast(getActivity(), "正在处理中");
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        ToastHelper.showToast(getActivity(), "订单支付失败");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        ToastHelper.showToast(getActivity(), "取消支付");
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        ToastHelper.showToast(getActivity(), "网络连接出错");
                    } else {
                        ToastHelper.showToast(getActivity(), "支付失败");
                    }

                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_taocan, container, false);

        initView(rootView);
        initListener();
        return rootView;
    }

    private void initListener() {
        twenty_layout.setOnClickListener(this);
        twoHundred_layout.setOnClickListener(this);
        fiveHundred_layout.setOnClickListener(this);
        eightHundred_layout.setOnClickListener(this);
        twoThousand_layout.setOnClickListener(this);
        oneHundred_layout.setOnClickListener(this);

    }

    private void initView(View rootView) {
        twenty_layout = (RelativeLayout) rootView.findViewById(R.id.rechargeTwenty);
        oneHundred_layout = (RelativeLayout) rootView.findViewById(R.id.rechargeOneHundred);
        twoHundred_layout = (RelativeLayout) rootView.findViewById(R.id.rechargeTwoHundred);
        fiveHundred_layout = (RelativeLayout) rootView.findViewById(R.id.rechargeFiveHundred);
        eightHundred_layout = (RelativeLayout) rootView.findViewById(R.id.rechargeEightHundred);
        twoThousand_layout = (RelativeLayout) rootView.findViewById(R.id.rechargeTwoThousand);

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
            default:
                break;
        }
    }

    //显示底部支付框
    private void showPopupWindow(View v, int recharge_int) {
        iwxApi = WXAPIFactory.createWXAPI(getActivity(), null);
        iwxApi.registerApp("wxa791d98ecb40324b");
        if (popupWindow == null) {
            //初始化支付框各个组件
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
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
        //设置弹出和消失动画
        popupWindow.setAnimationStyle(R.style.AnimationPreview);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        //返回按钮点击事件
        backToPay_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //支付宝支付
        Ali_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                MsgWaitDialog.waitdialog(getActivity());
                String username = SharedPrefsUtil.getValue(getActivity(), "username", "");
                final String money_pay = money.getText().toString().substring(0, money.getText().toString().indexOf("元"));

                VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "alipay2", getAliParams(username, money_pay), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        MsgWaitDialog.waitdialog_close();
                        AliOrderBean aliOrder = gson.fromJson(result, AliOrderBean.class);
                        Log.e("tag", aliOrder.toString());
                        if (!aliOrder.getStats().equals("")) {
                            String sign_str = aliOrder.getSign();
                            pay(money.getText().toString(), sign_str);
                        } else {
                            ToastHelper.showToast(getActivity(), "支付失败");
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        MsgWaitDialog.waitdialog_close();
                        ToastHelper.showToast(getActivity(), "支付失败");
                    }
                });
            }
        });
        //微信支付
        WeiXin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                MsgWaitDialog.waitdialog(getActivity());
                String username = SharedPrefsUtil.getValue(getActivity(), "username", "");
                final String money_pay = money.getText().toString().substring(0, money.getText().toString().indexOf("元"));
                VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "wxpay_combo", getWeixinPayParams(username, money_pay), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {

                        if (!result.equals("")) {
                            MsgWaitDialog.waitdialog_close();
                            ToastHelper.showToast(getActivity(), "后台处理中...");
                            try {
                                PayReq req = new PayReq();
                                JSONObject json = new JSONObject(result);
                                req.appId = json.getString("appid");
                                req.nonceStr = json.getString("noncestr");
                                req.packageValue = json.getString("package");
                                req.partnerId = json.getString("partnerid");
                                req.timeStamp = json.getString("timestamp");
                                req.prepayId = json.getString("prepayid");
                                req.sign = json.getString("sign");
                                req.extData = "app data";
                                if (!iwxApi.sendReq(req)) {
                                    Toast.makeText(getActivity(), "请安装微信", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        MsgWaitDialog.waitdialog_close();
                        ToastHelper.showToast(getActivity(), "支付失败");
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

    private Map<String, String> getWeixinPayParams(String username, String money) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "wxpay_combo");
        params.put("username", username);
        params.put("usermoney", money);
        return params;
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
                    PayTask alipay = new PayTask(getActivity());
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;

        }
    }
}
