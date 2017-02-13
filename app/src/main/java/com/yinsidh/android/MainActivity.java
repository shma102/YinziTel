package com.yinsidh.android;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yinsidh.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private MyViewPagerAdapter viewAdapter;
    private List<View> views;
    private ImageView[] dots;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //初始化四张图片对应的四个页面
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.guide_a, null));
        views.add(inflater.inflate(R.layout.guide_b, null));
        views.add(inflater.inflate(R.layout.guide_c, null));
        views.add(inflater.inflate(R.layout.guide_d, null));
        views.add(inflater.inflate(R.layout.guide_e, null));

        viewAdapter = new MyViewPagerAdapter(views, this);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(viewAdapter); //设置适配器
        vp.setOnPageChangeListener(this);
    }

    //初始化圆点
    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }
    //设置当前圆点状态
    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentDot(position);
    }

}
