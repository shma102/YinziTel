package com.yinsidh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinsidh.android.R;
import com.yinsidh.bean.ContactBean;
import com.yinsidh.helper.OnNumberItemClickListener;
import com.yinsidh.utils.PinYinUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/10/29.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private List<ContactBean> list;
    private OnNumberItemClickListener listener;

    public ContactListAdapter(Context context, List<ContactBean> namelist) {
        mInflater = LayoutInflater.from(context);
        this.list = namelist;
    }

    public void setOnItemClickListener(OnNumberItemClickListener listener) {
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.contact_list_item, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //条目文本填充
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.name.setText(list.get(i).getName());
        viewHolder.number.setText(list.get(i).getNumber());
        Log.e("onbind", list.get(i).getNumber());

        /**
         * 判断当前条目与上一个条目姓名首字母是否相同，相同不显示索引，不同则显示
         */
        // 当前字母
        String currentStr = PinYinUtils.getAlpha(PinYinUtils.getPingYin(list.get(i).getName())).toUpperCase();
        // 前面的字母
        String previewStr = "";
        if (i > 0) {
            previewStr = PinYinUtils.getAlpha(PinYinUtils.getPingYin(list.get(i - 1).getName())).toUpperCase();
        }
        if (!previewStr.equals(currentStr)) {
            viewHolder.alpha.setVisibility(View.VISIBLE);
            viewHolder.alpha.setText(currentStr);
        } else {
            viewHolder.alpha.setVisibility(View.GONE);
        }
        viewHolder.itemView.setTag(i);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onNumberClick(v, list.get((int) v.getTag()).getNumber());
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        private TextView number;
        private TextView alpha;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            number = (TextView) itemView.findViewById(R.id.number);
            alpha = (TextView) itemView.findViewById(R.id.alpha);
        }
    }

    /**
     * 获得指定首字母的位置
     *
     * @param c
     * @return
     */
    public int getPositionForSection(char c) {

        for (int i = 0; i < getItemCount(); i++) {
            char firstChar = list.get(i).pinyin.charAt(0);
            if (c >= 'A' && c <= 'Z') {
                c += 32;
            }
            if (firstChar == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateRecyclerView(List<ContactBean> list) {
        if (list == null) {
            this.list = new ArrayList<ContactBean>();
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }


}