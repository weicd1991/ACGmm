package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.CategoryActivity;
import com.jsmy.acgmm.bean.XueXiaoBean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class CategoryDMAdapter extends RecyclerView.Adapter<CategoryDMAdapter.Category2Holder> {
    private CategoryActivity context;
    private List<XueXiaoBean.DataBean.ListBean> list;
    private LayoutInflater inflater;

    public CategoryDMAdapter(CategoryActivity context, List<XueXiaoBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Category2Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_category_item2, null);
        return new Category2Holder(view);
    }

    @Override
    public void onBindViewHolder(final Category2Holder holder, final int position) {
        holder.tvName.setText(list.get(position).getXxmc());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                context.setxxids(isChecked,list.get(position).getXxid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Category2Holder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CheckBox checkBox;

        public Category2Holder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
        }
    }
}
