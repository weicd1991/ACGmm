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
import com.jsmy.acgmm.adapter.Holographic1Adapter;
import com.jsmy.acgmm.bean.Holo1Bean;
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

public class Holographic1Activity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_check)
    TextView tvCheck;
    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_back)
    TextView tvBack;
    private List<Holo1Bean.DataBean.ListBean> listH;
    private List<Holo1Bean.DataBean.ListBean> list;
    private Holographic1Adapter adapter;
    private int pageindex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_holographic1;
    }

    @Override
    protected void initView() {
        tvBack.setText("首页");
        tvTitle.setText("全息学英语");
        tvCheck.setText("教材");
        tvCheck.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        adapter = new Holographic1Adapter(this, list);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
//                NetWork.getmyqxjclist(SPF.getString(Holographic1Activity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", Holographic1Activity.this);
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
//                NetWork.getmyqxjclist(SPF.getString(Holographic1Activity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", Holographic1Activity.this);
                refreshlayout.finishLoadmore();
            }
        });
    }

    @Override
    protected void initData() {
        NetWork.getmyqxjclist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_MYQX_JCLIST:
                    listH = gson.fromJson(result, Holo1Bean.class).getData().getList();
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

    @OnClick({R.id.tv_back, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                break;
            case R.id.tv_check:
                startActivityForResult(new Intent(this, ChioseBookActivity.class), 1001);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                refreshLayout.finishRefresh();
                list.clear();
                list.addAll(listH);
                adapter.notifyDataSetChanged();
            } else {
                refreshLayout.finishLoadmore();
                list.addAll(listH);
                adapter.notifyItemRangeInserted(list.size() - listH.size(), listH.size());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pageindex = 1;
        NetWork.getmyqxjclist(SPF.getString(Holographic1Activity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", Holographic1Activity.this);
    }
}
