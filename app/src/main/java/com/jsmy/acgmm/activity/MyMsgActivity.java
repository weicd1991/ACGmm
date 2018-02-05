package com.jsmy.acgmm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.MyMsgAdapter;
import com.jsmy.acgmm.bean.MsgListBean;
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

public class MyMsgActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<MsgListBean.DataBean.ListBean> listMsg;
    private List<MsgListBean.DataBean.ListBean> list;
    private MyMsgAdapter adapter;
    private int pageindex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_my_msg;
    }

    @Override
    protected void initView() {
        tvTitle.setText("我的消息");
        list = new ArrayList<>();
        adapter = new MyMsgAdapter(this, list);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
                NetWork.getmsglist(SPF.getString(MyMsgActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", MyMsgActivity.this);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                NetWork.getmsglist(SPF.getString(MyMsgActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", MyMsgActivity.this);
            }
        });

    }

    @Override
    protected void initData() {
        NetWork.getmsglist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_MSG_LIST:
                    listMsg = gson.fromJson(result, MsgListBean.class).getData().getList();
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
                list.addAll(listMsg);
                adapter.notifyDataSetChanged();
            } else {
                refreshLayout.finishLoadmore();
                list.addAll(listMsg);
                adapter.notifyItemRangeInserted(list.size() - listMsg.size(), listMsg.size());
            }
        }
    };

    public void goToMsgInfo(String id) {
        Intent intent = new Intent(this, MyMsgInfoActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pageindex = 1;
        NetWork.getmsglist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), pageindex + "", "10", this);
    }
}
