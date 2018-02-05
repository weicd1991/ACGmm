package com.jsmy.acgmm.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2018/1/16.
 */

public class BannerAdapter extends ImageLoader {

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
    }
}
