package com.yinsidh.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.utils.URL;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    private TextView not_agree, agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_register);

        initView();
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView.loadUrl(URL.HOST_REGISTER);
        MsgWaitDialog.waitdialog(this);
        //设置Web视图
        webView.setWebViewClient(new webViewClient() {
            //网页加载完成的操作
            @Override
            public void onPageFinished(WebView view, String url) {
                MsgWaitDialog.waitdialog_close();
                //设置按钮为可点击状态
                agree.setClickable(true);
                agree.setBackgroundResource(R.drawable.login_selector5);
                agree.setEnabled(true);
                not_agree.setClickable(true);
                not_agree.setBackgroundResource(R.drawable.login_selector5);
                not_agree.setEnabled(true);
            }
        });
        agree.setOnClickListener(this);
        not_agree.setOnClickListener(this);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.register_webview);
        agree = (TextView) findViewById(R.id.agree);
        not_agree = (TextView) findViewById(R.id.not_agree);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.not_agree:
                RegisterActivity.this.finish();
                break;
            case R.id.agree:
                Intent intent = new Intent(this, RegisterMessActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
                break;
            default:
                break;
        }
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
