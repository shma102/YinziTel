package com.yinsidh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yinsidh.android.LoginActivity;
import com.yinsidh.android.R;
import com.yinsidh.bean.CommonData;
import com.yinsidh.bean.NumberStatsBean;
import com.yinsidh.helper.MyDialog;
import com.yinsidh.helper.ToastHelper;
import com.yinsidh.helper.VolleyListenerInterface;
import com.yinsidh.utils.SharedPrefsUtil;
import com.yinsidh.utils.URL;
import com.yinsidh.utils.VolleyRequestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2016/11/8.
 */
public class NumberStatsAdapter extends RecyclerView.Adapter<NumberStatsAdapter.ViewHolder> {

    private static final int ON_OK = 0;
    private static final int ON_ERROR = 1;
    private static final int OFF_OK = 2;
    private static final int OFF_ERROR = 3;
    private List<NumberStatsBean> list;
    private Context context;
    private LayoutInflater inflater;
    private Gson gson;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ON_OK:
                    MyDialog.setOneDialog((Activity) context, "提示",
                            (String) msg.obj, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    break;
                case ON_ERROR:
                    MyDialog.setOneDialog((Activity) context, "提示",
                            (String) msg.obj, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPrefsUtil.clearValue(context);
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                }
                            });
                    break;
                case OFF_OK:
                    MyDialog.setDialog((Activity) context, "提示",
                            (String) msg.obj, "取消",
                            new DialogInterface.OnClickListener() {
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
                    break;
                case OFF_ERROR:
                    MyDialog.setDialog((Activity) context, "提示",
                            (String) msg.obj, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPrefsUtil.clearValue(context);
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                }
                            });
                    break;
            }
        }
    };

    public NumberStatsAdapter(List<NumberStatsBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        gson = new Gson();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.number_stats_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.stats_number.setText(list.get(position).getName());
        int prior = Integer.valueOf(list.get(position).getPrior());
        switch (prior) {
            case 0:
                holder.splitbutton_off.setVisibility(View.VISIBLE);
                holder.splitbutton_on.setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.splitbutton_on.setVisibility(View.VISIBLE);
                holder.splitbutton_off.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
        holder.splitbutton_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDialog.setDialog((Activity) context, "提示",
                        "您将关闭" + list.get(position).getName() + "这个号码",
                        "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String off_number = list.get(position).getName();
                                String username = SharedPrefsUtil.getValue(context, "username", "");
                                String userpass = SharedPrefsUtil.getValue(context, "password", "");
                                VolleyRequestUtil.RequestPost(context, URL.HOST, "didstats",
                                        getDidStatsParams(off_number, username, userpass),
                                        new VolleyListenerInterface(context,
                                                VolleyListenerInterface.mListener,
                                                VolleyListenerInterface.mErrorListener) {
                                            @Override
                                            public void onMySuccess(String result) {
                                                if (!result.equals("[]")) {
                                                    CommonData data = gson.fromJson(result, CommonData.class);
                                                    Message message;

                                                    if (data.getStats().equals("ok")) {
                                                        holder.splitbutton_on.setVisibility(View.INVISIBLE);
                                                        holder.splitbutton_off.setVisibility(View.VISIBLE);
                                                        message = handler.obtainMessage();
                                                        message.what = ON_OK;
                                                        message.obj = data.getMsg();
                                                    } else {
                                                        message = handler.obtainMessage();
                                                        message.what = ON_OK;
                                                        message.obj = data.getMsg();
                                                    }
                                                    handler.sendMessage(message);
                                                }

                                            }

                                            @Override
                                            public void onMyError(VolleyError error) {
                                                ToastHelper.showToast(context, "购买失败");

                                            }
                                        });

                            }
                        });

            }
        });
        holder.splitbutton_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDialog.setDialog((Activity) context, "提示",
                        "您将开通" + list.get(position).getName() + "这个号码",
                        "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String off_number = list.get(position).getName();
                                String username = SharedPrefsUtil.getValue(context, "username", "");
                                String userpass = SharedPrefsUtil.getValue(context, "password", "");
                                VolleyRequestUtil.RequestPost(context, URL.HOST, "didstats",
                                        getDoStatsParams(off_number, username, userpass),
                                        new VolleyListenerInterface(context,
                                                VolleyListenerInterface.mListener,
                                                VolleyListenerInterface.mErrorListener) {
                                            @Override
                                            public void onMySuccess(String result) {
                                                if (!result.equals("[]")) {
                                                    CommonData data = gson.fromJson(result, CommonData.class);
                                                    Message message;
                                                    if (data.getStats().equals("ok")) {
                                                        holder.splitbutton_on.setVisibility(View.VISIBLE);
                                                        holder.splitbutton_off.setVisibility(View.INVISIBLE);
                                                        message = handler.obtainMessage();
                                                        message.what = ON_OK;
                                                        message.obj = data.getMsg();
                                                    } else {
                                                        message = handler.obtainMessage();
                                                        message.what = ON_OK;
                                                        message.obj = data.getMsg();
                                                    }
                                                    handler.sendMessage(message);
                                                }

                                            }

                                            @Override
                                            public void onMyError(VolleyError error) {
                                                ToastHelper.showToast(context, "购买失败");
                                            }
                                        });

                            }
                        });

            }
        });


    }

    private Map<String, String> getDoStatsParams(String off_number, String username, String userpass) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "didstats");
        params.put("username", username);
        params.put("userpass", userpass);
        params.put("didnumber", off_number);
        params.put("didstats", "1");
        return params;
    }

    private Map<String, String> getDidStatsParams(String off_number, String username, String userpass) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("action", "didstats");
        params.put("username", username);
        params.put("userpass", userpass);
        params.put("didnumber", off_number);
        params.put("didstats", "0");
        return params;
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView stats_number;
        private ImageView splitbutton_on, splitbutton_off;

        public ViewHolder(View itemView) {
            super(itemView);
            stats_number = (TextView) itemView.findViewById(R.id.stats_number);
            splitbutton_on = (ImageView) itemView.findViewById(R.id.splitbutton_on);
            splitbutton_off = (ImageView) itemView.findViewById(R.id.splitbutton_off);
        }
    }
}
