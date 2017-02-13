package com.yinsidh.user.fragment2;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;


public class ErweimaActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView weixin_back2my,open_weixin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_erweima);

        inirView();

        initListener();
    }

    private void initListener() {
        weixin_back2my.setOnClickListener(this);
        open_weixin.setOnClickListener(this);
    }

    private void inirView() {
        weixin_back2my= (TextView) findViewById(R.id.weixin_back2my);
        open_weixin= (TextView) findViewById(R.id.open_weixin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weixin_back2my:
                ErweimaActivity.this.finish();
                break;
            case R.id.open_weixin:
                try {
                    Intent intentwechat = new Intent();
                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                    intentwechat.setAction(Intent.ACTION_MAIN);
                    intentwechat.addCategory(Intent.CATEGORY_LAUNCHER);
                    intentwechat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentwechat.setComponent(cmp);
                    startActivity(intentwechat);
                } catch (Exception intentwechat) {
                    ToastHelper.showToast(this,"请安装微信");
                }
                break;
            default:
                break;
        }
    }
}
