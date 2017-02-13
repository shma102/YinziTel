package com.yinsidh.user.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yinsidh.adapter.NumberStatsAdapter;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.bean.CommonData;
import com.yinsidh.bean.NumberStatsBean;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
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

public class NumberStatsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout number_stats_layout1;
    private TextView stats_back2my;
    private TextView number_stats_empty;
    private RecyclerView recyclerView;

    private NumberStatsAdapter adapter;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_number_stats);

        initView();

        initDate();

        initListener();
    }

    private void initListener() {
        stats_back2my.setOnClickListener(this);
    }

    private void initDate() {
        VolleyRequestUtil.RequestPost(this, URL.HOST, "didownbuy", getDidOwnParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if ((result.contains("[")) && (!result.equals("[]"))) {  //返回json 包含【】 并且不等于【】
                    number_stats_layout1.setVisibility(View.VISIBLE);
                    MsgWaitDialog.waitdialog_close();
                    number_stats_empty.setVisibility(View.GONE);
                    Type type = new TypeToken<ArrayList<NumberStatsBean>>() {
                    }.getType();
                    List<NumberStatsBean> list = gson.fromJson(result, type);
                    setAdapter(list);

                } else if (result.equals("[]")) {
                    //当前没有号码
                    number_stats_layout1.setVisibility(View.GONE);
                    MsgWaitDialog.waitdialog_close();
                    number_stats_empty.setVisibility(View.VISIBLE);
                    number_stats_empty.setText("您暂无可使用的号码 快去购买吧");
                } else if (!result.contains("[")) {
                    MsgWaitDialog.waitdialog_close();
                    CommonData commonData = gson.fromJson(result, CommonData.class);
                    if (commonData.getStats().equals("errorlogin")) {
                        MyDialog.setDialog(NumberStatsActivity.this, "提示", commonData.getMsg(), "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SharedPrefsUtil.clearValue(NumberStatsActivity.this);

                                Intent intent = new Intent(NumberStatsActivity.this, LoginActivity.class);
                                startActivity(intent);
                                NumberStatsActivity.this.finish();
                            }
                        });
                    }
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(NumberStatsActivity.this, "网络出错了");
            }
        });
    }

    private void setAdapter(List<NumberStatsBean> list) {
        adapter = new NumberStatsAdapter(list, NumberStatsActivity.this);
        DividerDecoration decoration = new DividerDecoration.Builder(NumberStatsActivity.this)
                .setColorResource(R.color.gray)
                .setHeight(1.0f)
                .setPadding(0.0f)
                .build();
        recyclerView.addItemDecoration(decoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(NumberStatsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private Map<String, String> getDidOwnParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "didownbuy");
        params.put("username", SharedPrefsUtil.getValue(NumberStatsActivity.this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(NumberStatsActivity.this, "userpass", ""));
        return params;
    }

    private void initView() {
        number_stats_layout1 = (LinearLayout) findViewById(R.id.number_stats_layout1);
        stats_back2my = (TextView) findViewById(R.id.stats_back2my);
        number_stats_empty = (TextView) findViewById(R.id.number_stats_empty);
        recyclerView = (RecyclerView) findViewById(R.id.number_stats_list);

        gson = new Gson();
        number_stats_layout1.setVisibility(View.GONE);
        MsgWaitDialog.waitdialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stats_back2my:
                NumberStatsActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
