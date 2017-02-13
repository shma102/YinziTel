package com.yinsidh.user.fragment2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.adapter.HistoryFinanceAdapter;
import com.yinsidh.adapter.MyFinanceAdapter;
import com.yinsidh.android.R;
import com.yinsidh.bean.MyFinanceBean;
import com.yinsidh.bean.MyFinanceZaiqiBean;
import com.yinsidh.helper.DividerItemDecoration;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.OnChildItemClickListener;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;

public class MyFinanceActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout my_finance_layout1;
    private LinearLayout myfinance_zaiqi_layout;
    private LinearLayout myfinance_wangqi_layout;
    private TextView finance_my_back2my;
    private RecyclerView zaiqilicai_recyclerView;
    private RecyclerView wangqilicai_recyclerView;
    private TextView my_tv_financial_ljsy;
    private TextView my_tv_financial_ztzj;
    private MyFinanceAdapter zaiqi_adapter;
    private HistoryFinanceAdapter wangqi_adapter;
    private PopupWindow shPopuWindow;
    private WindowManager manager;
    private TextView shts_back;
    private Button shts_btn;
    private TextView my_finance_ztbj;
    private String ord_id;


    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_my_finance);

        initView();

        initListener();

        MyFinance_Http();

    }

    private void initListener() {
        finance_my_back2my.setOnClickListener(this);
    }

    private void MyFinance_Http() {
           VolleyRequestUtil.RequestPost(this, URL.HOST_LC, "myinfo", getMyInfoParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
               @Override
               public void onMySuccess(String result) {
                   if (!TextUtils.isEmpty(result)) {
                       Log.e("tag", result);
                       my_finance_layout1.setVisibility(View.VISIBLE);
                       MsgWaitDialog.waitdialog_close();
                       MyFinanceBean bean = gson.fromJson(result, MyFinanceBean.class);
                       my_tv_financial_ljsy.setText(bean.getEarnings());
                       my_tv_financial_ztzj.setText(bean.getWcapital());
                       my_finance_ztbj.setText(bean.getCapital());
                       if (bean.getZaiqi().size() > 0) {
                           myfinance_zaiqi_layout.setVisibility(View.VISIBLE);
                           zaiqilicai_recyclerView.setVisibility(View.VISIBLE);
                           setZaiqiAdapter(bean.getZaiqi());
                       }
                       if (bean.getHistory().size() > 0) {
                           myfinance_wangqi_layout.setVisibility(View.VISIBLE);
                           wangqilicai_recyclerView.setVisibility(View.VISIBLE);
                           setHistoryAdapter(bean.getHistory());
                       }
                   }


               }

               @Override
               public void onMyError(VolleyError error) {
                   ToastHelper.showToast(MyFinanceActivity.this, "网络出错了");

               }
           });

    }

    private void setHistoryAdapter(List<MyFinanceZaiqiBean> history) {

        wangqi_adapter = new HistoryFinanceAdapter(history, this);
        setDecaration(wangqilicai_recyclerView);
        wangqilicai_recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        wangqilicai_recyclerView.setLayoutManager(layoutManager);
        wangqilicai_recyclerView.setAdapter(wangqi_adapter);
    }

    private void setZaiqiAdapter(final List<MyFinanceZaiqiBean> zaiqi) {
        zaiqi_adapter = new MyFinanceAdapter(zaiqi, this);
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.gray));
        zaiqilicai_recyclerView.addItemDecoration(decoration);
        zaiqilicai_recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        zaiqilicai_recyclerView.setLayoutManager(layoutManager);
        zaiqilicai_recyclerView.setAdapter(zaiqi_adapter);
        zaiqi_adapter.setFhClickListener(new OnChildItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                double d = Double.valueOf(zaiqi.get(position).getOrd_earnings());
                if (d > 10.0) {
                    Intent intent = new Intent(MyFinanceActivity.this, ConfirmRedemptionActivity.class);
                    ord_id = zaiqi.get(position).getOrd_no();
                    intent.putExtra("ord_id", ord_id);
                    intent.putExtra("select", 1);
                    startActivity(intent);
                    MyFinanceActivity.this.finish();
                } else {
                    MyDialog.setDialog(MyFinanceActivity.this, "提示", "收益金额大于10元才能领取分红", "取消", new DialogInterface.OnClickListener() {
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
        });
        zaiqi_adapter.setShClickListener(new OnChildItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showShPopuWindows(view, zaiqi, position);


            }
        });
        zaiqi_adapter.setItemClickListener(new OnChildItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MyFinanceActivity.this, ZaiqiFinanceActivity.class);
                intent.putExtra("pid", zaiqi.get(position).getOrd_no());
                startActivity(intent);
                MyFinanceActivity.this.finish();
            }
        });
    }

    private void showShPopuWindows(View v, final List<MyFinanceZaiqiBean> zaiqi, final int position) {
        if (shPopuWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.sh_popuwindow, null);
            shts_back = (TextView) view.findViewById(R.id.shts_back);
            shts_btn = (Button) view.findViewById(R.id.shts_btn);

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
                Intent intent = new Intent(MyFinanceActivity.this, ConfirmRedemptionActivity.class);
                ord_id = zaiqi.get(position).getOrd_no();
                intent.putExtra("ord_id", ord_id);
                intent.putExtra("select", 2);
                startActivity(intent);
                MyFinanceActivity.this.finish();

            }
        });
    }

    private void setDecaration(RecyclerView zaiqi_recyclerView) {
        DividerDecoration decoration = new DividerDecoration.Builder(this)
                .setHeight(20.0f)
                .setPadding(0.0f)
                .setColorResource(R.color.gray)
                .build();
        zaiqi_recyclerView.addItemDecoration(decoration);
    }

    private Map<String, String> getMyInfoParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "myinfo");
        params.put("username", SharedPrefsUtil.getValue(MyFinanceActivity.this, "username", ""));
        return params;
    }

    private void initView() {
        my_finance_layout1 = (LinearLayout) findViewById(R.id.my_finance_layout1);
        myfinance_zaiqi_layout = (LinearLayout) findViewById(R.id.myfinance_zaiqi_layout);
        myfinance_wangqi_layout = (LinearLayout) findViewById(R.id.myfinance_wangqi_layout);
        finance_my_back2my = (TextView) findViewById(R.id.finance_my_back2my);
        zaiqilicai_recyclerView = (RecyclerView) findViewById(R.id.zaiqilicai_recyclerView);
        wangqilicai_recyclerView = (RecyclerView) findViewById(R.id.wangqilicai_recyclerView);
        my_tv_financial_ljsy = (TextView) findViewById(R.id.my_tv_financial_ljsy);
        my_tv_financial_ztzj = (TextView) findViewById(R.id.my_tv_financial_ztzj);
        my_finance_ztbj = (TextView) findViewById(R.id.my_finance_ztbj);

        gson = new Gson();
        manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        my_finance_layout1.setVisibility(View.GONE);
        MsgWaitDialog.waitdialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finance_my_back2my:
                MyFinanceActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
