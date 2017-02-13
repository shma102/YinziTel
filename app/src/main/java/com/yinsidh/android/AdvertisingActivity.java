package com.yinsidh.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yinsidh.user.fragment.IndexActivity;
import com.yinsidh.utils.ScreenUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;


public class AdvertisingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView advertising_img;
    private TextView advertising_skip;
    private int time = 5;
    private Timer timer;
    private TimerTask timerTask;
    private String str_img = "";
    private String img_h = "";
    private String img_w = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);

        initView();
        initListener();
        str_img = getIntent().getStringExtra("img_path");
        img_h = getIntent().getStringExtra("img_h");
        img_w = getIntent().getStringExtra("img_w");
        if (!TextUtils.isEmpty(str_img) && !TextUtils.isEmpty(img_h) && !TextUtils.isEmpty(img_w)) {
            Bitmap bitmap = getLoacalBitmap(str_img);
            ViewGroup.LayoutParams lp = advertising_img.getLayoutParams();
            lp.width = ScreenUtils.getScreenWidth(AdvertisingActivity.this);
            double img_width = Double.valueOf(img_w);
            double img_height = Double.valueOf(img_h);
            if (lp.width >= img_width) {
                lp.height = (int) ((lp.width / img_width) * img_height);
            } else {
                lp.height = (int) ((lp.width * img_height) / img_width);
            }
            Log.e("tag", "into" + Integer.valueOf(img_h) + "--" + Integer.valueOf(img_w) + "==" + lp.height + "==" + lp.width);
            advertising_img.setLayoutParams(lp);
            advertising_img.setImageBitmap(bitmap);
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (time <= 0) {
                    timer.cancel();
                    ToIndex();
                }
                time -= 1;
            }
        };
        timer.schedule(timerTask, 0, 1000);


    }

    private void initListener() {
        advertising_skip.setOnClickListener(this);
    }

    private void initView() {
        advertising_img = (ImageView) findViewById(R.id.advertising_img);
        advertising_skip = (TextView) findViewById(R.id.advertising_skip);
        timer = new Timer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advertising_skip:
                time = 0;
                break;
            default:
                break;
        }
    }

    private void ToIndex() {
        Intent intent = new Intent(AdvertisingActivity.this, IndexActivity.class);
        startActivity(intent);
        AdvertisingActivity.this.finish();
    }

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
