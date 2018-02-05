package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.IntegralRankActivity;
import com.jsmy.acgmm.bean.RankBean;
import com.jsmy.acgmm.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankHolder> {
    private IntegralRankActivity context;
    private List<RankBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public RankAdapter(IntegralRankActivity context, List<RankBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_rank_item, null);
        return new RankHolder(view);
    }

    @Override
    public void onBindViewHolder(RankHolder holder, int position) {
        Glide.with(context).load(list.get(position).getTx()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imgTx);
        holder.tvName.setText(list.get(position).getXsnc());
        holder.tvJf.setText("积分：" + list.get(position).getJf());
        holder.tvSchool.setText(list.get(position).getXsqm());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RankHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgTx;
        private TextView tvName;
        private TextView tvJf;
        private TextView tvSchool;

        public RankHolder(View itemView) {
            super(itemView);
            imgTx = (CircleImageView) itemView.findViewById(R.id.img_tx);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvJf = (TextView) itemView.findViewById(R.id.tv_jf);
            tvSchool = (TextView) itemView.findViewById(R.id.tv_school);

        }
    }
}
