package com.yinsidh.user.fragment2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.android.R;
import com.yinsidh.bean.CommonData;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.StatusBarHelper;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.KeyboardUtils;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class LevelQueryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button levelQuery_btn;
    private EditText levelQurey_tv;
    private TextView back2my;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.setTranslucentStatus(this);
        setContentView(R.layout.activity_level_query);

        initView();
        initListener();
    }

    private void initListener() {
        levelQuery_btn.setOnClickListener(this);
        back2my.setOnClickListener(this);
    }

    private void initView() {
        levelQuery_btn = (Button) findViewById(R.id.levelQuery_Button);
        levelQurey_tv = (EditText) findViewById(R.id.levelQueryNumber);
        back2my = (TextView) findViewById(R.id.level_back2my);

        gson = new Gson();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.levelQuery_Button:
                if (!TextUtils.isEmpty(levelQurey_tv.getText().toString())) {
                    MsgWaitDialog.waitdialog(LevelQueryActivity.this);
                    Query_Http();
                } else {
                    MyDialog.setOneDialog(LevelQueryActivity.this, "提示", "查询号码不能为空", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

                break;
            case R.id.level_back2my:
                KeyboardUtils.hideSoftInput(LevelQueryActivity.this);
                LevelQueryActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private void Query_Http() {
        VolleyRequestUtil.RequestPost(this, URL.HOST, "checkphone", getCheckPhoneParams(), new VolleyListenerInterface(this, VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                CommonData commonData = gson.fromJson(result, CommonData.class);
                MyDialog.setOneDialog(LevelQueryActivity.this, "提示", commonData.getMsg(), "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                ToastHelper.showToast(LevelQueryActivity.this, "网络出错了");
            }
        });
    }

    private Map<String, String> getCheckPhoneParams() {
        String username = SharedPrefsUtil.getValue(this, "username", "");
        String password = SharedPrefsUtil.getValue(this, "userpass", "");
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "checkphone");
        params.put("username", username);
        params.put("userpass", password);
        params.put("phone", levelQurey_tv.getText().toString());
        return params;
    }
}
