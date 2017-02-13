package com.yinsidh.user.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yinsidh.adapter.BuyNumberAdapter;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.bean.BuyNumberBean;
import com.yinsidh.bean.CommonData;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.OnChildItemClickListener;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.barrenechea.widget.recyclerview.decoration.DividerDecoration;

public class BuyNumberActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int BUYNUMBER = 0;
    private static final int BUYNUMBER_ERROR_LOGIN = 1;
    private static final int BUYNUMBER_ERROR_MESS = 2;
    private RelativeLayout buy_number_layout1;
    private RecyclerView recyclerView;
    private Gson gson;
    private BuyNumberAdapter adapter;
    private TextView buyNumber_empty;
    private LinearLayout buyNumber_title;  //列表顶部信息
    private Button buyNumber_btn;
    private TextView buy_back;  //返回
    private String buy_number = "";
    private String buy_money = "";
    private List<BuyNumberBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_buy_number);
        initView();
        initListener();

        Number_Http();

    }

    private void initView() {
        buy_number_layout1 = (RelativeLayout) findViewById(R.id.buy_number_layout1);
        recyclerView = (RecyclerView) findViewById(R.id.buynumber_recyclerView);
        buyNumber_empty = (TextView) findViewById(R.id.buyNumber_empty);
        buyNumber_title = (LinearLayout) findViewById(R.id.buyNumber_title);
        buy_back = (TextView) findViewById(R.id.buy_back);
        buyNumber_btn = (Button) findViewById(R.id.buyNumber_btn);
        buy_number_layout1.setVisibility(View.GONE);
        MsgWaitDialog.waitdialog(this);
        gson = new Gson();
        list = new ArrayList<BuyNumberBean>();
    }

    private void initListener() {
        buy_back.setOnClickListener(this);
        buyNumber_btn.setOnClickListener(this);
    }

    private void Number_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST, "didnumber", getDidNumberParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if (result.equals("[]")) {
                    MsgWaitDialog.waitdialog_close();
                    buy_number_layout1.setVisibility(View.VISIBLE);
                    buyNumber_empty.setVisibility(View.VISIBLE);
                    buyNumber_empty.setText("暂无可购买的号码了 请等候新的号码上线");
                    recyclerView.setVisibility(View.GONE);
                    buyNumber_title.setVisibility(View.GONE);
                    buyNumber_btn.setVisibility(View.GONE);
                } else {
                    buy_number_layout1.setVisibility(View.VISIBLE);
                    MsgWaitDialog.waitdialog_close();
                    buyNumber_empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    buyNumber_title.setVisibility(View.VISIBLE);
                    buyNumber_btn.setVisibility(View.VISIBLE);

                    Type type = new TypeToken<ArrayList<BuyNumberBean>>() {
                    }.getType();
                    list = gson.fromJson(result, type);
                    buy_number = list.get(0).getName();
                    setAdapter(list);
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(BuyNumberActivity.this, "网络出错了");
            }
        });
    }

    private void setAdapter(final List<BuyNumberBean> list) {

        adapter = new BuyNumberAdapter(list, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerDecoration decoration = new DividerDecoration.Builder(this)
                .setHeight(1.0f)
                .setPadding(0.0f)
                .setColorResource(R.color.gray)
                .build();

        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        adapter.onItemClickListener(new OnChildItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("tag", list.get(position).getName());
                buy_number = list.get(position).getName();
                buy_money = list.get(position).getMoney();
            }
        });

    }

    private Map<String, String> getDidNumberParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "didnumber");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(this, "userpass", ""));
        return params;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_back:
                BuyNumberActivity.this.finish();
                break;
            case R.id.buyNumber_btn:
                MyDialog.setDialog(BuyNumberActivity.this, "提示", "确认购买", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        BuyNumber_Http();

                    }
                });
                break;
            default:
                break;
        }
    }

    private void BuyNumber_Http() {
        MsgWaitDialog.waitdialog(this);
        VolleyRequestUtil.RequestPost(this, URL.HOST, "didpay", getDidPayParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                CommonData commonData = gson.fromJson(result, CommonData.class);
                if (commonData.getStats().equals("ok")) {
                    list.clear();
                    Number_Http();
                    Message message = new Message();
                    message.obj = commonData.getMsg();
                    message.what = BUYNUMBER;
                    handler.sendMessage(message);
                } else if (commonData.getStats().equals("errorlogin")) {
                    Message message = new Message();
                    message.obj = commonData.getMsg();
                    message.what = BUYNUMBER_ERROR_LOGIN;
                    handler.sendMessage(message);
                } else if (commonData.getStats().equals("errormsg")) {
                    Message message = new Message();
                    message.obj = commonData.getMsg();
                    message.what = BUYNUMBER_ERROR_MESS;
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();

                ToastHelper.showToast(BuyNumberActivity.this, "购买失败");
            }

        });
    }

    private Map<String, String> getDidPayParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "didpay");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(this, "userpass", ""));
        params.put("didnumber", buy_number);
        params.put("money", buy_money);
        Log.e("didnumber", buy_money);
        return params;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BUYNUMBER:
                    MyDialog.setOneDialog(BuyNumberActivity.this, "提示", (String) msg.obj, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case BUYNUMBER_ERROR_LOGIN:
                    MyDialog.setDialog(BuyNumberActivity.this, "提示", (String) msg.obj, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SharedPrefsUtil.clearValue(BuyNumberActivity.this);

                            Intent intent = new Intent(BuyNumberActivity.this, LoginActivity.class);
                            startActivity(intent);
                            BuyNumberActivity.this.finish();
                        }
                    });
                    break;
                case BUYNUMBER_ERROR_MESS:
                    MyDialog.setOneDialog(BuyNumberActivity.this, "提示", (String) msg.obj, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
            }
        }
    };
}
