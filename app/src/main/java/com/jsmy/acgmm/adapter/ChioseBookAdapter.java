package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.ChioseBookActivity;
import com.jsmy.acgmm.bean.BookBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class ChioseBookAdapter extends RecyclerView.Adapter<ChioseBookAdapter.ChioseBookHolder> {
    private ChioseBookActivity context;
    private List<BookBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public ChioseBookAdapter(ChioseBookActivity context, List<BookBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ChioseBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_up_load_item, null);
        return new ChioseBookHolder(view);
    }

    @Override
    public void onBindViewHolder(ChioseBookHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getJcimg()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imgWall);
        holder.imgTag.setVisibility(View.GONE);
        holder.tvTitle.setText(list.get(position).getJcmc());
        holder.tvNum.setText("");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.savexzjcinfo(list.get(position).getJcid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ChioseBookHolder extends RecyclerView.ViewHolder {
        private ImageView imgWall;
        private ImageView imgTag;
        private TextView tvTitle;
        private TextView tvNum;

        public ChioseBookHolder(View itemView) {
            super(itemView);
            imgWall = (ImageView) itemView.findViewById(R.id.img_wall);
            imgTag = (ImageView) itemView.findViewById(R.id.img_tag);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }
}
