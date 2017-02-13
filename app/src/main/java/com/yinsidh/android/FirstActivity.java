package com.yinsidh.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yinsidh.bean.AdvertisingBean;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.user.fragment.IndexActivity;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

public class FirstActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private String str_img = "";
    private String img_h = "";
    private String img_w = "";
    private Bitmap bitmap;
    private String path;
    private String imageName;
    private static final String SHAREDPREFERENCES_NAME = "FirstLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_first);
        initView();
        //动态申请权限
        initDate();
        RxPermissions.getInstance(this)
                .request(
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_CONTACTS)//这里申请了两组权限
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                        } else {
                            //不同意，给提示
                            Toast.makeText(FirstActivity.this, "请同意软件的权限，才能继续提供服务", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        Advertising_Http();

    }

    private void initDate() {
        path = Environment.getExternalStorageDirectory().getPath() + "/dingcheng";
        imageName = "/advertising.png";
    }

    Thread mThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                sp = FirstActivity.this.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
                //判断是不是首次登录，
                if (sp.getBoolean("isFirstIn", true)) {
                    editor = sp.edit();
                    //将登录标志位设置为false，下次登录时不在显示首次登录界面
                    editor.putBoolean("isFirstIn", false);
                    editor.commit();
                    Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                    startActivity(intent);
                    FirstActivity.this.finish();
                } else {
                    if (SharedPrefsUtil.getValue(FirstActivity.this, "username", "").equals("")) {
                        //未登陆过则跳转到登陆界面
                        Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                        startActivity(intent);
                        FirstActivity.this.finish();
                    } else {
                        if (fileIsExists() && !TextUtils.isEmpty(str_img)) {
                            Intent intent = new Intent(FirstActivity.this, AdvertisingActivity.class);
                            intent.putExtra("img_path", path + imageName);
                            intent.putExtra("img_h", img_h);
                            intent.putExtra("img_w", img_w);
                            startActivity(intent);
                            FirstActivity.this.finish();
                        } else {
                            ToIndex();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    });

    private void ToIndex() {
        Intent intent = new Intent(FirstActivity.this, IndexActivity.class);
        startActivity(intent);
        FirstActivity.this.finish();
    }

    private void Advertising_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST, "img", getAdvertisingParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                AdvertisingBean bean = gson.fromJson(result, AdvertisingBean.class);
                if (bean.getIsshow().equals("1")) {
                    str_img = bean.getImg_url();
                    img_h = bean.getImg_h();
                    img_w = bean.getImg_w();
                    new Task().execute(str_img);
                    mThread.start();

                } else {
                    mThread.start();
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                mThread.start();
            }
        });
    }

    private Map<String, String> getAdvertisingParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "img");
        return params;
    }

    private void initView() {
        gson = new Gson();
    }

    /**
     * 异步线程下载图片
     */
    class Task extends AsyncTask<String, Integer, Void> {

        protected Void doInBackground(String... params) {
            bitmap = GetImageInputStream((String) params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Message message = handler.obtainMessage();
            message.what = 0;
            message.obj = bitmap;
            handler.sendMessage(message);
        }

    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public void SavaImage(Bitmap bitmap, String path) {

        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }

        try {
            fileOutputStream = new FileOutputStream(path + imageName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public Bitmap GetImageInputStream(String imageurl) {
        java.net.URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new java.net.URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(true); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj != null) {
                        SavaImage(bitmap, path);
                    }
                    break;
            }
        }
    };

    public boolean fileIsExists() {
        try {
            File f = new File(path + imageName);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }
}
