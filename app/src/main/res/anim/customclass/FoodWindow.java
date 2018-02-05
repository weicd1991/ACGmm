package com.jsmy.chongyin.customclass;

import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jsmy.chongyin.R;
import com.jsmy.chongyin.activity.MainActivity;
import com.jsmy.chongyin.adapter.FoodRecyclerAdapter2;
import com.jsmy.chongyin.bean.MyFoodBean;
import com.jsmy.chongyin.contents.DowloadEvent;
import com.jsmy.chongyin.utils.AtyTag;
import com.jsmy.chongyin.view.RefreshRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class FoodWindow extends PopupWindow {
    private MainActivity context;
    private View view;
    private View foot;
    private RefreshRecyclerView recyclerFood;
    private FoodRecyclerAdapter2 adapter;
    private Handler handler;
    private ImageView imgClose;
    public ImageView imgMosce;

    public RelativeLayout activityFrend;
    private List<MyFoodBean.DataBean.ListBean> list;

    public FoodWindow(final MainActivity context, List<MyFoodBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        //共用布局
        view = LayoutInflater.from(context).inflate(R.layout.pop_food, null);
        initView(view);
        foot = LayoutInflater.from(context).inflate(R.layout.pop_food_recycler_item, null);
        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                context.addType = "two";
                context.gotoSomeActivity(context, AtyTag.ATY_AddFood, true);
            }
        });
        initRecycler();
        this.setOutsideTouchable(false);
        this.setContentView(this.view);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(false);
        this.setAnimationStyle(R.style.take_pop_anim);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(DowloadEvent event) {
        if ("food".equals(event.getCode())) {
            Gson gson = new Gson();
            list.clear();
            list.addAll(gson.fromJson(event.getMsg(), MyFoodBean.class).getData().getList());
//            getData(true);
//            initRecycler();
            adapter.clear();
            adapter.addAll(list);
        }
    }

    private void initView(View view) {
        recyclerFood = (RefreshRecyclerView) view.findViewById(R.id.recycler_frend);
        activityFrend = (RelativeLayout) view.findViewById(R.id.activity_frend);
        imgMosce = (ImageView) view.findViewById(R.id.img_mosce);
        imgClose = (ImageView) view.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initRecycler() {
        handler = new Handler();
        adapter = new FoodRecyclerAdapter2(context);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerFood.setLayoutManager(layoutManager);
        recyclerFood.setItemAnimor(null);
        recyclerFood.setAdapter(adapter);
        getData(true);
    }

    public void getData(final boolean b) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.removeFooter();
                adapter.clear();
                adapter.addAll(list);
                adapter.setFooter(foot);
            }
        }, 100);
    }

}
