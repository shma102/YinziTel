package com.yinsidh.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by User on 2016/10/26.
 */
public class MyDialog {

    public static void dialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public static void setDialog(Activity activity, String title, String message, String left, DialogInterface.OnClickListener leftListener, String right, DialogInterface.OnClickListener rightListener) {
        WarningMsgDialog.Builder builder = new WarningMsgDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(right, rightListener).setNegativeButton(left, leftListener);
        builder.create().show();
    }

    public static void setOneDialog(Activity activity, String title, String message, String right, DialogInterface.OnClickListener rightListener) {
        MsgDialog.Builder builder = new MsgDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(right, rightListener);
        builder.create().show();
    }


}
