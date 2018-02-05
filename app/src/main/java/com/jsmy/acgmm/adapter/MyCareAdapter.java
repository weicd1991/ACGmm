package com.jsmy.acgmm.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.MyCareActivity;
import com.jsmy.acgmm.activity.VideoActivity;
import com.jsmy.acgmm.bean.VideoListBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyCareAdapter extends RecyclerView.Adapter<MyCareAdapter.MyCareHolder> {
    private MyCareActivity context;
    private List<VideoListBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public MyCareAdapter(MyCareActivity context, List<VideoListBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyCareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_up_load_item, null);
        return new MyCareHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCareHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImgurl()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imgWall);
        if ("Y".equals(list.get(position).getIsjh())) {
            holder.imgTag.setVisibility(View.VISIBLE);
        } else {
            holder.imgTag.setVisibility(View.GONE);
        }
        holder.tvTitle.setText(list.get(position).getDmmc());
        holder.tvNum.setText(list.get(position).getBfcs() + "次");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("id", list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyCareHolder extends RecyclerView.ViewHolder {
        private ImageView imgWall;
        private ImageView imgTag;
        private TextView tvTitle;
        private TextView tvNum;

        public MyCareHolder(View itemView) {
            super(itemView);
            imgWall = (ImageView) itemView.findViewById(R.id.img_wall);
            imgTag = (ImageView) itemView.findViewById(R.id.img_tag);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }
}
