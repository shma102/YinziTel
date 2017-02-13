package com.yinsidh.user.fragment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yinsidh.adapter.WithDrawalAdapter;
import com.yinsidh.android.R;
import com.yinsidh.bean.WithDrawMoneyBean;
import com.yinsidh.helper.MsgWaitDialog;
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

public class WithdrawalActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout withdawal_layout1;
    private RecyclerView recyclerView;
    private List<WithDrawMoneyBean> list;
    private TextView withdrawal_back2my;
    private TextView NoteForWithCash_empty;
    private LinearLayout NotesWithDraw_Title;
    private WithDrawalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_withdrawal);

        initView();
        initListener();
        WithDrawal_Http();
    }

    private void WithDrawal_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST, "cash_log", getCashLogParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("tag", result);
                if (result.equals("[]")) {
                    withdawal_layout1.setVisibility(View.VISIBLE);
                    MsgWaitDialog.waitdialog_close();
                    recyclerView.setVisibility(View.GONE);
                    NoteForWithCash_empty.setVisibility(View.VISIBLE);
                    NoteForWithCash_empty.setText("您还没有提现记录");
                } else {
                    withdawal_layout1.setVisibility(View.VISIBLE);
                    MsgWaitDialog.waitdialog_close();
                    NoteForWithCash_empty.setVisibility(View.GONE);
                    NotesWithDraw_Title.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    try {
                        List<WithDrawMoneyBean> e = new ArrayList<WithDrawMoneyBean>();
                        Type type = new TypeToken<ArrayList<WithDrawMoneyBean>>() {
                        }.getType();
                        e = gson.fromJson(result, type);
                        for (WithDrawMoneyBean bean : e) {
                            list.add(bean);
                        }
                        Log.e("listsize", list.size() + "");
                        setAdapter(list);

                    } catch (JsonSyntaxException e1) {
                        e1.printStackTrace();
                    }
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(WithdrawalActivity.this, "网络出错了");
            }
        });
    }

    private void setAdapter(List<WithDrawMoneyBean> list) {
        adapter = new WithDrawalAdapter(list, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerDecoration decoration = new DividerDecoration.Builder(this)
                .setColorResource(R.color.gray)
                .setHeight(1.0f)
                .setPadding(0.0f)
                .build();
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private Map<String, String> getCashLogParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "cash_log");
        params.put("username", SharedPrefsUtil.getValue(WithdrawalActivity.this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(WithdrawalActivity.this, "userpass", ""));
        return params;
    }

    private void initListener() {
        withdrawal_back2my.setOnClickListener(this);
    }

    private void initView() {
        withdawal_layout1 = (LinearLayout) findViewById(R.id.withdawal_layout1);
        recyclerView = (RecyclerView) findViewById(R.id.noteForWithCash_list);
        withdrawal_back2my = (TextView) findViewById(R.id.withdrawal_back2my);
        NoteForWithCash_empty = (TextView) findViewById(R.id.NoteForWithCash_empty);
        NotesWithDraw_Title = (LinearLayout) findViewById(R.id.NotesWithDraw_Title);

        list = new ArrayList<WithDrawMoneyBean>();

        withdawal_layout1.setVisibility(View.GONE);
        MsgWaitDialog.waitdialog(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdrawal_back2my:
                WithdrawalActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
