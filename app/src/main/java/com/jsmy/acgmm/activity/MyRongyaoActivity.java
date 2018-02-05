package com.jsmy.acgmm.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.MyRongyaoAdapter;
import com.jsmy.acgmm.bean.RongYaoBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyRongyaoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<RongYaoBean.DataBean.ListBean> listR;
    private List<RongYaoBean.DataBean.ListBean> list;
    private MyRongyaoAdapter adapter;

    private int pageindex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_my_rongyao;
    }

    @Override
    protected void initView() {
        tvTitle.setText("我的荣耀");
        list = new ArrayList<>();
        adapter = new MyRongyaoAdapter(this, list);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
                NetWork.getmyrylist(SPF.getString(MyRongyaoActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", MyRongyaoActivity.this);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                NetWork.getmyrylist(SPF.getString(MyRongyaoActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", MyRongyaoActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        NetWork.getmyrylist(SPF.getString(MyRongyaoActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", MyRongyaoActivity.this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_MYRY_LIST:
                    listR = gson.fromJson(result, RongYaoBean.class).getData().getList();
                    if (pageindex == 1) {
                        handler.sendEmptyMessage(101);
                    } else {
                        handler.sendEmptyMessage(102);
                    }
                    break;
            }
        } else {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
            ToastUtil.showShort(this, msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                refreshLayout.finishRefresh();
                list.clear();
                list.addAll(listR);
                adapter.notifyDataSetChanged();
            } else {
                refreshLayout.finishLoadmore();
                list.addAll(listR);
                adapter.notifyItemRangeInserted(list.size() - listR.size(), listR.size());
            }
        }
    };

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }

}
