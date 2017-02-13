package com.yinsidh.user.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.yinsidh.bean.InvestmentBean;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.user.alipay.PayResult;
import com.yinsidh.user.alipay.SignUtils;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class InvestmentActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout investment_layout1;
    private TextView investment_back;
    private TextView investment_number;  //理财金额
    private TextView investment_shouyi;
    private TextView investment_tianshu;
    private RelativeLayout investment_weixin;  //微信支付
    private RelativeLayout investment_zhifubao;  //支付宝支付
    private TextView investment_weixin_radio;
    private TextView investment_zhifubao_radio;
    private Button investment_btn;
    private int flag = 0;

    public static final String PARTNER = "2088511089141811";
    // 商户收款账号
    public static final String SELLER = "pay@rrtel.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANVbRuPjoDXUNf130BkG5dVoPIaCgF86f142nATI+0wZINuETX/9kZohMOJRdWpFg5iFO3uxeCRvt3LDYCquBSGb1cYzHTOxbjSIH9tggpWT1NC5MhRTZR/kQ//vqwg4otQq8sjLzMwDCr1msLAIlBlPeuevEaG8lduFHpd4UMLjAgMBAAECgYBA07gpfDtsv6sb9fCz+gWRn1dso3Bv/u0+aidjyNGaYLQ8HuoRbrFz/IvG6Gtdu/ttN2ZIgjb4Ez3RUA4DSdbivjaLNFxLW0XZScn03uCqQ5/Z5i8FDZ+RHaETxylzyn8gjSZqHV3Ukz6i24ReRU2ubzZ3krG3DsG3Iqc4ZbV/wQJBAPQ/4mA4zkQcQyPbhMVCGrWgpHv7tiMnd8GQOj9YM48k7Eg/3X+t0YfceB4uZA78UIEUSl7bmL2fLecJNBgV1OECQQDfnuwar6Od39BOIDr7MFF32zdIKqp+MPIUjui31BWzauq/tJzacxu9DUFcNtXSBeLKGGooVAtHOspOpGjhD4xDAkBHBgCcvEf2dEVtutqLxaIgqHrL9rDK3iIrrfXSD6LGgBUZGUEebHveRtTC6fh8hxAdyLmrha2Pjib55Ko6SrbhAkBE2ncSRHBZhPdrhssCWr9K60mbQ9/ZGRMAt3v6VigUZWBAhMkjDfxFepZcYVn23+8TkO7m2fHbDcQK8N6GJbZnAkEAzycYXnDiur6SALUlb7wiI76JShMuUcQruqmDK602nq7qsSHdm71iJItYTw+CC0kG1PI+t0g3ipLcRrTeGh9NMA==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    private IWXAPI iwxApi;

    private int qitou, number; // 余额增益 理财产品
    private String jine;  //显示的金额
    private int shuru_jine; //增益体验专区
    private String pid;
    private Gson gson;
    private String str_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_investment);
        initView();
        initListener();
        shuru_jine = getIntent().getIntExtra("money", 0);
        number = getIntent().getIntExtra("number", 0);
        qitou = getIntent().getIntExtra("qitou", 0);
        if (number != 0) {
            jine = number * qitou + "";
            investment_number.setText(jine);
        }
        if (shuru_jine != 0) {
            jine = shuru_jine + "";
            investment_number.setText(jine);
        }
        pid = getIntent().getStringExtra("pid");

        Investement_Http();

    }

    private void Investement_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST_LC, "pay_page", getPayPageParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("tag", result);
                if (!TextUtils.isEmpty(result)) {
                    investment_layout1.setVisibility(View.VISIBLE);
                    MsgWaitDialog.waitdialog_close();
                    InvestmentBean bean = gson.fromJson(result, InvestmentBean.class);
                    DecimalFormat df = new DecimalFormat("#.00");
                    str_rate = bean.getRate();
                    double rate = Double.valueOf(bean.getRate()) / 100;
                    int money = Integer.valueOf(investment_number.getText().toString());
                    investment_shouyi.setText(df.format((money * rate + money)) + "");
                    investment_tianshu.setText(bean.getDays());
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(InvestmentActivity.this, "请检查网络设置");
            }
        });
    }

    private Map<String, String> getPayPageParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "pay_page");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("pid", pid);
        return params;
    }

    private void initListener() {
        investment_back.setOnClickListener(this);
        investment_weixin.setOnClickListener(this);
        investment_zhifubao.setOnClickListener(this);
        investment_btn.setOnClickListener(this);

    }

    private void initView() {
        investment_layout1 = (LinearLayout) findViewById(R.id.investment_layout1);
        investment_back = (TextView) findViewById(R.id.investment_back);
        investment_number = (TextView) findViewById(R.id.investment_number);
        investment_weixin = (RelativeLayout) findViewById(R.id.investment_weixin);
        investment_weixin_radio = (TextView) findViewById(R.id.investment_weixin_radio);
        investment_zhifubao_radio = (TextView) findViewById(R.id.investment_zhifubao_radio);
        investment_zhifubao = (RelativeLayout) findViewById(R.id.investment_zhifubao);
        investment_shouyi = (TextView) findViewById(R.id.investment_shouyi);
        investment_tianshu = (TextView) findViewById(R.id.investment_tianshu);
        investment_btn = (Button) findViewById(R.id.investment_btn);

        gson = new Gson();
        iwxApi = WXAPIFactory.createWXAPI(this, "wxa791d98ecb40324b");
        iwxApi.registerApp("wxa791d98ecb40324b");

        investment_layout1.setVisibility(View.GONE);
        MsgWaitDialog.waitdialog(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.investment_back:
                InvestmentActivity.this.finish();
                break;
            case R.id.investment_weixin:
                investment_weixin_radio.setBackgroundResource(R.drawable.checkbox_check);
                investment_zhifubao_radio.setBackgroundResource(R.drawable.checkbox_uncheck);
                flag = 1;
                break;
            case R.id.investment_zhifubao:
                investment_weixin_radio.setBackgroundResource(R.drawable.checkbox_uncheck);
                investment_zhifubao_radio.setBackgroundResource(R.drawable.checkbox_check);
                flag = 2;
                break;
            case R.id.investment_btn:
                if (flag == 1) {
                    MsgWaitDialog.waitdialog(this);
                    VolleyRequestUtil.RequestPost(this, URL.HOST_LC, "licai_wxpay", getLicaiWxParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                        @Override
                        public void onMySuccess(String result) {
                            MsgWaitDialog.waitdialog_close();
                            if (!result.equals("")) {
                                MsgWaitDialog.waitdialog_close();
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
                                        Toast.makeText(InvestmentActivity.this, "请安装微信", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ToastHelper.showToast(InvestmentActivity.this, "支付失败");
                            }
                        }

                        @Override
                        public void onMyError(VolleyError error) {
                            MsgWaitDialog.waitdialog_close();
                            ToastHelper.showToast(InvestmentActivity.this, "网络出错了");
                        }
                    });

                } else if (flag == 2) {
                    ToastHelper.showToast(this, "后台处理中...");
                    MsgWaitDialog.waitdialog(this);
                    VolleyRequestUtil.RequestPost(this, URL.HOST_LC, "licai_alipay", getLicaiAliParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                        @Override
                        public void onMySuccess(String result) {
                            MsgWaitDialog.waitdialog_close();
                            AliOrderBean aliOrder = gson.fromJson(result, AliOrderBean.class);
                            if (!aliOrder.getStats().equals("")) {
                                String order_number = aliOrder.getStats();
                                MsgWaitDialog.waitdialog_close();
                                pay(order_number, "0.01元");
//                                pay(order_number, jine + "元");
                            } else {
                                ToastHelper.showToast(InvestmentActivity.this, "支付失败");
                            }

                        }

                        @Override
                        public void onMyError(VolleyError error) {
                            MsgWaitDialog.waitdialog_close();
                            ToastHelper.showToast(InvestmentActivity.this, "网络出错了");
                        }
                    });

                } else {
                    ToastHelper.showToast(this, "请选择支付方式");
                }
                break;
            default:
                break;
        }
    }

    private Map<String, String> getLicaiWxParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "licai_wxpay");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("id", pid);
        params.put("money", "0.01");
//        params.put("money", investment_number.getText().toString());
        params.put("ord_num", number + "");
        params.put("ord_rate", str_rate);
        return params;
    }

    private Map<String, String> getLicaiAliParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "licai_alipay");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("id", pid);
        params.put("money", "0.01");
//        params.put("money", investment_number.getText().toString());
        params.put("ord_num", number + "");
        params.put("ord_rate", str_rate);
        return params;
    }

    public void pay(String or, String money) {
        if (!money.equals("")) {
            if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
                new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                InvestmentActivity.this.finish();
                            }
                        }).show();
                return;
            }
            String orderInfo = getOrderInfo("隐私电话", SharedPrefsUtil.getValue(InvestmentActivity.this, "username", ""), money.substring(0, money.indexOf("元")), or);
            /**
             * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
             */
            String sign = sign(orderInfo);
            try {
                /**
                 * 仅需对sign 做URL编码
                 */
                sign = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            /**
             * 完整的符合支付宝参数规范的订单信息
             */
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

            final Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(InvestmentActivity.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);
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

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price, String or) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + or + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + URL.HOST_LC_ALIPAY + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

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
                        Intent intent = new Intent(InvestmentActivity.this, PayBackActivity.class);
                        startActivity(intent);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        ToastHelper.showToast(InvestmentActivity.this, "正在处理中");
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        ToastHelper.showToast(InvestmentActivity.this, "订单支付失败");
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        ToastHelper.showToast(InvestmentActivity.this, "取消支付");
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        ToastHelper.showToast(InvestmentActivity.this, "网络连接出错");
                    } else {
                        ToastHelper.showToast(InvestmentActivity.this, "支付失败");
                    }
                    break;
            }
        }
    };
}
