package com.yinsidh.user.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinsidh.android.R;

public class RechargeFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    public static final int TAOCANGOUMAI = 1;
    public static final int MIANFEIHUOQU = 2;
    private int current_fragment;
    private Fragment mFragmentTaocan;
    private Fragment mFragmentMianfei;
    private TextView mTvTaocan;
    private TextView mTvmianfei;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fg3, container, false);
        }

        initView(rootView);
        addListener();

        return rootView;

    }

    private void addListener() {
        mTvTaocan.setOnClickListener(this);
        mTvmianfei.setOnClickListener(this);
    }

    private void initView(View view) {
        mTvmianfei = (TextView) view.findViewById(R.id.recharge_toolbar_mfhq);
        mTvTaocan = (TextView) view.findViewById(R.id.recharge_toolbar_tcgm);

        loadFragment(TAOCANGOUMAI);

    }


    private void loadFragment(int type) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        switch (type) {
            case TAOCANGOUMAI:
                if (mFragmentTaocan == null) {
                    mFragmentTaocan = new TaocanFragment();
                    ft.add(R.id.recharge_content, mFragmentTaocan, "taocangoumai");
                } else {
                    ft.show(mFragmentTaocan);
                }
                if (mFragmentMianfei != null) {
                    ft.hide(mFragmentMianfei);
                }
                current_fragment = TAOCANGOUMAI;
                break;
            case MIANFEIHUOQU:

                if (mFragmentMianfei != null) {
                    ft.remove(mFragmentMianfei);
                }
                mFragmentMianfei = new FreeFragment();
                ft.add(R.id.recharge_content, mFragmentMianfei, "mianfeihuoqu");
                if (mFragmentTaocan != null) {
                    ft.hide(mFragmentTaocan);
                }
                current_fragment = MIANFEIHUOQU;
                break;
        }
        ft.commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_toolbar_mfhq:
                mTvmianfei.setTextColor(getResources().getColor(R.color.title));
                mTvmianfei.setBackgroundResource(R.drawable.right_bold);
                mTvTaocan.setTextColor(getResources().getColor(R.color.white));
                mTvTaocan.setBackgroundResource(R.drawable.left_transparent);
                loadFragment(MIANFEIHUOQU);
                break;
            case R.id.recharge_toolbar_tcgm:
                mTvTaocan.setTextColor(getResources().getColor(R.color.title));
                mTvTaocan.setBackgroundResource(R.drawable.left_bold);
                mTvmianfei.setTextColor(getResources().getColor(R.color.white));
                mTvmianfei.setBackgroundResource(R.drawable.right_transparent);
                loadFragment(TAOCANGOUMAI);
                break;
            default:
                break;
        }
    }

}

