package com.yinsidh.user.fragment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.android.R;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.user.alipay.PayBackBean;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016/11/3.
 */
public class PayBackActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView money, id, date, jixu, fangshi, zhanghu, fq1;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.payback);

        initView();
        fq1.setOnClickListener(this);
        jixu.setOnClickListener(this);
        payBack_Http();

    }

    private void initView() {
        money = (TextView) findViewById(R.id.money);
        id = (TextView) findViewById(R.id.id);
        date = (TextView) findViewById(R.id.date);
        fq1 = (TextView) findViewById(R.id.payback_back);
        jixu = (TextView) findViewById(R.id.jixu);
        zhanghu = (TextView) findViewById(R.id.zhanghu);
        fangshi = (TextView) findViewById(R.id.fangshi);

        gson = new Gson();
    }

    private void payBack_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST, "payback", getPaybckParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                PayBackBean payBackBean = gson.fromJson(result, PayBackBean.class);
                money.setText("￥" + payBackBean.getOrder_money());
                date.setText(payBackBean.getOrder_date());
                id.setText(payBackBean.getOrder_id());
                fangshi.setText(payBackBean.getOrder_type());
                zhanghu.setText(payBackBean.getOrder_user());

            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(PayBackActivity.this, "请检查网络后重试");

            }
        });
    }

    private Map<String, String> getPaybckParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "payok");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        return params;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jixu:
                PayBackActivity.this.finish();
                break;
            case R.id.payback_back:
                PayBackActivity.this.finish();
                break;
            default:
                break;
        }
    }
}

