package com.yinsidh.helper;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import com.yinsidh.android.R;

/**
 * Created by User on 2016/11/14.
 */
public class StatusBarHelper {
    public static void setTranslucentStatus(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            context.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            context.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(context);
            tintManager.setStatusBarTintEnabled(true);
            // 设置状态栏的颜色
            tintManager.setStatusBarTintResource(R.color.title);
            context.getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }

}
