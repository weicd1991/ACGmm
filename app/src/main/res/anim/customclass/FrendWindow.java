package com.jsmy.chongyin.customclass;

import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jsmy.chongyin.R;
import com.jsmy.chongyin.activity.MainActivity;
import com.jsmy.chongyin.adapter.FrendRecyclerAdapter2;
import com.jsmy.chongyin.application.MyApplication;
import com.jsmy.chongyin.bean.FriendListBean;
import com.jsmy.chongyin.utils.AtyTag;
import com.jsmy.chongyin.view.RefreshRecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class FrendWindow extends PopupWindow {
    private MainActivity context;
    private View view;
    private View foot;
    private RefreshRecyclerView recyclerFrend;
    private ImageView imgClose;
    private FrendRecyclerAdapter2 adapter;
    private Handler handler;
    private List<FriendListBean.DataBean.ListBean> friendList;

    public FrendWindow(final MainActivity context, List<FriendListBean.DataBean.ListBean> listBeen) {
        this.context = context;
        this.friendList = listBeen;
        if (friendList != null) {
            for (int i = 0; i < friendList.size(); i++) {
                friendList.get(i).setChosice(false);
            }
        }else {
            return;
        }
        view = LayoutInflater.from(context).inflate(R.layout.pop_frend, null);
        initView(view);
        foot = LayoutInflater.from(context).inflate(R.layout.pop_food_recycler_item, null);
        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                context.gotoSomeActivity(context, AtyTag.ATY_NewFrend, true);
            }
        });
        initRecycler();
        this.setOutsideTouchable(false);
        this.setContentView(this.view);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(false);
        this.setAnimationStyle(R.style.take_pop_anim);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

    }

    private void initView(View view) {
        imgClose = (ImageView) view.findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                context.showAnimation(1, MyApplication.getMyApplication().userInfo.getPetid(), MyApplication.getMyApplication().userInfo.getPetdj());
                context.setThingGone();
                context.showFrend(false);
                context.getSJWPList();
                context.setImgPetZ(context.bean.getTpurl());

            }
        });
    }

    private void initRecycler() {
        handler = new Handler();
        adapter = new FrendRecyclerAdapter2(context);
        recyclerFrend = (RefreshRecyclerView) view.findViewById(R.id.recycler_frend);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerFrend.setLayoutManager(layoutManager);
        recyclerFrend.setAdapter(adapter);
        recyclerFrend.setItemAnimor(null);
        getData(false, 0);
    }

    public void setChoise(List<FriendListBean.DataBean.ListBean> listBeen, int position) {
        friendList.clear();
        friendList.addAll(listBeen);
//        initRecycler();
//        getData(true, position);
        adapter.notifyItemChange(friendList,position);
    }

    public void updataPosition(int position){
        for (int i = 0; i < friendList.size(); i++) {
            friendList.get(i).setChosice(false);
        }
        friendList.get(position).setChosice(true);
        adapter.notifyItemChange(friendList,context.friendOldPosition);
        adapter.notifyItemChange(friendList,context.friendPosition);
    }

    public void getData(final boolean b, final int position) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.removeFooter();
                adapter.clear();
                adapter.addAll(friendList);
                adapter.setFooter(foot);
            }
        }, 100);
    }

    @Override
    public void dismiss() {
        context.clearChoisce();
        super.dismiss();
    }
}
