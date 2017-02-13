package com.yinsidh.android.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.user.fragment2.PayBackActivity;
import com.yinsidh.utils.KeyboardUtils;

/**
 * Created by User on 2016/11/14.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, null);
        api.handleIntent(getIntent(), this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    public void onReq(BaseReq req) {
    }

    public void onResp(BaseResp resp) {
        KeyboardUtils.hideSoftInput(this);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    //成功
                    this.finish();
                    Intent CZ = new Intent(WXPayEntryActivity.this, PayBackActivity.class);
                    startActivity(CZ);
                    ToastHelper.showToast(WXPayEntryActivity.this, "支付成功");
                    break;
                case -1:
                    //失败
                    this.finish();
                    ToastHelper.showToast(WXPayEntryActivity.this, "支付失败");
                    break;
                case -2:
                    this.finish();
                    ToastHelper.showToast(WXPayEntryActivity.this, "支付取消");
                    // 取消
                    break;
                default:
                    break;
            }
        }


    }
}
