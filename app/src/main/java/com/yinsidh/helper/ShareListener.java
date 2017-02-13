package com.yinsidh.helper;

import android.content.Context;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;


/**
 * Created by User on 2016/11/8.
 */
public class ShareListener implements IUiListener {

    private Context context;
    public ShareListener(Context context){
        this.context=context;
    }
    @Override
    public void onCancel() {
        // TODO Auto-generated method stub
        ToastHelper.showToast(context,"分享取消");
    }

    @Override
    public void onComplete(Object arg0) {
        // TODO Auto-generated method stub
        ToastHelper.showToast(context,"分享成功");

    }
    @Override
    public void onError(UiError arg0) {
        // TODO Auto-generated method stub
        ToastHelper.showToast(context,"分享出错");
    }

}
