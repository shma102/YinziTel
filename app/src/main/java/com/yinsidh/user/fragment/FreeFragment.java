package com.yinsidh.user.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.yinsidh.adapter.FinanceGainAdapter;
import com.yinsidh.android.R;
import com.yinsidh.application.MyApplication;
import com.yinsidh.bean.FinacialListBean;
import com.yinsidh.bean.FinancialBean;
import com.yinsidh.helper.DividerItemDecoration;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.OnChildItemClickListener;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.user.fragment2.FinanceTyActivity;
import com.yinsidh.user.fragment2.GainDetailsActivity;
import com.yinsidh.utils.ScreenUtils;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeFragment extends Fragment implements View.OnClickListener {
    private LinearLayout financial_product_layout1;
    private ImageView finance_title_image; //顶部图片
    private TextView interest_rate;  //近期年化
    private RecyclerView gain_recyclerView;  //余额增益专区
    private RecyclerView product_recyclerView;  //理财产品专区
    private FinanceGainAdapter gain_adapter;
    private View finance_product_ty;
    private String ty_pid;

    private TextView ty_title;
    private TextView ty_rate;
    private TextView ty_qixian;

    private Gson gson;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_free, container, false);
        initView(rootView);
        initListener();
        FinanceList_Http();
        return rootView;
    }

    private void initListener() {
        finance_product_ty.setOnClickListener(this);

    }

    private void initView(View rootView) {
        gson = new Gson();
        financial_product_layout1 = (LinearLayout) rootView.findViewById(R.id.financial_product_layout1);

        finance_title_image = (ImageView) rootView.findViewById(R.id.finance_title_image);
        interest_rate = (TextView) rootView.findViewById(R.id.interest_rate);
        gain_recyclerView = (RecyclerView) rootView.findViewById(R.id.yuezengyi_recyclerView);
        product_recyclerView = (RecyclerView) rootView.findViewById(R.id.licaichanpin_recyclerView);
        finance_product_ty = rootView.findViewById(R.id.finance_product_ty);

        ty_title = (TextView) rootView.findViewById(R.id.financial_ty_title);
        ty_rate = (TextView) rootView.findViewById(R.id.finance_ty_rate);
        ty_qixian = (TextView) rootView.findViewById(R.id.finance_ty_qixian);
        financial_product_layout1.setVisibility(View.GONE);
        MsgWaitDialog.waitdialog(getActivity());
    }

    private void FinanceList_Http() {
        VolleyRequestUtil.RequestPost(getActivity(), URL.HOST_LC, "product_list", getFinanceParams(), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if (!result.equals("[]")) {
                    financial_product_layout1.setVisibility(View.VISIBLE);
                    MsgWaitDialog.waitdialog_close();
                    final FinacialListBean listBean = gson.fromJson(result, FinacialListBean.class);
                    Log.e("listbean", listBean.getImg() + "--" + listBean.getProduct().get(0).toString());
                    Glide.with(getActivity()).load(listBean.getImg()).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            int imageWidth = resource.getWidth();
                            int imageHeight = resource.getHeight();
                            int height = ScreenUtils.getScreenWidth(getActivity()) * imageHeight / imageWidth;
                            ViewGroup.LayoutParams para = finance_title_image.getLayoutParams();
                            para.height = height;
                            finance_title_image.setLayoutParams(para);
                            Glide.with(getActivity()).load(listBean.getImg()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(finance_title_image);
                        }
                    });
                    interest_rate.setText(listBean.getRate());
                    ty_title.setText(listBean.getTy().getPro_name());
                    ty_rate.setText(listBean.getTy().getPro_rate());
                    ty_qixian.setText(listBean.getTy().getDeadline());
                    ty_pid = listBean.getTy().getId();
                    setGainAdapter(listBean.getProduct());
                    setProductAdapter(listBean.getMatters());
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(getActivity(), "网络出错了");

            }
        });
    }

    private void setGainAdapter(final List<FinancialBean> listBean) {
        gain_adapter = new FinanceGainAdapter(listBean, getActivity());
        gain_adapter.setOnClickListener(new OnChildItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), GainDetailsActivity.class);
                intent.putExtra("pid", listBean.get(position).getId());
                startActivity(intent);
            }
        });
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.finance_zhuanqu));
        gain_recyclerView.addItemDecoration(decoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setAutoMeasureEnabled(true);
        gain_recyclerView.setLayoutManager(layoutManager);
        gain_recyclerView.setAdapter(gain_adapter);

    }

    private void setProductAdapter(final List<FinancialBean> listBean) {
        gain_adapter = new FinanceGainAdapter(listBean, getActivity());
        gain_adapter.setOnClickListener(new OnChildItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), GainDetailsActivity.class);
                intent.putExtra("pid", listBean.get(position).getId());
                startActivity(intent);
            }
        });
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL, 20, getResources().getColor(R.color.finance_zhuanqu));
        product_recyclerView.addItemDecoration(decoration);
        product_recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setAutoMeasureEnabled(true);
        product_recyclerView.setLayoutManager(layoutManager);
        product_recyclerView.setAdapter(gain_adapter);
    }

    private Map<String, String> getFinanceParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "product_list");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        return params;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finance_product_ty:
                Intent intent = new Intent(getActivity(), FinanceTyActivity.class);
                intent.putExtra("pid", ty_pid);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getRequestQueue().cancelAll(this);

    }
}
