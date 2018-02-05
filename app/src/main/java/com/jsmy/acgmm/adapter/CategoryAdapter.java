package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.CategoryActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Category1Holder> {
    private CategoryActivity context;
    private List<String> list;
    private LayoutInflater inflater;

    public CategoryAdapter(CategoryActivity context, List<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Category1Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_category_item1, null);
        return new Category1Holder(view);
    }

    @Override
    public void onBindViewHolder(Category1Holder holder, final int position) {
        holder.tvName.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.setAdapter2List(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Category1Holder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public Category1Holder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
