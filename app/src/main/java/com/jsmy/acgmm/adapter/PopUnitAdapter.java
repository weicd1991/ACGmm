package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.Holographic1Activity;
import com.jsmy.acgmm.bean.BookBean;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.SPF;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class PopUnitAdapter extends RecyclerView.Adapter<PopUnitAdapter.PopUnitHolder> {
    private Holographic1Activity context;
    private List<BookBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public PopUnitAdapter(Holographic1Activity context, List<BookBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PopUnitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pop_layout_book_item, null);
        return new PopUnitHolder(view);
    }

    @Override
    public void onBindViewHolder(PopUnitHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).getJcmc());
        Glide.with(context).load(list.get(position).getJcimg()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imgBook);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWork.savexzjcinfo(SPF.getString(context, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), list.get(position).getJcid(), context);
                context.showChiosePopWindow();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PopUnitHolder extends RecyclerView.ViewHolder {
        private ImageView imgBook;
        private TextView tvTitle;

        public PopUnitHolder(View itemView) {
            super(itemView);
            imgBook = (ImageView) itemView.findViewById(R.id.img_book);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
