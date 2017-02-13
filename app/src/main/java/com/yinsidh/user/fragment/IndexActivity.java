package com.yinsidh.user.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yinsidh.android.R;
import com.yinsidh.helper.StatusBarHelper;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends FragmentActivity implements OnClickListener {

    //四个fragment页面
    private ContactFragment contactFragment;
    private RechargeFragment rechargeFragment;
    private MyFragment myFragment;
    private CallFragment callFragment;

    //底部导航栏的四个布局
    private RelativeLayout call_layout;
    private RelativeLayout contact_layout;
    private RelativeLayout recharge_layout;
    private RelativeLayout my_layout;

    //底部导航栏的四张图片
    private ImageView call_image;
    private ImageView contact_image;
    private ImageView recharge_image;
    private ImageView my_image;

    public TextView tv_call;
    private TextView tv_contacts, tv_recharge, tv_my; //底部导航栏的文字
    private int tv_color_normal, tv_color; //文字点击和未点击的颜色

    private List<Fragment> fragmentList; //fragment集合
    public static boolean isShowKeyboar = true;
    public static boolean isShowAnimation = true;
    private TranslateAnimation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_index);

        initViews();
        initData();
        call_layout.setOnClickListener(this);
        contact_layout.setOnClickListener(this);
        recharge_layout.setOnClickListener(this);
        my_layout.setOnClickListener(this);


    }

    private void initData() {
        //设置底部导航栏文字 点击和未点击的颜色
        tv_color_normal = getResources().getColor(R.color.tv_color_normal);
        tv_color = getResources().getColor(R.color.tv_color);
        //将各个fragment添加到集合中方便管理
        fragmentList.add(callFragment);
        fragmentList.add(contactFragment);
        fragmentList.add(rechargeFragment);
        fragmentList.add(myFragment);

        switchFragment(0);  //显示第一个fragment页面

    }

    //双击退出
    private static boolean isExit = false;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出隐私电话",
                    Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            System.exit(0);
        }
    }

    public void initViews() {
        call_image = (ImageView) findViewById(R.id.call_image);
        contact_image = (ImageView) findViewById(R.id.contact_image);
        recharge_image = (ImageView) findViewById(R.id.recharge_image);
        my_image = (ImageView) findViewById(R.id.my_image);
        call_layout = (RelativeLayout) findViewById(R.id.call_layout);
        contact_layout = (RelativeLayout) findViewById(R.id.contact_layout);
        recharge_layout = (RelativeLayout) findViewById(R.id.recharge_layout);
        my_layout = (RelativeLayout) findViewById(R.id.my_layout);
        tv_call = (TextView) findViewById(R.id.tv_call);
        tv_contacts = (TextView) findViewById(R.id.tv_contacts);
        tv_my = (TextView) findViewById(R.id.tv_my);
        tv_recharge = (TextView) findViewById(R.id.tv_recharge);

        fragmentList = new ArrayList<Fragment>();

        callFragment = new CallFragment();
        contactFragment = new ContactFragment();
        rechargeFragment = new RechargeFragment();
        myFragment = new MyFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_layout:
                switchFragment(0);
                if (isShowKeyboar) {
                    isShowKeyboar = false;
                    isShowAnimation = true;
                    callFragment.Keyboard_layout.setVisibility(View.GONE);
                    call_image.setImageResource(R.drawable.call_new_check_up);
                    tv_call.setText("展开");
                    animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 1);
                    animation.setDuration(500);
                    animation.setInterpolator(AnimationUtils
                            .loadInterpolator(IndexActivity.this,
                                    android.R.anim.accelerate_decelerate_interpolator));
                    callFragment.Keyboard_layout.startAnimation(animation);

                } else {
                    isShowKeyboar = true;
                    callFragment.Keyboard_layout.setVisibility(View.VISIBLE);
                    call_image.setImageResource(R.drawable.call_new_check_down);
                    tv_call.setText("收起");
                    if (isShowAnimation) {
                        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                                Animation.RELATIVE_TO_PARENT, 0,
                                Animation.RELATIVE_TO_PARENT, 1,
                                Animation.RELATIVE_TO_PARENT, 0);
                        animation.setDuration(500);
                        animation.setInterpolator(AnimationUtils
                                .loadInterpolator(IndexActivity.this,
                                        android.R.anim.accelerate_decelerate_interpolator));
                        callFragment.Keyboard_layout.startAnimation(animation);
                    }
                }
                break;
            case R.id.contact_layout:
                isShowKeyboar = false;
                isShowAnimation = false;
                switchFragment(1);
                break;
            case R.id.recharge_layout:
                isShowKeyboar = false;
                isShowAnimation = false;

                switchFragment(2);
                break;
            case R.id.my_layout:
                isShowKeyboar = false;
                isShowAnimation = false;
                switchFragment(3);
                break;
        }
    }

    //还原所有页面为未点击状态
    public void clearChioce() {
        call_image.setImageResource(R.drawable.call_new_normal);
        contact_image.setImageResource(R.drawable.contactsnormal);
        recharge_image.setImageResource(R.drawable.rechargenormal);
        my_image.setImageResource(R.drawable.mynormal);

        tv_call.setTextColor(tv_color_normal);
        tv_contacts.setTextColor(tv_color_normal);
        tv_recharge.setTextColor(tv_color_normal);
        tv_my.setTextColor(tv_color_normal);
        tv_call.setText("拨号");
    }

    private int currentIndex = 0;  //默认设置当前fragment为第一个页面

    public void switchFragment(int targetIndex) {
        clearChioce();
        //判断是从哪个页面跳转过来的，改变底部导航栏的状态
        switch (targetIndex) {
            case 0:
                call_image.setImageResource(R.drawable.call_new_check_down);
                tv_call.setTextColor(tv_color);
                tv_call.setText("收起");
                break;
            case 1:
                contact_image.setImageResource(R.drawable.contactspress);
                tv_contacts.setTextColor(tv_color);
                break;
            case 2:
                recharge_image.setImageResource(R.drawable.rechargepress);
                tv_recharge.setTextColor(tv_color);
                break;
            case 3:
                my_image.setImageResource(R.drawable.mypress);
                tv_my.setTextColor(tv_color);
                break;
            default:
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = fragmentList.get(currentIndex);  //现在的页面
        Fragment targetFragment = fragmentList.get(targetIndex);  //即将跳转的页面
        //如果即将跳转的页面没有添加到Activity中，则隐藏现在的页面并将该页面添加到Activity中
        // 如果即将跳转的页面已经添加到Activity中，则隐藏现在的页面并显示该页面
        if (!targetFragment.isAdded()) {
            //把已经显示的隐藏
            if (currentIndex == targetIndex) {
                transaction.hide(currentFragment).add(R.id.content, targetFragment).show(targetFragment).commit();
            } else {
                transaction.hide(currentFragment).add(R.id.content, targetFragment).commit();
            }
        } else {
            transaction.hide(currentFragment).show(targetFragment).commit();
        }
        currentIndex = targetIndex;  //使跳转后的页面为现在页面
    }
}
