package com.yinsidh.user.fragment2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.android.R;
import com.yinsidh.bean.ZaiqiDetailsBean;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class ZaiqiFinanceActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout zaiqi_finance_layout1;
    private TextView zaiqi_finance_my_back2my;
    private TextView zaiqi_finance_qixian_tv;
    private TextView zaiqi_finance_shouyi_tv;
    private TextView zaiqi_finance_dengji_tv;
    private TextView zaiqi_finance_cyje;
    private TextView zaiqi_finance_ygdq;
    private TextView zaiqi_finance_bxzj;
    private TextView zaiqi_finance_title;
    private TextView zaiqi_finance_earning;
    private TextView zaiqi_finance_bianxian;
    private TextView zaiqi_finance_xinxi;
    private TextView shts_back;
    private Button shts_btn;

    private String pid;
    private Gson gson;
    private PopupWindow shPopuWindow;
    private WindowManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_zaiqi_finance);

        pid = getIntent().getStringExtra("pid");

        initView();

        initListener();

    }

    @Override
    protected void onStart() {
        super.onResume();
        if (!TextUtils.isEmpty(pid)) {
            ZaiqiFinance_Http();
        }

    }

    private void ZaiqiFinance_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST_LC, "myorder", getMyOrderParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    zaiqi_finance_layout1.setVisibility(View.VISIBLE);
                    MsgWaitDialog.waitdialog_close();
                    ZaiqiDetailsBean bean = gson.fromJson(result, ZaiqiDetailsBean.class);
                    initDate(bean);
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(ZaiqiFinanceActivity.this, "网络出错了");
            }
        });
    }

    private void initDate(ZaiqiDetailsBean bean) {
        zaiqi_finance_qixian_tv.setText(bean.getDeadline());
        zaiqi_finance_shouyi_tv.setText(bean.getRate() + "%");
        zaiqi_finance_dengji_tv.setText(bean.getRisk());
        zaiqi_finance_cyje.setText(bean.getPrice());
        zaiqi_finance_ygdq.setText(bean.getAllprice());
        zaiqi_finance_bxzj.setText(bean.getCashprice());
        zaiqi_finance_title.setText(bean.getName());
        zaiqi_finance_earning.setText(bean.getEarnings());
        zaiqi_finance_xinxi.setText(bean.getSafety());
    }

    private Map<String, String> getMyOrderParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "myorder");
        params.put("username", SharedPrefsUtil.getValue(ZaiqiFinanceActivity.this, "username", ""));
        params.put("pid", pid);
        return params;
    }

    private void initListener() {
        zaiqi_finance_my_back2my.setOnClickListener(this);
        zaiqi_finance_bianxian.setOnClickListener(this);
    }

    private void initView() {
        zaiqi_finance_layout1 = (RelativeLayout) findViewById(R.id.zaiqi_finance_layout1);
        zaiqi_finance_my_back2my = (TextView) findViewById(R.id.zaiqi_finance_my_back2my);
        zaiqi_finance_dengji_tv = (TextView) findViewById(R.id.zaiqi_finance_dengji_tv);
        zaiqi_finance_qixian_tv = (TextView) findViewById(R.id.zaiqi_finance_qixian_tv);
        zaiqi_finance_shouyi_tv = (TextView) findViewById(R.id.zaiqi_finance_shouyi_tv);
        zaiqi_finance_cyje = (TextView) findViewById(R.id.zaiqi_finance_cyje);
        zaiqi_finance_ygdq = (TextView) findViewById(R.id.zaiqi_finance_ygdq);
        zaiqi_finance_bxzj = (TextView) findViewById(R.id.zaiqi_finance_bxzj);
        zaiqi_finance_title = (TextView) findViewById(R.id.zaiqi_finance_title);
        zaiqi_finance_earning = (TextView) findViewById(R.id.zaiqi_finance_earning);
        zaiqi_finance_bianxian = (TextView) findViewById(R.id.zaiqi_finance_bianxian);
        zaiqi_finance_xinxi = (TextView) findViewById(R.id.zaiqi_finance_xinxi);
        gson = new Gson();
        zaiqi_finance_layout1.setVisibility(View.GONE);
        MsgWaitDialog.waitdialog(this);
        manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zaiqi_finance_my_back2my:
                ZaiqiFinanceActivity.this.finish();
                break;
            case R.id.zaiqi_finance_bianxian:
                showShPopuWindows(v);
                break;
            default:
                break;
        }
    }

    private void showShPopuWindows(View v) {
        if (shPopuWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.sh_popuwindow, null);
            shts_back = (TextView) view.findViewById(R.id.shts_back);
            shts_btn = (Button) view.findViewById(R.id.shts_btn);
            shts_btn.setText("提交");

            int width = (int) (manager.getDefaultDisplay().getWidth() * (0.8));
            shPopuWindow = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        shPopuWindow.setFocusable(true);
        shPopuWindow.setOutsideTouchable(true);
        shPopuWindow.setAnimationStyle(R.style.AnimationMy);
        shPopuWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        shPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        shPopuWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        shts_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shPopuWindow.dismiss();
            }
        });
        shts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shPopuWindow.dismiss();
                Intent intent = new Intent(ZaiqiFinanceActivity.this, ConfirmRedemptionActivity.class);
                intent.putExtra("ord_id", pid);
                intent.putExtra("select", 2);
                startActivity(intent);
                ZaiqiFinanceActivity.this.finish();

            }
        });
    }
}
