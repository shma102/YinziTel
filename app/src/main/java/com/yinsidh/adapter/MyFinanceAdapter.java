package com.yinsidh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.bean.MyFinanceZaiqiBean;
import com.yinsidh.helper.OnChildItemClickListener;

import java.util.List;

/**
 * Created by User on 2016/11/10.
 */
public class MyFinanceAdapter extends RecyclerView.Adapter<MyFinanceAdapter.ViewHolder> implements View.OnClickListener {

    private List<MyFinanceZaiqiBean> list;
    private Context context;
    private LayoutInflater inflater;
    private OnChildItemClickListener fenhong_listener, shuhui_listener,item_listener;

    public MyFinanceAdapter(List<MyFinanceZaiqiBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }
    public void setItemClickListener(OnChildItemClickListener listener) {
        this.item_listener = listener;
    }
    public void setFhClickListener(OnChildItemClickListener listener) {
        this.fenhong_listener = listener;
    }

    public void setShClickListener(OnChildItemClickListener listener) {
        this.shuhui_listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_finance_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.myfinancial_item_title.setText(list.get(position).getPro_name());
        holder.myfinance_item_shouyi.setText(list.get(position).getOrd_rate());
        holder.myfinance_item_qixian.setText(list.get(position).getDays());
        holder.myfinance_item_jine.setText(list.get(position).getOrd_earnings());
        holder.myfinance_item_number.setText(list.get(position).getOrd_num());

        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(position);
        holder.my_finance_item_shuhui.setTag(position);
        holder.my_finance_item_fenhong.setTag(position);
        holder.my_finance_item_fenhong.setOnClickListener(this);
        holder.my_finance_item_shuhui.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.my_finance_item_fenhong:
                if(fenhong_listener!=null)
                    fenhong_listener.onItemClick(v, (Integer) v.getTag());
                break;
            case R.id.my_finance_item_shuhui:
                if(shuhui_listener!=null)
                    shuhui_listener.onItemClick(v, (Integer) v.getTag());
                break;
            default:
                if(item_listener!=null){
                    item_listener.onItemClick(v, (Integer) v.getTag());
                }
                break;

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView myfinancial_item_title;
        private TextView myfinance_item_shouyi;
        private TextView myfinance_item_qixian;
        private TextView myfinance_item_jine;
        private TextView myfinance_item_number;
        private TextView my_finance_item_fenhong;
        private TextView my_finance_item_shuhui;

        public ViewHolder(View itemView) {
            super(itemView);
            myfinancial_item_title = (TextView) itemView.findViewById(R.id.myfinancial_item_title);
            myfinance_item_shouyi = (TextView) itemView.findViewById(R.id.myfinance_item_shouyi);
            myfinance_item_qixian = (TextView) itemView.findViewById(R.id.myfinance_item_tianshu);
            myfinance_item_jine = (TextView) itemView.findViewById(R.id.myfinance_item_jine);
            my_finance_item_fenhong = (TextView) itemView.findViewById(R.id.my_finance_item_fenhong);
            my_finance_item_shuhui = (TextView) itemView.findViewById(R.id.my_finance_item_shuhui);
            myfinance_item_number = (TextView) itemView.findViewById(R.id.myfinance_item_number);
        }

    }
}
