package com.yinsidh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.bean.FinancialBean;
import com.yinsidh.helper.OnChildItemClickListener;

import java.util.List;

/**
 * Created by User on 2016/11/8.
 */
public class FinanceGainAdapter extends RecyclerView.Adapter<FinanceGainAdapter.ViewHolder> implements View.OnClickListener {
    private List<FinancialBean> list;
    private Context context;
    private LayoutInflater inflater;
    private OnChildItemClickListener listener;

    public FinanceGainAdapter(List<FinancialBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public void setOnClickListener(OnChildItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.finance_item, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getPro_name());
        holder.shouyi.setText(list.get(position).getPro_rate());
        holder.qixian.setText(list.get(position).getDeadline());
        holder.itemView.setTop(20);
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (Integer) v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView shouyi;
        private TextView qixian;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.financial_item_title);
            shouyi = (TextView) itemView.findViewById(R.id.finance_item_shouyi);
            qixian = (TextView) itemView.findViewById(R.id.finance_item_qixian);
        }
    }
}
