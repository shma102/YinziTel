package com.yinsidh.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.R.attr.value;

/**
 * sharedpreferences工具类
 */
public class SharedPreferencesUtil {
    public final static String ADVERTISING = "Advertising";

    public static void putValue(Context context, String key, String defValue) {
        SharedPreferences.Editor sp = context.getSharedPreferences(ADVERTISING, Context.MODE_PRIVATE).edit();
        sp.putInt(key, value);
        sp.commit();
    }

    public static String getValue(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(ADVERTISING, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }

    public static void clearValue(Context context) {
        SharedPreferences.Editor sp = context.getSharedPreferences(ADVERTISING, Context.MODE_PRIVATE).edit();
        sp.clear();
        sp.commit();
    }
}
