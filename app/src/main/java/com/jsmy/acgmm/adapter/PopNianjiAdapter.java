package com.jsmy.acgmm.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.Holographic1Activity;
import com.jsmy.acgmm.bean.NianJiBean;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.SPF;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class PopNianjiAdapter extends RecyclerView.Adapter<PopNianjiAdapter.PopNianjiHolder> {
    private Holographic1Activity context;
    private List<NianJiBean.DataBean.ListBean> list;
    private LayoutInflater inflater;
    private int selectePosition = -2;

    public PopNianjiAdapter(Holographic1Activity context, List<NianJiBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PopNianjiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pop_layout_item, null);
        return new PopNianjiHolder(view);
    }

    @Override
    public void onBindViewHolder(PopNianjiHolder holder, final int position) {
        if (selectePosition == position) {
            holder.tvName.setBackgroundColor(Color.parseColor("#D5D5D5"));
        } else {
            holder.tvName.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.tvName.setText(list.get(position).getNjmc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWork.getjclist(SPF.getString(context, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), list.get(position).getNjid(), 1 + "", "10", context);
                selectePosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PopNianjiHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public PopNianjiHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
