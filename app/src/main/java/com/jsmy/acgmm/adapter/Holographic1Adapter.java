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
import com.jsmy.acgmm.activity.Holographic1Activity;
import com.jsmy.acgmm.activity.Holographic2Activity;
import com.jsmy.acgmm.activity.Holographic3Activity;
import com.jsmy.acgmm.bean.Holo1Bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class Holographic1Adapter extends RecyclerView.Adapter<Holographic1Adapter.Holographic1Holder> {
    private Holographic1Activity context;
    private List<Holo1Bean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public Holographic1Adapter(Holographic1Activity context, List<Holo1Bean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Holographic1Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_holographic1_item, null);
        return new Holographic1Holder(view);
    }

    @Override
    public void onBindViewHolder(Holographic1Holder holder, final int position) {
        Glide.with(context).load(list.get(position).getDyimg()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imgF);
        holder.tvUnit.setText(list.get(position).getDymc());
        holder.tvWord.setText("单词数：" + list.get(position).getAllcs() + "个");
        holder.tvFinish.setText("已完成：" + list.get(position).getYzcs() + "个");
        holder.tvQx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Holographic2Activity.class);
                intent.putExtra("type", "1");
                intent.putExtra("id", list.get(position).getDyid());
                context.startActivity(intent);
            }
        });
        holder.tvPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Holographic3Activity.class);
                intent.putExtra("type", "2");
                intent.putExtra("id", list.get(position).getDyid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holographic1Holder extends RecyclerView.ViewHolder {
        private ImageView imgF;
        private TextView tvUnit;
        private TextView tvWord;
        private TextView tvFinish;
        private TextView tvQx;
        private TextView tvPm;

        public Holographic1Holder(View itemView) {
            super(itemView);
            imgF = (ImageView) itemView.findViewById(R.id.img_f);
            tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
            tvWord = (TextView) itemView.findViewById(R.id.tv_word);
            tvFinish = (TextView) itemView.findViewById(R.id.tv_finish);
            tvQx = (TextView) itemView.findViewById(R.id.tv_qx);
            tvPm = (TextView) itemView.findViewById(R.id.tv_pm);
        }
    }
}
