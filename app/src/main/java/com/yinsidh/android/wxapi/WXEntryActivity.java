package com.yinsidh.android.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yinsidh.helper.ToastHelper;

/**
 * Created by User on 2016/11/14.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, null);
        api.handleIntent(getIntent(), this);
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq req)
    {
    }

    @Override
    public void onResp(BaseResp resp)
    {
        switch (resp.errCode)
        {
            case BaseResp.ErrCode.ERR_OK:
                this.finish();
                ToastHelper.showToast(WXEntryActivity.this, "发送成功");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                this.finish();
                ToastHelper.showToast(WXEntryActivity.this, "发送取消");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                this.finish();
                ToastHelper.showToast(WXEntryActivity.this, "发送被拒绝");
                break;
            default:
                this.finish();
                ToastHelper.showToast(WXEntryActivity.this, "发送失败");
                break;
        }

    }

}
