package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.MyIntegralActivity;
import com.jsmy.acgmm.bean.MyIntegralBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyIntegralAdapter extends RecyclerView.Adapter<MyIntegralAdapter.MyIntegralHolder> {
    private MyIntegralActivity context;
    private List<MyIntegralBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public MyIntegralAdapter(MyIntegralActivity context, List<MyIntegralBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyIntegralHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_integral_item, null);
        return new MyIntegralHolder(view);
    }

    @Override
    public void onBindViewHolder(MyIntegralHolder holder, int position) {
        holder.tvTitle.setText(list.get(position).getMsg());
        holder.tvNum.setText(list.get(position).getIntegral());
        holder.tvTime.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyIntegralHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvNum;
        private TextView tvTime;

        public MyIntegralHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
