package com.yinsidh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.bean.CallLogBean;
import com.yinsidh.helper.OnChildItemClickListener;

import java.util.List;

/**
 * Created by User on 2016/10/29.
 */
public class MyCallLogAdapter extends RecyclerView.Adapter<MyCallLogAdapter.ViewHolder> implements View.OnClickListener {

    private List<CallLogBean> callLogs;
    private Context context;
    private LayoutInflater inflater;
    private OnChildItemClickListener listener;

    public MyCallLogAdapter(List<CallLogBean> callLogs, Context context) {
        this.callLogs = callLogs;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemListener(OnChildItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_record_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CallLogBean callLog = callLogs.get(position);
        switch (callLog.getType()) {
            case 1:
                holder.call_type
                        .setBackgroundResource(R.drawable.ic_calllog_outgoing_nomal);
                break;
            case 2:
                holder.call_type
                        .setBackgroundResource(R.drawable.ic_calllog_incomming_normal);
                break;
            case 3:
                holder.call_type
                        .setBackgroundResource(R.drawable.ic_calllog_missed_normal);
                break;
        }
        holder.name.setText(callLog.getName());
        holder.number.setText(callLog.getNumber());
        holder.time.setText(callLog.getDate());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView call_type;
        private TextView name;
        private TextView number;
        private TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            call_type = (ImageView) itemView.findViewById(R.id.call_type);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
