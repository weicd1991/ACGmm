package com.jsmy.acgmm.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.IntegralRankActivity;
import com.jsmy.acgmm.adapter.RankAdapter;
import com.jsmy.acgmm.bean.RankBean;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/18.
 */

public class AllRankFragment extends BaseFragment {

    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<RankBean.DataBean.ListBean> listRank;
    private List<RankBean.DataBean.ListBean> list;
    private RankAdapter adapter;
    private int pageindex = 1;

    Unbinder unbinder;

    private String type = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, null);
        unbinder = ButterKnife.bind(this, view);
        initRecycler();
        initRefresh();
        NetWork.getxsphblist(SPF.getString(getActivity(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                type,
                pageindex + "",
                "10",
                this);
        return view;
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_XSPHB_LIST:
                    listRank = gson.fromJson(result, RankBean.class).getData().getList();
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
            ToastUtil.showShort(getActivity(), msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initRecycler() {
        list = new ArrayList<>();
        adapter = new RankAdapter((IntegralRankActivity) getActivity(), list);
        myRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
                NetWork.getxsphblist(SPF.getString(getActivity(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                        type,
                        pageindex + "",
                        "10",
                        AllRankFragment.this);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                NetWork.getxsphblist(SPF.getString(getActivity(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                        type,
                        pageindex + "",
                        "10",
                        AllRankFragment.this);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                refreshLayout.finishRefresh();
                list.clear();
                list.addAll(listRank);
                adapter.notifyDataSetChanged();
            } else {
                refreshLayout.finishLoadmore();
                list.addAll(listRank);
                adapter.notifyItemRangeInserted(list.size() - listRank.size(), listRank.size());
            }
        }
    };
}
