package com.yinsidh.user.fragment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.helper.StatusBarHelper;


public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView contact_back2my;
    private RelativeLayout bangzhu_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_contact_us);

        initView();

        initListener();
    }

    private void initListener() {
        contact_back2my.setOnClickListener(this);
        bangzhu_layout.setOnClickListener(this);
    }

    private void initView() {
        contact_back2my = (TextView) findViewById(R.id.contact_back2my);
        bangzhu_layout = (RelativeLayout) findViewById(R.id.shiyongbangzhu);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_back2my:
                ContactUsActivity.this.finish();
                break;
            case R.id.shiyongbangzhu:
                Intent intent = new Intent(ContactUsActivity.this, HelperActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
