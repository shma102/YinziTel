package com.yinsidh.user.fragment2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.bean.InvitaionBean;
import com.yinsidh.helper.ShareListener;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvitationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvInvitationNumber;
    private TextView tvInvitationCode;
    private Button btnInvitation;
    private Gson gson;
    private TextView invitation_back2my;
    private PopupWindow popupWindow;
    private LinearLayout qzone_layout, wechat_layout, wechatmoment_layout, qq_layout;

    private IWXAPI iwxapi;
    private Tencent mTencent;
    private ShareListener shareListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_invitation);
        initView();

        Invitation_Http();

        initListener();
    }

    private void Invitation_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST, "checkdistr", getCheckDisterParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    InvitaionBean bean = gson.fromJson(result, InvitaionBean.class);
                    if (!bean.getNumb().equals("")) {
                        tvInvitationCode.setText(bean.getNumb());
                        tvInvitationNumber.setText(bean.getCount());
                    } else {
                        ToastHelper.showToast(InvitationActivity.this, "登陆信息错误");
                        Intent intent = new Intent(InvitationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        InvitationActivity.this.finish();
                    }
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                ToastHelper.showToast(InvitationActivity.this, "网络出错了");
            }
        });
    }

    private Map<String, String> getCheckDisterParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "checkdistr");
        params.put("username", SharedPrefsUtil.getValue(this, "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(this, "userpass", ""));
        return params;
    }

    private void initListener() {
        btnInvitation.setOnClickListener(this);
        invitation_back2my.setOnClickListener(this);
    }

    private void initView() {
        tvInvitationNumber = (TextView) findViewById(R.id.invitation_number);
        tvInvitationCode = (TextView) findViewById(R.id.invitation_code);
        btnInvitation = (Button) findViewById(R.id.invitation_btn);
        invitation_back2my = (TextView) findViewById(R.id.invitation_back2my);
        gson = new Gson();
        iwxapi = WXAPIFactory.createWXAPI(this, "wxa791d98ecb40324b", true);
        iwxapi.registerApp("wxa791d98ecb40324b");
        mTencent = Tencent.createInstance("1105487101", this);
        shareListener = new ShareListener(InvitationActivity.this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invitation_btn:
                showShare(v);
                break;
            case R.id.invitation_back2my:
                InvitationActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private void showShare(View v) {
        if (popupWindow == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.share_layout, null);
            qzone_layout = (LinearLayout) view.findViewById(R.id.share_qzone);
            wechatmoment_layout = (LinearLayout) view.findViewById(R.id.share_wechatmoment);
            wechat_layout = (LinearLayout) view.findViewById(R.id.share_wechat);
            qq_layout = (LinearLayout) view.findViewById(R.id.share_qq);

            int width = WindowManager.LayoutParams.MATCH_PARENT;
            int height = WindowManager.LayoutParams.WRAP_CONTENT;
            popupWindow = new PopupWindow(view, width, height);
        }

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.AnimationPreview);
        popupWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.7f;
        getWindow().setAttributes(layoutParams);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        qzone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showToast(InvitationActivity.this, "分享操作进行中...");
                shareToQzone();

            }
        });

        wechatmoment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showToast(InvitationActivity.this, "分享操作进行中...");
                share2weixin(1);
            }
        });

        qq_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showToast(InvitationActivity.this, "分享操作进行中...");
                shareToQQ();
            }
        });

        wechat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showToast(InvitationActivity.this, "分享操作进行中...");
                share2weixin(0);
            }
        });
    }

    private void share2weixin(int flag) {
        if (!iwxapi.isWXAppInstalled()) {
            Toast.makeText(InvitationActivity.this, "您还未安装微信客户端",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://ystel.13059.com:9090/web/disr.php?disnum=" + tvInvitationCode.getText().toString();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "隐私保护电话";
        msg.description = "隐私保护电话，快来加入我们吧";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

    private void shareToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "隐私保护电话");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "隐私保护电话，快来加入我们吧");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://ystel.13059.com:9090/web/disr.php?disnum=" + tvInvitationCode.getText().toString());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://ystel.13059.com:9090/web/s_logo.jpg");
        mTencent.shareToQQ(InvitationActivity.this, params, shareListener);
    }

    private void shareToQzone() {
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);//类型
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "隐私保护电话");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "隐私保护电话，快来加入我们吧");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://ystel.13059.com:9090/web/disr.php?disnum=" + tvInvitationCode.getText().toString());//必填
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add("http://ystel.13059.com:9090/web/s_logo.jpg");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        mTencent.shareToQzone(InvitationActivity.this, params, new ShareListener(InvitationActivity.this));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 官方文档没这句代码, 但是很重要, 不然不会回调!
        Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, shareListener);
            }
        }
    }
}
