package com.yinsidh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.bean.MyFinanceZaiqiBean;

import java.util.List;

/**
 * Created by User on 2016/11/21.
 */

public class HistoryFinanceAdapter extends RecyclerView.Adapter<HistoryFinanceAdapter.ViewHolder> {

    private List<MyFinanceZaiqiBean> list;
    private Context context;
    private LayoutInflater inflater;

    public HistoryFinanceAdapter(List<MyFinanceZaiqiBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.history_finance_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getPro_name());
        holder.shoyi.setText(list.get(position).getOrd_earnings());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView shoyi;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.history_item_title);
            shoyi = (TextView) itemView.findViewById(R.id.history_item_shouyi_tv);
        }
    }

}
