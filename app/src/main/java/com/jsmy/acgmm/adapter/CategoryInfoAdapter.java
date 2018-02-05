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
import com.jsmy.acgmm.activity.CategoryInfoActivity;
import com.jsmy.acgmm.activity.VideoActivity;
import com.jsmy.acgmm.bean.CategoryListBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class CategoryInfoAdapter extends RecyclerView.Adapter<CategoryInfoAdapter.CategoryInfoHolder> {
    private CategoryInfoActivity context;
    private List<CategoryListBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public CategoryInfoAdapter(CategoryInfoActivity context, List<CategoryListBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CategoryInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_up_load_item, null);
        return new CategoryInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryInfoHolder holder, final int position) {
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

    class CategoryInfoHolder extends RecyclerView.ViewHolder {
        private ImageView imgWall;
        private ImageView imgTag;
        private TextView tvTitle;
        private TextView tvNum;

        public CategoryInfoHolder(View itemView) {
            super(itemView);
            imgWall = (ImageView) itemView.findViewById(R.id.img_wall);
            imgTag = (ImageView) itemView.findViewById(R.id.img_tag);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }
}
