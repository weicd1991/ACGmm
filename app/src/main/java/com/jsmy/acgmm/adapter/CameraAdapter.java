package com.jsmy.acgmm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.CameraActivity;
import com.jsmy.acgmm.util.MyLog;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.CameraHolder> {
    private CameraActivity context;
    private List<String> list;
    private LayoutInflater inflater;

    public CameraAdapter(CameraActivity context, List<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CameraHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_camera_item, null);
        return new CameraHolder(view);
    }

    @Override
    public void onBindViewHolder(CameraHolder holder, int position) {
//        ViewGroup.LayoutParams params = holder.img.getLayoutParams();
//        params.width = (int) ((720 * params.height) / 1280);
//        holder.img.setLayoutParams(params);
//        MyLog.showLog(getClass().getName(), list.get(position));
        Glide.with(context).load(list.get(position)).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CameraHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public CameraHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
