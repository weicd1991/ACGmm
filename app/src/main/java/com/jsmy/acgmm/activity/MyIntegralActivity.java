package com.jsmy.acgmm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.MyIntegralAdapter;
import com.jsmy.acgmm.bean.IntegralBean;
import com.jsmy.acgmm.bean.MyIntegralBean;
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

public class MyIntegralActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<MyIntegralBean.DataBean.ListBean> listM;
    private List<MyIntegralBean.DataBean.ListBean> list;
    private MyIntegralAdapter adapter;
    private int pageindex = 1;

    private IntegralBean.DataBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_my_integral;
    }

    @Override
    protected void initView() {
        tvTitle.setText("我的积分");
        list = new ArrayList<>();
        adapter = new MyIntegralAdapter(this, list);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
                NetWork.getintegrallist(SPF.getString(MyIntegralActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", MyIntegralActivity.this);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                NetWork.getintegrallist(SPF.getString(MyIntegralActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", MyIntegralActivity.this);
            }
        });

    }

    @Override
    protected void initData() {
        NetWork.getintegralinfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
        NetWork.getintegrallist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_INTEGRAL_INFO:
                    bean = gson.fromJson(result, IntegralBean.class).getData();
                    tvIntegral.setText(bean.getJf());
                    break;
                case API.GET_INTEGRAL_LIST:
                    listM = gson.fromJson(result, MyIntegralBean.class).getData().getList();
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

    @OnClick({R.id.tv_back, R.id.tv_duihuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_duihuan:
                goToWebView(this, "https://www.baidu.com");
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                refreshLayout.finishRefresh();
                list.clear();
                list.addAll(listM);
                adapter.notifyDataSetChanged();
            } else {
                refreshLayout.finishLoadmore();
                list.addAll(listM);
                adapter.notifyItemRangeInserted(list.size() - listM.size(), listM.size());
            }
        }
    };
}
