package com.jsmy.acgmm.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.CategoryInfoAdapter;
import com.jsmy.acgmm.bean.CategoryListBean;
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

public class CategoryInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<CategoryListBean.DataBean.ListBean> listCa;
    private List<CategoryListBean.DataBean.ListBean> list;
    private CategoryInfoAdapter adapter;
    private int pageindex = 1;

    private String flids = "";
    private String xxids = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_category_info;
    }

    @Override
    protected void initView() {
        tvTitle.setText("检索列表");
        list = new ArrayList<>();
        adapter = new CategoryInfoAdapter(this, list);
        myRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
                NetWork.getflsplist(SPF.getString(CategoryInfoActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                        flids,
                        xxids,
                        pageindex + "",
                        "10",
                        CategoryInfoActivity.this);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                NetWork.getflsplist(SPF.getString(CategoryInfoActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                        flids,
                        xxids,
                        pageindex + "",
                        "10",
                        CategoryInfoActivity.this);
            }
        });

    }

    @Override
    protected void initData() {
        flids = getIntent().getStringExtra("flids");
        xxids = getIntent().getStringExtra("xxids");
        NetWork.getflsplist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                flids,
                xxids,
                pageindex + "",
                "10",
                this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_FLSP_LIST:
                    listCa = gson.fromJson(result, CategoryListBean.class).getData().getList();
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

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                refreshLayout.finishRefresh();
                list.clear();
                list.addAll(listCa);
                adapter.notifyDataSetChanged();
            } else {
                refreshLayout.finishLoadmore();
                list.addAll(listCa);
                adapter.notifyItemRangeInserted(list.size() - listCa.size(), listCa.size());
            }
        }
    };
}
