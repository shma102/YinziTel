package com.yinsidh.user.fragment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.bean.GainDetailsBean;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class FinanceTyActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout finance_ty_layout1;
    private String pid;
    private Gson gson;
    private TextView finance_ty_touzi;  //立即投资

    private TextView licai_back;  // 返回按钮
    private TextView gain_details_title;  //标题
    private TextView finance_ty_jine; //输入金额
    private TextView gain_details_yucun_tv;  //预存
    private TextView gain_details_shouyi_tv;  //产品收益
    private TextView gain_details_dengji_tv;   //风险等级
    private TextView gain_details_qrsj;  //确认时间
    private TextView gain_details_zysj;  //增益时间
    private TextView gain_details_gmfs;  //购买方式
    private TextView gain_details_txfs;  //体现方式
    private TextView gain_details_txsj;  //提现时间
    private TextView gain_details_txgz;  //提现规则
    private TextView gain_details_tbrs;  //投标人数
    private TextView gain_details_fxts;  //风险提示
    private TextView gain_details_cpjs_xq_tv;  //产品介绍详情
    private TextView gain_details_aqbz_xq_tv;  //安全保障详情
    private TextView gain_details_cjwt_xq_tv;  //常见问题详情

    private RelativeLayout gain_details_mrxz;  //买入须知
    private View gain_details_mrxz_line;
    private LinearLayout gain_details_mrxz_layout;

    private RelativeLayout gain_details_txxz;  //体现须知
    private View gain_details_txxz_line;
    private LinearLayout gain_details_txxz_layout;

    private LinearLayout gain_details_cpjs; //产品介绍
    private TextView gain_details_cpjs_img; //产品介绍
    private LinearLayout gain_details_cpjs_xq; //产品介绍详情
    private boolean cpjs_flag = false;

    private LinearLayout gain_details_aqbz; //安全保障
    private TextView gain_details_aqbz_img; //安全保障
    private LinearLayout gain_details_aqbz_xq; //安全保障详情
    private boolean aqbz_flag = false;

    private LinearLayout gain_details_cjwt; //常见问题
    private TextView gain_details_cjwt_img;
    private LinearLayout gain_details_cjwt_xq; //常见问题详情
    private boolean cjwt_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_finance_ty);

        initView();

        initListener();


        pid = getIntent().getStringExtra("pid");
        if (!TextUtils.isEmpty(pid)) {
            Log.e("tag", pid);
        }

        GainDetails_Http();
    }

    private void initListener() {
        licai_back.setOnClickListener(this);
        gain_details_txxz.setOnClickListener(this);
        gain_details_mrxz.setOnClickListener(this);
        gain_details_cpjs.setOnClickListener(this);
        gain_details_aqbz.setOnClickListener(this);
        gain_details_cjwt.setOnClickListener(this);

        finance_ty_touzi.setOnClickListener(this);


    }

    private void initView() {
        finance_ty_layout1 = (RelativeLayout) findViewById(R.id.finance_ty_layout1);
        gson = new Gson();
        licai_back = (TextView) findViewById(R.id.licai_back);
        gain_details_title = (TextView) findViewById(R.id.gain_details_title);
        finance_ty_jine = (TextView) findViewById(R.id.finance_ty_jine);
        gain_details_yucun_tv = (TextView) findViewById(R.id.gain_details_yucun_tv);
        gain_details_shouyi_tv = (TextView) findViewById(R.id.gain_details_shouyi_tv);
        gain_details_dengji_tv = (TextView) findViewById(R.id.gain_details_dengji_tv);
        gain_details_qrsj = (TextView) findViewById(R.id.gain_details_qrsj);
        gain_details_zysj = (TextView) findViewById(R.id.gain_details_zysj);
        gain_details_gmfs = (TextView) findViewById(R.id.gain_details_gmfs);
        gain_details_txfs = (TextView) findViewById(R.id.gain_details_txfs);
        gain_details_txsj = (TextView) findViewById(R.id.gain_details_txsj);
        gain_details_txgz = (TextView) findViewById(R.id.gain_details_txgz);
        gain_details_tbrs = (TextView) findViewById(R.id.gain_details_tbrs);
        gain_details_fxts = (TextView) findViewById(R.id.gain_details_fxts);
        gain_details_cpjs_xq_tv = (TextView) findViewById(R.id.gain_details_cpjs_xq_tv);
        gain_details_aqbz_xq_tv = (TextView) findViewById(R.id.gain_details_aqbz_xq_tv);
        gain_details_cjwt_xq_tv = (TextView) findViewById(R.id.gain_details_cjwt_xq_tv);

        gain_details_mrxz = (RelativeLayout) findViewById(R.id.gain_details_mrxz);
        gain_details_mrxz_line = (View) findViewById(R.id.gain_details_mrxz_line);
        gain_details_mrxz_layout = (LinearLayout) findViewById(R.id.gain_details_mrxz_layout);

        gain_details_txxz = (RelativeLayout) findViewById(R.id.gain_details_txxz);
        gain_details_txxz_line = (View) findViewById(R.id.gain_details_txxz_line);
        gain_details_txxz_layout = (LinearLayout) findViewById(R.id.gain_details_txxz_layout);

        gain_details_cpjs = (LinearLayout) findViewById(R.id.gain_details_cpjs);
        gain_details_cpjs_img = (TextView) findViewById(R.id.gain_details_cpjs_img);
        gain_details_cpjs_xq = (LinearLayout) findViewById(R.id.gain_details_cpjs_xq);
        gain_details_aqbz = (LinearLayout) findViewById(R.id.gain_details_aqbz);
        gain_details_aqbz_img = (TextView) findViewById(R.id.gain_details_aqbz_img);
        gain_details_aqbz_xq = (LinearLayout) findViewById(R.id.gain_details_aqbz_xq);
        gain_details_cjwt = (LinearLayout) findViewById(R.id.gain_details_cjwt);
        gain_details_cjwt_img = (TextView) findViewById(R.id.gain_details_cjwt_img);
        gain_details_cjwt_xq = (LinearLayout) findViewById(R.id.gain_details_cjwt_xq);

        finance_ty_touzi = (TextView) findViewById(R.id.finance_ty_touzi);

        finance_ty_layout1.setVisibility(View.GONE);
        MsgWaitDialog.waitdialog(this);
    }

    private void GainDetails_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST_LC, "product_details", getProductDetailsParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    finance_ty_layout1.setVisibility(View.VISIBLE);
                    MsgWaitDialog.waitdialog_close();
                    GainDetailsBean bean = gson.fromJson(result, GainDetailsBean.class);
                    if (!bean.getId().equals("")) {

                        Log.e("bean", bean.toString());
                        initDate(bean);
                    } else {
                        MyDialog.setDialog(FinanceTyActivity.this, "提示", "登陆信息错误", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SharedPrefsUtil.clearValue(FinanceTyActivity.this);
                                Intent intent = new Intent(FinanceTyActivity.this, LoginActivity.class);
                                startActivity(intent);
                                FinanceTyActivity.this.finish();
                            }
                        });
                    }
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(FinanceTyActivity.this, "网络出错了");
            }
        });
    }

    private void initDate(GainDetailsBean bean) {
        gain_details_title.setText(bean.getName());
        gain_details_yucun_tv.setText(bean.getPrice() + "元");
        gain_details_shouyi_tv.setText(bean.getDeadline());
        gain_details_dengji_tv.setText(bean.getRisk());
        gain_details_qrsj.setText(bean.getAccrual_time());
        gain_details_zysj.setText(bean.getExpire());
        gain_details_gmfs.setText(bean.getRemit_type());
        gain_details_txfs.setText(bean.getCash_type());
        gain_details_txsj.setText(bean.getGet_out());
        gain_details_txgz.setText(bean.getGet_rule());
        gain_details_tbrs.setText(bean.getPeople());
        gain_details_fxts.setText("\t\t" + bean.getHint());
        gain_details_cpjs_xq_tv.setText("\t\t" + bean.getDetails());
        gain_details_aqbz_xq_tv.setText("\t\t" + bean.getSafety());
        String question = bean.getFaq().replace("/n", "\n");
        gain_details_cjwt_xq_tv.setText(question);

    }

    private Map<String, String> getProductDetailsParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "product_details");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("pid", pid);
        return params;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.licai_back:
                FinanceTyActivity.this.finish();
                break;
            case R.id.gain_details_mrxz:
                gain_details_mrxz_line.setVisibility(View.VISIBLE);
                gain_details_txxz_line.setVisibility(View.GONE);
                gain_details_mrxz_layout.setVisibility(View.VISIBLE);
                gain_details_txxz_layout.setVisibility(View.GONE);
                break;
            case R.id.gain_details_txxz:
                gain_details_mrxz_line.setVisibility(View.GONE);
                gain_details_txxz_line.setVisibility(View.VISIBLE);
                gain_details_mrxz_layout.setVisibility(View.GONE);
                gain_details_txxz_layout.setVisibility(View.VISIBLE);

                break;
            case R.id.gain_details_cpjs:
                if (!cpjs_flag) {
                    gain_details_cpjs_xq.setVisibility(View.VISIBLE);
                    gain_details_cpjs_img.setBackgroundResource(R.drawable.up);
                    cpjs_flag = true;
                } else {
                    gain_details_cpjs_xq.setVisibility(View.GONE);
                    gain_details_cpjs_img.setBackgroundResource(R.drawable.down);
                    cpjs_flag = false;
                }

                break;
            case R.id.gain_details_aqbz:
                if (!aqbz_flag) {
                    gain_details_aqbz_xq.setVisibility(View.VISIBLE);
                    gain_details_aqbz_img.setBackgroundResource(R.drawable.up);
                    aqbz_flag = true;
                } else {
                    gain_details_aqbz_xq.setVisibility(View.GONE);
                    gain_details_aqbz_img.setBackgroundResource(R.drawable.down);
                    aqbz_flag = false;
                }

                break;
            case R.id.gain_details_cjwt:
                if (!cjwt_flag) {
                    gain_details_cjwt_xq.setVisibility(View.VISIBLE);
                    gain_details_cjwt_img.setBackgroundResource(R.drawable.up);
                    cjwt_flag = true;
                } else {
                    gain_details_cjwt_xq.setVisibility(View.GONE);
                    gain_details_cjwt_img.setBackgroundResource(R.drawable.down);
                    cjwt_flag = false;
                }
                break;
            case R.id.finance_ty_touzi:
                if (finance_ty_jine.getText().toString().equals("")) {
                    MyDialog.setOneDialog(FinanceTyActivity.this, "提示", "请输入您要体验的金额", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    int i = Integer.valueOf(finance_ty_jine.getText().toString());
                    if (i >= 100 && i <= 1000) {
                        Intent intent = new Intent(FinanceTyActivity.this, InvestmentActivity.class);
                        intent.putExtra("pid", pid);
                        intent.putExtra("money", i);
                        startActivity(intent);
                    } else {
                        MyDialog.setOneDialog(FinanceTyActivity.this, "提示", "体验金额需大于100小于1000", "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
                break;
            default:
                break;

        }
    }
}