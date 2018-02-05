package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.VideoActivity;
import com.jsmy.acgmm.bean.DiscussBean;
import com.jsmy.acgmm.view.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private VideoActivity context;
    private List<DiscussBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public VideoAdapter(VideoActivity context, List<DiscussBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_video_item, null);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Glide.with(context).load(list.get(position).getHeadurl()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imgTx);
        holder.tvActer.setText(list.get(position).getName());
        holder.tvTime.setText(list.get(position).getTime());
        holder.tvSige.setText(list.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgTx;
        private TextView tvActer;
        private TextView tvTime;
        private TextView tvSige;

        public VideoHolder(View itemView) {
            super(itemView);
            imgTx = (CircleImageView) itemView.findViewById(R.id.img_tx);
            tvActer = (TextView) itemView.findViewById(R.id.tv_acter);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvSige = (TextView) itemView.findViewById(R.id.tv_sige);
        }
    }
}
