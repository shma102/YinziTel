package com.yinsidh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.bean.BuyNumberBean;
import com.yinsidh.helper.OnChildItemClickListener;

import java.util.List;

/**
 * Created by User on 2016/11/5.
 */
public class BuyNumberAdapter extends RecyclerView.Adapter<BuyNumberAdapter.ViewHolder> {
    private List<BuyNumberBean> list;
    private Context context;
    private LayoutInflater inflater;
    private OnChildItemClickListener listener;
    private int layoutPosition;

    public BuyNumberAdapter(List<BuyNumberBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public void onItemClickListener(OnChildItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.buynumber_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.number.setText(list.get(position).getName());
        holder.money.setText(list.get(position).getMoney());
        holder.itemView.setTag(position);
        if (list.get(position).getTime().equals("mouth")) {
            holder.time.setText("/月");
        } else if (list.get(position).getTime().equals("year")) {
            holder.time.setText("/年");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取当前点击的位置
                layoutPosition = holder.getLayoutPosition();
                notifyDataSetChanged();
                listener.onItemClick(holder.itemView, (int) holder.itemView.getTag());
            }
        });
        //更改状态
        if (position == layoutPosition) {
            holder.numberItem_radio.setBackgroundResource(R.drawable.checkbox_check);
        } else {
            holder.numberItem_radio.setBackgroundResource(R.drawable.checkbox_uncheck);
        }

    }

    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView money;
        private TextView number;
        private TextView time;
        public TextView numberItem_radio;

        public ViewHolder(View itemView) {
            super(itemView);
            money = (TextView) itemView.findViewById(R.id.buyNumberItem_money);
            number = (TextView) itemView.findViewById(R.id.buyNumberItem_number);
            time = (TextView) itemView.findViewById(R.id.buyNumberItem_time);
            numberItem_radio = (TextView) itemView.findViewById(R.id.numberItem_radio);

        }
    }

}
