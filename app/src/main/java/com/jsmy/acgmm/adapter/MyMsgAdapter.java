package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.MyMsgActivity;
import com.jsmy.acgmm.bean.MsgListBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyMsgAdapter extends RecyclerView.Adapter<MyMsgAdapter.MyMsgHolder> {
    private MyMsgActivity context;
    private List<MsgListBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public MyMsgAdapter(MyMsgActivity context, List<MsgListBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyMsgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_msg_item, null);
        return new MyMsgHolder(view);
    }

    @Override
    public void onBindViewHolder(MyMsgHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).getMsgtitle());
        holder.tvInfo.setText(list.get(position).getMsginfo());
        holder.tvTime.setText(list.get(position).getMsgtime());
        if ("Y".equals(list.get(position).getIsread())) {
            holder.imgDot.setVisibility(View.VISIBLE);
        } else {
            holder.imgDot.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.goToMsgInfo(list.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyMsgHolder extends RecyclerView.ViewHolder {
        private ImageView imgDot;
        private TextView tvTitle;
        private TextView tvInfo;
        private TextView tvTime;

        public MyMsgHolder(View itemView) {
            super(itemView);
            imgDot = (ImageView) itemView.findViewById(R.id.img_dot);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvInfo = (TextView) itemView.findViewById(R.id.tv_info);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
