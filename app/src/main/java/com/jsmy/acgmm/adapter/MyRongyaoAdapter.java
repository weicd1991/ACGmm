package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.MyRongyaoActivity;
import com.jsmy.acgmm.bean.RongYaoBean;
import com.jsmy.acgmm.util.MyLog;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class MyRongyaoAdapter extends RecyclerView.Adapter<MyRongyaoAdapter.MyRongyaoHolder> {
    private MyRongyaoActivity context;
    private List<RongYaoBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public MyRongyaoAdapter(MyRongyaoActivity context, List<RongYaoBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyRongyaoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_my_rongyao_item, null);
        return new MyRongyaoHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRongyaoHolder holder, int position) {
        ViewGroup.LayoutParams params = holder.img.getLayoutParams();
        params.height = (int) ((720 * context.getScreenWidth(context)) / 1280);
        holder.img.setLayoutParams(params);
        Glide.with(context).load(list.get(position).getImg()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyRongyaoHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public MyRongyaoHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
