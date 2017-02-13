package com.yinsidh.user.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.bean.CommonData;
import com.yinsidh.bean.User;
import com.yinsidh.helper.MsgWaitDialog;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.user.fragment2.BuyNumberActivity;
import com.yinsidh.user.fragment2.CommissionActivity;
import com.yinsidh.user.fragment2.ContactUsActivity;
import com.yinsidh.user.fragment2.ErweimaActivity;
import com.yinsidh.user.fragment2.FinanceProductsActivity;
import com.yinsidh.user.fragment2.HelperActivity;
import com.yinsidh.user.fragment2.InvitationActivity;
import com.yinsidh.user.fragment2.LevelQueryActivity;
import com.yinsidh.user.fragment2.MyFinanceActivity;
import com.yinsidh.user.fragment2.NumberStatsActivity;
import com.yinsidh.user.fragment2.RechargeActivity;
import com.yinsidh.user.fragment2.RechargeThatActivity;
import com.yinsidh.user.fragment2.WithdrawalActivity;
import com.yinsidh.utils.ImageUtil;
import com.yinsidh.utils.KeyboardUtils;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class MyFragment extends Fragment implements View.OnClickListener {

    private static final int CALL = 0;
    private static final int VOICE = 1;
    private static final int EDIT = 2;
    private static final int CALL_ENHANCE = 3;
    private View rootView;
    private SwipeRefreshLayout swipe_ly;
    private ImageView my_headimage;

    private TextView tvNumber, tvState; //号码 会员
    private TextView balance;  //余额
    private TextView commission, financial, earnings;  //佣金 增益金账户 增益金收入
    private TextView jifen, guoqi, haoma, voice;  //积分 期限 号码 声音
    private LinearLayout ll_integral, ll_timeLimit, ll_numb, ll_voice;
    private ImageView splitbutton_on, splitbutton_off;  //呼叫增强
    private LinearLayout zengyichanpin, wodezengyi, yuechongzhi, zhujiaohaoma;  //增益产品 我的增益 余额充值 主叫号码
    private LinearLayout yongjintixian, tixianjilu, goumaihaoma, haomazhuangtai;  //佣金提现 提现记录 号码购买 号码状态
    private LinearLayout bianyinshezhi, jibiechaxun, zifeishuoming, xiugaimima;  //变音设置 级别查询 资费说明 修改密码
    private LinearLayout yaoqinghaoyou, huoqugongzhong, bangzhufankui, lianxiwomen;  //邀请好友 获取公众号 帮助反馈 联系我们


    private TextView exit_account; //退出当前账号

    private String str_earnings, str_financial, str_username, str_callId, str_balance, str_guoqi, str_jifen, str_commission, str_level, str_voice;

    private PopupWindow callPopupWindow;
    private TextView callTitleBack;
    private WindowManager manager;
    private RadioButton benji, suiji, ziding;
    private EditText zidingNumber;
    private Button call_submit;
    private String callId;

    private PopupWindow voicePopupWindow;
    private TextView voiceTitleBack;
    private RadioButton myself, woman, man, child, old, machine;
    private Button voiceSubmit;
    private String voiceId;

    private PopupWindow editPwdPopupWindow;
    private EditText etOldPwd, etNewPwd;
    private String strOldPwd, strNewPwd;
    private Button editPwdSubmit;
    private TextView editTitleBack;

    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fg4, container, false);
        }
        initView(rootView);

        initDate();
        //设置卷内的颜色
        swipe_ly.setColorSchemeResources(R.color.holo_blue_light,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);

        swipe_ly.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "getinfo", getInfoParams(),
                        new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                                VolleyListenerInterface.mErrorListener) {
                            @Override
                            public void onMySuccess(String result) {
                                if (!TextUtils.isEmpty(result)) {
                                    Log.e("tag", result.toString());
                                    swipe_ly.setRefreshing(false);
                                    User bean = gson.fromJson(result, User.class);
                                    if (bean.getStats().equals("ok")) {
                                        ToastHelper.showToast(getActivity(), "刷新成功");
                                        saveUser(bean);
                                        str_earnings = SharedPrefsUtil.getValue(getActivity(), "earnings", "");
                                        str_financial = SharedPrefsUtil.getValue(getActivity(), "capital", "");
                                        str_username = SharedPrefsUtil.getValue(getActivity(), "username", "");
                                        str_callId = SharedPrefsUtil.getValue(getActivity(), "callerid", "");
                                        str_balance = SharedPrefsUtil.getValue(getActivity(), "money", "");
                                        str_guoqi = SharedPrefsUtil.getValue(getActivity(), "guoqi", "");
                                        str_jifen = SharedPrefsUtil.getValue(getActivity(), "point", "");
                                        str_commission = SharedPrefsUtil.getValue(getActivity(), "commission", "");
                                        str_level = SharedPrefsUtil.getValue(getActivity(), "userlevel", "");
                                        str_voice = SharedPrefsUtil.getValue(getActivity(), "uservoice", "");

                                        earnings.setText(str_earnings);
                                        financial.setText(str_financial);
                                        tvNumber.setText(str_username);
                                        balance.setText(str_balance);
                                        guoqi.setText(str_guoqi);
                                        jifen.setText(str_jifen);
                                        commission.setText(str_commission);
                                        tvState.setText(str_level);
                                        voice.setText(str_voice);
                                        //初始化号码
                                        if (str_callId.equals("callerid")) {
                                            haoma.setText(SharedPrefsUtil.getValue(getActivity(), "username", ""));
                                        } else if (str_callId.equals("")) {
                                            haoma.setText("随机号码");
                                        } else {
                                            haoma.setText(SharedPrefsUtil.getValue(getActivity(), "callerid", ""));
                                        }
                                    } else if (bean.getStats().equals("errormsg")) {
                                        MyDialog.setDialog(getActivity(), "提示", bean.getMsg(), "取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }, "确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                    } else if (bean.getStats().equals("errorlogin")) {
                                        DidExit(bean.getMsg());
                                    }
                                } else {
                                    swipe_ly.setRefreshing(false);
                                    ToastHelper.showToast(getActivity(), "刷新失败");
                                }

                            }

                            @Override
                            public void onMyError(VolleyError error) {
                                swipe_ly.setRefreshing(false);
                                ToastHelper.showToast(getActivity(), "请检查网络设置");
                            }
                        });
            }


        });

        //添加点击事件
        initListener();

        return rootView;

    }

    private void saveUser(User user) {
        SharedPrefsUtil.putValue(getActivity(), "stats", user.getStats());
        SharedPrefsUtil.putValue(getActivity(), "msg", user.getMsg());
        SharedPrefsUtil.putValue(getActivity(), "username", user.getUsername());
        SharedPrefsUtil.putValue(getActivity(), "callerid", user.getCallerid());
        SharedPrefsUtil.putValue(getActivity(), "money", user.getMoney());
        SharedPrefsUtil.putValue(getActivity(), "bindphone", user.getBindphone());
        SharedPrefsUtil.putValue(getActivity(), "guoqi", user.getGuoqi());
        SharedPrefsUtil.putValue(getActivity(), "point", user.getPoint());
        SharedPrefsUtil.putValue(getActivity(), "commission", user.getCommission());
        SharedPrefsUtil.putValue(getActivity(), "userlevel", user.getUserlevel());
        SharedPrefsUtil.putValue(getActivity(), "uservoice", user.getUservoice());
        SharedPrefsUtil.putValue(getActivity(), "didcall", user.getDidcall());
        SharedPrefsUtil.putValue(getActivity(), "model", user.getModel());

    }

    //登陆接口的参数
    public Map<String, String> loginParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "login");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        params.put("version", "1.0");
        return params;
    }

    private Map<String, String> getInfoParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "getinfo");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        Log.e("tagg", SharedPrefsUtil.getValue(getActivity(), "username", "") + "--" + SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        return params;
    }

    private void initDate() {
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.header);
        float width = (bitmap1.getWidth()) / 2;
        my_headimage.setImageBitmap(ImageUtil.getRoundedCornerBitmap(bitmap1, width));
        str_earnings = SharedPrefsUtil.getValue(getActivity(), "earnings", "");
        str_financial = SharedPrefsUtil.getValue(getActivity(), "capital", "");
        str_username = SharedPrefsUtil.getValue(getActivity(), "username", "");
        str_callId = SharedPrefsUtil.getValue(getActivity(), "callerid", "");
        str_balance = SharedPrefsUtil.getValue(getActivity(), "money", "");
        str_guoqi = SharedPrefsUtil.getValue(getActivity(), "guoqi", "");
        str_jifen = SharedPrefsUtil.getValue(getActivity(), "point", "");
        str_commission = SharedPrefsUtil.getValue(getActivity(), "commission", "");
        str_level = SharedPrefsUtil.getValue(getActivity(), "userlevel", "");
        str_voice = SharedPrefsUtil.getValue(getActivity(), "uservoice", "");

        earnings.setText(str_earnings);
        financial.setText(str_financial);
        tvNumber.setText(str_username);
        balance.setText(str_balance);
        guoqi.setText(str_guoqi);
        jifen.setText(str_jifen);
        commission.setText(str_commission);
        tvState.setText(str_level);
        voice.setText(str_voice);
        //初始化号码
        if (str_callId.equals("callerid")) {
            haoma.setText(SharedPrefsUtil.getValue(getActivity(), "username", ""));
        } else if (str_callId.equals("")) {
            haoma.setText("随机号码");
        } else {
            haoma.setText(SharedPrefsUtil.getValue(getActivity(), "callerid", ""));
        }

        VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "getinfo", getInfoParams(),
                new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener,
                        VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        if (!TextUtils.isEmpty(result)) {
                            Log.e("tag", result.toString());
                            swipe_ly.setRefreshing(false);
                            User bean = gson.fromJson(result, User.class);
                            if (bean.getStats().equals("ok")) {
                                saveUser(bean);
                            } else if (bean.getStats().equals("errormsg")) {
                                MyDialog.setDialog(getActivity(), "提示", bean.getMsg(), "取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }, "确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                            } else if (bean.getStats().equals("errorlogin")) {
                                SharedPrefsUtil.clearValue(getActivity());

                                ToastHelper.showToast(getActivity(), "登陆信息错误");
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } else {
                            swipe_ly.setRefreshing(false);
                            ToastHelper.showToast(getActivity(), "获取信息失败");
                        }

                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        swipe_ly.setRefreshing(false);
                        ToastHelper.showToast(getActivity(), "请检查网络后刷新");
                    }
                });


    }

    private void initListener() {
        jifen.setOnClickListener(this);
        voice.setOnClickListener(this);
        haoma.setOnClickListener(this);
        guoqi.setOnClickListener(this);
        zengyichanpin.setOnClickListener(this);
        wodezengyi.setOnClickListener(this);
        yuechongzhi.setOnClickListener(this);
        zhujiaohaoma.setOnClickListener(this);
        yongjintixian.setOnClickListener(this);
        tixianjilu.setOnClickListener(this);
        goumaihaoma.setOnClickListener(this);
        haomazhuangtai.setOnClickListener(this);
        bianyinshezhi.setOnClickListener(this);
        jibiechaxun.setOnClickListener(this);
        zifeishuoming.setOnClickListener(this);
        xiugaimima.setOnClickListener(this);
        yaoqinghaoyou.setOnClickListener(this);
        huoqugongzhong.setOnClickListener(this);
        bangzhufankui.setOnClickListener(this);
        lianxiwomen.setOnClickListener(this);

        ll_integral.setOnClickListener(this);
        ll_timeLimit.setOnClickListener(this);
        ll_numb.setOnClickListener(this);
        ll_voice.setOnClickListener(this);

        exit_account.setOnClickListener(this);

        splitbutton_off.setOnClickListener(this);
        splitbutton_on.setOnClickListener(this);
    }

    private void initView(View rootView) {
        swipe_ly = (SwipeRefreshLayout) rootView.findViewById(R.id.id_swipe_ly);
        my_headimage = (ImageView) rootView.findViewById(R.id.my_headimage);
        tvNumber = (TextView) rootView.findViewById(R.id.my_tv_number);
        tvState = (TextView) rootView.findViewById(R.id.my_tv_state);
        balance = (TextView) rootView.findViewById(R.id.my_tv_balance);
        commission = (TextView) rootView.findViewById(R.id.my_tv_commission);
        financial = (TextView) rootView.findViewById(R.id.my_tv_financial);
        earnings = (TextView) rootView.findViewById(R.id.my_tv_earnings);
        jifen = (TextView) rootView.findViewById(R.id.my_tv_jifen);
        guoqi = (TextView) rootView.findViewById(R.id.my_tv_guoqi);
        haoma = (TextView) rootView.findViewById(R.id.my_tv_haoma);
        voice = (TextView) rootView.findViewById(R.id.my_tv_shengyin);
        splitbutton_on = (ImageView) rootView.findViewById(R.id.splitbutton_on);
        splitbutton_off = (ImageView) rootView.findViewById(R.id.splitbutton_off);
        zengyichanpin = (LinearLayout) rootView.findViewById(R.id.zengyichanpin);
        wodezengyi = (LinearLayout) rootView.findViewById(R.id.wodezengyi);
        yuechongzhi = (LinearLayout) rootView.findViewById(R.id.yuechongzhi);
        zhujiaohaoma = (LinearLayout) rootView.findViewById(R.id.zhujiaohaoma);
        yongjintixian = (LinearLayout) rootView.findViewById(R.id.yongjintixian);
        tixianjilu = (LinearLayout) rootView.findViewById(R.id.tixianjilu);
        goumaihaoma = (LinearLayout) rootView.findViewById(R.id.goumaihaoma);
        haomazhuangtai = (LinearLayout) rootView.findViewById(R.id.haomazhuangtai);
        bianyinshezhi = (LinearLayout) rootView.findViewById(R.id.bianyinshezhi);
        jibiechaxun = (LinearLayout) rootView.findViewById(R.id.jibiechaxun);
        zifeishuoming = (LinearLayout) rootView.findViewById(R.id.zifeishuoming);
        xiugaimima = (LinearLayout) rootView.findViewById(R.id.xiugaimima);
        yaoqinghaoyou = (LinearLayout) rootView.findViewById(R.id.yaoqinghaoyou);
        huoqugongzhong = (LinearLayout) rootView.findViewById(R.id.huoqugongzhong);
        bangzhufankui = (LinearLayout) rootView.findViewById(R.id.bangzhufankui);
        lianxiwomen = (LinearLayout) rootView.findViewById(R.id.lianxiwomen);
        exit_account = (TextView) rootView.findViewById(R.id.exit_account);

        ll_integral = (LinearLayout) rootView.findViewById(R.id.ll_integral);
        ll_timeLimit = (LinearLayout) rootView.findViewById(R.id.ll_timeLimit);
        ll_numb = (LinearLayout) rootView.findViewById(R.id.ll_numb);
        ll_voice = (LinearLayout) rootView.findViewById(R.id.ll_voice);

        manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        gson = new Gson();

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.ll_integral:
                Intent intent_jifen = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent_jifen);
                break;
            case R.id.ll_timeLimit:
                Intent intent_guoqi = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent_guoqi);
                break;
            case R.id.ll_numb:
                MsgWaitDialog.waitdialog(getActivity());
                VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "checklevel", getCheckLevelParams(), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        MsgWaitDialog.waitdialog_close();
                        CommonData commonData = gson.fromJson(result, CommonData.class);
                        if (commonData.getStats().equals("supermode")) {
                            showCallPopupWindow(v);
                        } else if (commonData.getStats().equals("errorlogin")) {
                            DidExit(commonData.getMsg());

                        } else if (commonData.getStats().equals("normalmode")) {
                            ziding.setVisibility(View.GONE);
                            showCallPopupWindow(v);
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        MsgWaitDialog.waitdialog_close();
                        ToastHelper.showToast(getActivity(), "网络出错了！");
                    }
                });

                break;
            case R.id.ll_voice:
                showVoicePopupWindow(v);
                break;
            /**
             * 增益产品 我的增益
             */
            case R.id.zengyichanpin:
                Intent intent_financeproduct = new Intent(getActivity(), FinanceProductsActivity.class);
                intent_financeproduct.putExtra("advertising", "");
                startActivity(intent_financeproduct);
                break;
            case R.id.wodezengyi:
                Intent intent_myfinance = new Intent(getActivity(), MyFinanceActivity.class);
                startActivity(intent_myfinance);
                break;

            case R.id.yuechongzhi:
                Intent intent_yue = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent_yue);
                break;
            case R.id.zhujiaohaoma:
                MsgWaitDialog.waitdialog(getActivity());
                VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "checklevel", getCheckLevelParams(), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
                    @Override
                    public void onMySuccess(String result) {
                        MsgWaitDialog.waitdialog_close();
                        CommonData commonData = gson.fromJson(result, CommonData.class);
                        if (commonData.getStats().equals("supermode")) {
                            showCallPopupWindow(v);
                        } else if (commonData.getStats().equals("errorlogin")) {
                            DidExit(commonData.getMsg());

                        } else if (commonData.getStats().equals("normalmode")) {
                            ziding.setVisibility(View.GONE);
                            showCallPopupWindow(v);
                        }
                    }

                    @Override
                    public void onMyError(VolleyError error) {
                        MsgWaitDialog.waitdialog_close();
                        ToastHelper.showToast(getActivity(), "网络出错了！");
                    }
                });
                break;
            case R.id.yongjintixian:
                Intent intent_tixian = new Intent(getActivity(), CommissionActivity.class);
                startActivity(intent_tixian);
                break;
            case R.id.tixianjilu:
                Intent intent_jilu = new Intent(getActivity(), WithdrawalActivity.class);
                startActivity(intent_jilu);
                break;
            case R.id.goumaihaoma:
                Intent intent_goumai = new Intent(getActivity(), BuyNumberActivity.class);
                startActivity(intent_goumai);
                break;
            case R.id.haomazhuangtai:
                Intent intent_zhuangtai = new Intent(getActivity(), NumberStatsActivity.class);
                startActivity(intent_zhuangtai);
                break;
            case R.id.bianyinshezhi:
                showVoicePopupWindow(v);
                break;
            case R.id.jibiechaxun:
                Intent intent_jibie = new Intent(getActivity(), LevelQueryActivity.class);
                startActivity(intent_jibie);
                break;
            case R.id.zifeishuoming:
                Intent intent_zifei = new Intent(getActivity(), RechargeThatActivity.class);
                startActivity(intent_zifei);
                break;
            case R.id.xiugaimima:
                showEditPwdPopupWindow(v);
                break;
            case R.id.yaoqinghaoyou:
                Intent intent_yaoqing = new Intent(getActivity(), InvitationActivity.class);
                startActivity(intent_yaoqing);
                break;
            case R.id.huoqugongzhong:
                Intent intent_weixin = new Intent(getActivity(), ErweimaActivity.class);
                startActivity(intent_weixin);
                break;
            case R.id.bangzhufankui:
                Intent intent_help = new Intent(getActivity(), HelperActivity.class);
                startActivity(intent_help);
                break;
            case R.id.lianxiwomen:
                Intent intent_lianxi = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent_lianxi);
                break;
            case R.id.splitbutton_off:
                CallEnhance_Http();
                break;
            case R.id.splitbutton_on:
                splitbutton_off.setVisibility(View.VISIBLE);
                splitbutton_on.setVisibility(View.INVISIBLE);
                break;
            case R.id.exit_account:
                MyDialog.setDialog(getActivity(), "提示", "退出当前账号", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPrefsUtil.clearValue(getActivity());

                        ToastHelper.showToast(getActivity(), "注销成功");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void DidExit(String s) {
        MyDialog.setDialog(getActivity(), "提示", s, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPrefsUtil.clearValue(getActivity());

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private Map<String, String> getCheckLevelParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "checklevel");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        return params;
    }

    private void CallEnhance_Http() {
        MsgWaitDialog.waitdialog(getActivity());
        VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "checkcall", getCheckCallParams(), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                if (!TextUtils.isEmpty(result)) {
                    CommonData data = gson.fromJson(result, CommonData.class);
                    Message message = new Message();
                    message.what = CALL_ENHANCE;
                    message.obj = data;
                    handler.sendMessage(message);

                } else {


                }
            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                ToastHelper.showToast(getActivity(), "开启失败");
            }
        });

    }

    private Map<String, String> getCheckCallParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "checkcall");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        return params;
    }

    private void showEditPwdPopupWindow(View v) {
        if (editPwdPopupWindow == null) {
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.activity_edit_password, null);
            etOldPwd = (EditText) view.findViewById(R.id.edit_oldpass);
            etNewPwd = (EditText) view.findViewById(R.id.edit_newpass);
            editPwdSubmit = (Button) view.findViewById(R.id.button_edit_submit);
            editTitleBack = (TextView) view.findViewById(R.id.button_edit_back);
            int width = (int) (manager.getDefaultDisplay().getWidth() * (0.7));
            editPwdPopupWindow = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
        editPwdPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        editPwdPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        editPwdPopupWindow.setFocusable(true);
        editPwdPopupWindow.setAnimationStyle(R.style.AnimationMy);
        editPwdPopupWindow.setOutsideTouchable(false);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        editPwdPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        editPwdPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        editTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(getActivity(), etOldPwd);
                editPwdPopupWindow.dismiss();
            }
        });
        editPwdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOldPwd = etOldPwd.getText().toString().trim();
                strNewPwd = etNewPwd.getText().toString().trim();
                if (!strOldPwd.equals(SharedPrefsUtil.getValue(getActivity(), "userpass", ""))) {
                    MyDialog.setOneDialog(getActivity(),
                            "提示", "原始密码不正确", "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                } else {
                    if (strNewPwd.length() < 5 || strNewPwd.length() > 18) {
                        MyDialog.setOneDialog(getActivity(),
                                "提示", "密码需大于5位小于15位", "确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        return;
                    } else {
                        EditPwd_Http(strOldPwd, strNewPwd);
                    }
                }
            }
        });


    }

    private void EditPwd_Http(String oldPwd, String newPwd) {
        MsgWaitDialog.waitdialog(getActivity());

        VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "editpass", getEditPwdParams(oldPwd, newPwd), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                if (!TextUtils.isEmpty(result)) {
                    Log.e("tag", result);
                    CommonData commonData = gson.fromJson(result, CommonData.class);
                    if (commonData.getStats().equals("ok")) {
                        Message message = new Message();
                        message.obj = commonData;
                        message.what = EDIT;
                        handler.sendMessage(message);
                    } else if (commonData.getStats().equals("errorlogin")) {
                        DidExit(commonData.getMsg());
                    } else if (commonData.getStats().equals("errormsg")) {
                        setErrorMess(commonData.getMsg());
                    }
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                ToastHelper.showToast(getActivity(), "网络出错了");
            }

        });
    }

    private Map<String, String> getEditPwdParams(String oldPwd, String newPwd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "editpass");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        params.put("oldpass", oldPwd);
        params.put("newpass", newPwd);
        return params;
    }

    private void Voice_Http() {
        MsgWaitDialog.waitdialog(getActivity());

        VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "setvoice", getVoiceParams(), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                if (!TextUtils.isEmpty(result)) {
                    CommonData commonData = gson.fromJson(result, CommonData.class);
                    if (commonData.getStats().equals("ok")) {
                        Message message = new Message();
                        message.obj = commonData;
                        message.what = VOICE;
                        handler.sendMessage(message);
                        //设置声音
                        if (voiceId.equals("0")) {
                            voice.setText("原声音");
                        } else if (voiceId.equals("1")) {
                            voice.setText("男变女");
                        } else if (voiceId.equals("2")) {
                            voice.setText("女变男");
                        } else if (voiceId.equals("3")) {
                            voice.setText("变小孩");
                        } else if (voiceId.equals("4")) {
                            voice.setText("变老人");
                        } else if (voiceId.equals("5")) {
                            voice.setText("机器人");
                        }
                    } else if (commonData.getStats().equals("errorlogin")) {
                        DidExit(commonData.getMsg());
                    } else if (commonData.getStats().equals("errormsg")) {
                        setErrorMess(commonData.getMsg());
                    }
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                ToastHelper.showToast(getActivity(), "网络出错了");
            }

        });
    }

    private Map<String, String> getVoiceParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "setvoice");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        params.put("uservoice", voiceId);
        return params;
    }

    private void showVoicePopupWindow(View v) {
        if (voicePopupWindow == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_voice_setting, null, false);
            voiceTitleBack = (TextView) view.findViewById(R.id.voice_title_back);
            voiceSubmit = (Button) view.findViewById(R.id.voice_submit);
            myself = (RadioButton) view.findViewById(R.id.voice_myself);
            woman = (RadioButton) view.findViewById(R.id.voice_woman);
            man = (RadioButton) view.findViewById(R.id.voice_man);
            child = (RadioButton) view.findViewById(R.id.voice_child);
            old = (RadioButton) view.findViewById(R.id.voice_old);
            machine = (RadioButton) view.findViewById(R.id.voice_machine);

            int width = (int) (manager.getDefaultDisplay().getWidth() * (0.7));
            voicePopupWindow = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        //设置popupwindow可点击
        voicePopupWindow.setFocusable(true);
        //设置允许在外部点消失
        voicePopupWindow.setOutsideTouchable(false);
        //设置弹出和消失动画
        voicePopupWindow.setAnimationStyle(R.style.AnimationMy);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        voicePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        voicePopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        //退出当前popupWindow
        voiceTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(getActivity());
                voicePopupWindow.dismiss();

            }
        });
        //自己的声音
        myself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                myself.setChecked(true);

            }
        });
        //女士的声音
        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                woman.setChecked(true);

            }
        });
        //男士的声音
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                man.setChecked(true);

            }
        });
        //小孩的声音
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                child.setChecked(true);

            }
        });
        //老人的声音
        old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                old.setChecked(true);

            }
        });
        //机器的声音
        machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickVoiceRadioButton();
                machine.setChecked(true);

            }
        });
        //提交
        voiceSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myself.isChecked() || woman.isChecked() || man.isChecked() || old.isChecked() || child.isChecked() || machine.isChecked()) {

                    if (myself.isChecked()) {
                        voiceId = "0";
                    }
                    if (woman.isChecked()) {
                        voiceId = "1";
                    }
                    if (man.isChecked()) {
                        voiceId = "2";
                    }
                    if (child.isChecked()) {
                        voiceId = "3";
                    }
                    if (old.isChecked()) {
                        voiceId = "4";
                    }
                    if (machine.isChecked()) {
                        voiceId = "5";
                    }
                    Voice_Http();
                } else {
                    MyDialog.setDialog(getActivity(), "提示", "请选择变音", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

    }

    private void unclickVoiceRadioButton() {
        myself.setChecked(false);
        woman.setChecked(false);
        man.setChecked(false);
        child.setChecked(false);
        old.setChecked(false);
        machine.setChecked(false);
    }

    private void Number_Http() {
        VolleyRequestUtil.RequestPost(getActivity(), URL.HOST, "setcallerid", getCallerIdParams(), new VolleyListenerInterface(getActivity(), VolleyListenerInterface.mListener, VolleyListenerInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                MsgWaitDialog.waitdialog_close();
                if (!TextUtils.isEmpty(result)) {
                    CommonData commonData = gson.fromJson(result, CommonData.class);
                    if (commonData.getStats().equals("ok")) {
                        Message message = new Message();
                        message.obj = commonData;
                        message.what = CALL;
                        handler.sendMessage(message);
                        //设置号码
                        if (callId.equals("callerid")) {
                            haoma.setText(SharedPrefsUtil.getValue(getActivity(), "username", ""));

                        } else if (callId.equals("")) {
                            haoma.setText("随机号码");
                        } else {
                            haoma.setText(zidingNumber.getText().toString());
                        }
                    } else if (commonData.getStats().equals("errorlogin")) {
                        DidExit(commonData.getMsg());
                    } else if (commonData.getStats().equals("errormsg")) {
                        setErrorMess(commonData.getMsg());
                    }
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                MsgWaitDialog.waitdialog_close();
                ToastHelper.showToast(getActivity(), "网络出错了");
            }

        });
    }

    private Map<String, String> getCallerIdParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "setcallerid");
        params.put("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
        params.put("userpass", SharedPrefsUtil.getValue(getActivity(), "userpass", ""));
        params.put("callerid", callId);
        return params;
    }

    private void showCallPopupWindow(View v) {
        if (callPopupWindow == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_call_setting, null, false);
            callTitleBack = (TextView) view.findViewById(R.id.button_title_back);
            benji = (RadioButton) view.findViewById(R.id.benji);
            suiji = (RadioButton) view.findViewById(R.id.suiji);
            ziding = (RadioButton) view.findViewById(R.id.ziding);
            zidingNumber = (EditText) view.findViewById(R.id.haoma);
            call_submit = (Button) view.findViewById(R.id.setting_submit);
            int width = (int) (manager.getDefaultDisplay().getWidth() * (0.7));
            callPopupWindow = new PopupWindow(view, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        callPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        callPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popupwindow可点击
        callPopupWindow.setFocusable(true);
        //设置允许在外部点消失
        callPopupWindow.setOutsideTouchable(false);
        //设置弹出和消失动画
        callPopupWindow.setAnimationStyle(R.style.AnimationMy);
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        callPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                KeyboardUtils.hideSoftInput(getActivity(), zidingNumber);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        callPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        //退出当前popupWindow
        callTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(getActivity(), zidingNumber);
                callPopupWindow.dismiss();

            }
        });
        //本机号码
        benji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickCallRadioButton();
                benji.setChecked(true);
                callId = "callerid";
            }
        });
        //随机号码
        suiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickCallRadioButton();
                suiji.setChecked(true);
                callId = "";
            }
        });
        //自定号码
        ziding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unclickCallRadioButton();
                ziding.setChecked(true);
                zidingNumber.setVisibility(View.VISIBLE);
            }
        });
        //提交
        call_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断用户是否点击变号按钮
                if (benji.isChecked() || suiji.isChecked() || ziding.isChecked()) {
                    //如果用户点击的是自定义，则判断是否填入号码
                    if (ziding.isChecked()) {
                        if (TextUtils.isEmpty(zidingNumber.getText().toString())) {
                            MyDialog.setOneDialog(getActivity(), "提示", "请输入自定义号码", "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            callId = zidingNumber.getText().toString();
                            MsgWaitDialog.waitdialog(getActivity());
                            Number_Http();
                        }
                    } else {
                        MsgWaitDialog.waitdialog(getActivity());
                        Number_Http();
                    }
                } else {
                    MyDialog.setOneDialog(getActivity(), "提示", "您未做任何选择", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }


            }
        });
    }

    private void unclickCallRadioButton() {
        suiji.setChecked(false);
        ziding.setChecked(false);
        benji.setChecked(false);
        zidingNumber.setVisibility(View.INVISIBLE);
    }

    public void setErrorMess(String s) {
        MyDialog.setOneDialog(getActivity(), "提示", s, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CALL:
                    callPopupWindow.dismiss();
                    String stats = ((CommonData) msg.obj).getStats();
                    String mess = ((CommonData) msg.obj).getMsg();
                    if (stats.equals("errorlogin")) {
                        DidExit(mess);
                    } else if (stats.equals("errormsg")) {
                        MyDialog.setOneDialog(getActivity(), "提示", mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    } else if (stats.equals("ok")) {
                        MyDialog.setOneDialog(getActivity(), "提示", mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    }
                    break;
                case VOICE:
                    voicePopupWindow.dismiss();
                    String voice_stats = ((CommonData) msg.obj).getStats();
                    String voice_mess = ((CommonData) msg.obj).getMsg();
                    if (voice_stats.equals("errorlogin")) {
                        DidExit(voice_mess);

                    } else if (voice_stats.equals("errormsg")) {
                        MyDialog.setOneDialog(getActivity(), "提示", voice_mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    } else if (voice_stats.equals("ok")) {
                        MyDialog.setOneDialog(getActivity(), "提示", voice_mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    }
                    break;
                case EDIT:
                    editPwdPopupWindow.dismiss();
                    String edit_stats = ((CommonData) msg.obj).getStats();
                    String edit_mess = ((CommonData) msg.obj).getMsg();
                    if (edit_stats.equals("errorlogin")) {
                        DidExit(edit_mess);
                    } else if (edit_stats.equals("errormsg")) {
                        MyDialog.setOneDialog(getActivity(), "提示", edit_mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    } else if (edit_stats.equals("ok")) {
                        MyDialog.setDialog(getActivity(), "提示", edit_mess, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SharedPrefsUtil.clearValue(getActivity());
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.putExtra("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
                                intent.putExtra("password", etNewPwd.getText());
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SharedPrefsUtil.clearValue(getActivity());

                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.putExtra("username", SharedPrefsUtil.getValue(getActivity(), "username", ""));
                                intent.putExtra("password", etNewPwd.getText());
                                startActivity(intent);
                                getActivity().finish();

                            }
                        });

                    }
                    break;
                case CALL_ENHANCE:
                    String checkCall_stats = ((CommonData) msg.obj).getStats();
                    String checkCall_mess = ((CommonData) msg.obj).getMsg();
                    if (checkCall_stats.equals("errormsg")) {
                        MyDialog.setOneDialog(getActivity(), "提示", checkCall_mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        splitbutton_off.setVisibility(View.VISIBLE);
                        splitbutton_on.setVisibility(View.GONE);
                    } else if (checkCall_stats.equals("ok")) {
                        MyDialog.setOneDialog(getActivity(), "提示", checkCall_mess, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        splitbutton_off.setVisibility(View.GONE);
                        splitbutton_on.setVisibility(View.VISIBLE);
                        SharedPrefsUtil.putValue(getActivity(), "checkcall", "call");
                    } else if (checkCall_stats.equals("errorlogin")) {
                        DidExit(checkCall_mess);
                    }
                    break;

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callPopupWindow != null && callPopupWindow.isShowing()) {
            callPopupWindow.dismiss();
            callPopupWindow = null;

        }
        if (voicePopupWindow != null && voicePopupWindow.isShowing()) {
            voicePopupWindow.dismiss();
            voicePopupWindow = null;

        }
        if (editPwdPopupWindow != null && editPwdPopupWindow.isShowing()) {
            editPwdPopupWindow.dismiss();
            editPwdPopupWindow = null;

        }
    }
}
