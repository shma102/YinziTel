package com.yinsidh.user.fragment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.utils.URL;


public class HelperActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    private WebSettings settings;
    private TextView back2my;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_helper);
        initView();
        initDate();
        webView.loadUrl(URL.HOST_HELPER);
        MsgWaitDialog.waitdialog(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                MsgWaitDialog.waitdialog_close();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                ToastHelper.showToast(HelperActivity.this, "网络出错了");
            }
        });
        initListener();

    }

    private void initListener() {
        back2my.setOnClickListener(this);
    }

    private void initDate() {
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setBuiltInZoomControls(true);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.helper_webview);
        back2my = (TextView) findViewById(R.id.helper_back2my);

        settings = webView.getSettings();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.helper_back2my:
                HelperActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
