package com.yinsidh.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by User on 2016/11/1.
 */
public class ToastHelper {
    public static void showToast(Context context,String mess) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
    }
}
