package com.jsmy.chongyin.customclass;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jsmy.chongyin.R;
import com.jsmy.chongyin.activity.MainActivity;
import com.jsmy.chongyin.adapter.BackgroudRecyclerAdapter2;
import com.jsmy.chongyin.bean.BJTPBean;
import com.jsmy.chongyin.contents.DowloadEvent;
import com.jsmy.chongyin.utils.AtyTag;
import com.jsmy.chongyin.view.RefreshRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class BackgroudWindow extends PopupWindow {
    private MainActivity context;
    private View view;
    private View foot;
    private RefreshRecyclerView recyclerBack;
    private BackgroudRecyclerAdapter2 adapter;
    private List<BJTPBean.DataBean.ListBean> list;
    private ImageView imgClose;
    private Handler handler;

    public BackgroudWindow(final MainActivity context, List<BJTPBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        //共用布局
        view = LayoutInflater.from(context).inflate(R.layout.pop_frend, null);
        foot = LayoutInflater.from(context).inflate(R.layout.pop_back_recycler_item, null);
        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.isBackGroud = "Y";
                context.gotoSomeActivity(context, AtyTag.ATY_ChoiceImage, true);
            }
        });
        initView(view);
//        initRecycler(context, list);
        initRecycler();
        this.setOutsideTouchable(false);
        this.setContentView(this.view);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(false);
        this.setAnimationStyle(R.style.take_pop_anim);
        EventBus.getDefault().register(this);
    }

    private void initView(View view) {
        imgClose = (ImageView) view.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(DowloadEvent event) {
        if ("bjtp".equals(event.getCode())) {
            Gson gson = new Gson();
            list.clear();
            list.addAll(gson.fromJson(event.getMsg(), BJTPBean.class).getData().getList());
            getData(true);
        }
    }

    private void initRecycler() {
        handler = new Handler();
        adapter = new BackgroudRecyclerAdapter2(context);
        recyclerBack = (RefreshRecyclerView) view.findViewById(R.id.recycler_frend);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerBack.setLayoutManager(layoutManager);
        recyclerBack.setAdapter(adapter);
        recyclerBack.setItemAnimor(null);
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
