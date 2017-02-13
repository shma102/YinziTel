package com.yinsidh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.bean.WithDrawMoneyBean;

import java.util.List;

/**
 * Created by User on 2016/11/7.
 */
public class WithDrawalAdapter extends RecyclerView.Adapter<WithDrawalAdapter.ViewHolde> {

    private List<WithDrawMoneyBean> list;
    private Context context;
    private LayoutInflater inflater;

    public WithDrawalAdapter(List<WithDrawMoneyBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public ViewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.withdrawal_item, parent, false);
        return new ViewHolde(view);
    }

    @Override
    public void onBindViewHolder(ViewHolde holder, int position) {
        holder.withdawal_item_date.setText(list.get(position).getCash_date());
        holder.withdawal_item_money.setText(list.get(position).getCash_money());
        holder.withdawal_item_way.setText(list.get(position).getCash_name());
        if (list.get(position).getCash_stats().equals("0")) {
            holder.withdawal_item_stats.setText("待处理");
        } else if (list.get(position).getCash_stats().equals("1")) {
            holder.withdawal_item_stats.setText("成功");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolde extends RecyclerView.ViewHolder {

        private TextView withdawal_item_date;
        private TextView withdawal_item_money;
        private TextView withdawal_item_stats;
        private TextView withdawal_item_way;

        public ViewHolde(View itemView) {
            super(itemView);
            withdawal_item_date = (TextView) itemView.findViewById(R.id.withdawal_item_date);
            withdawal_item_money = (TextView) itemView.findViewById(R.id.withdawal_item_money);
            withdawal_item_stats = (TextView) itemView.findViewById(R.id.withdawal_item_stats);
            withdawal_item_way = (TextView) itemView.findViewById(R.id.withdawal_item_way);
        }
    }
}
