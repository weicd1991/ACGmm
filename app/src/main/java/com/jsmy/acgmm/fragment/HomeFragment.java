package com.jsmy.acgmm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.CategoryActivity;
import com.jsmy.acgmm.activity.Holographic1Activity;
import com.jsmy.acgmm.activity.IntegralRankActivity;
import com.jsmy.acgmm.activity.MainActivity;
import com.jsmy.acgmm.activity.WebViewActivity;
import com.jsmy.acgmm.adapter.BannerAdapter;
import com.jsmy.acgmm.adapter.HomeAdapter;
import com.jsmy.acgmm.bean.BannerBean;
import com.jsmy.acgmm.bean.VideoListBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/16.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.my_banner)
    Banner myBanner;
    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private int pageindex = 1;
    private List<BannerBean.DataBean.ListBean> listBanner;
    private List<String> images;

    private List<VideoListBean.DataBean.ListBean> listVideo;
    private List<VideoListBean.DataBean.ListBean> listVideo2;
    private HomeAdapter adapter;

    private String type = "R";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        NetWork.getbannerlist(this);
        NetWork.getsplist(SPF.getString(getContext(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), type, pageindex + "", "10", this);
        return view;
    }

    private void initView() {
        listVideo = new ArrayList<>();
        adapter = new HomeAdapter((MainActivity) getActivity(), listVideo);
        myRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
                NetWork.getsplist(SPF.getString(getContext(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), type, pageindex + "", "10", HomeFragment.this);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                NetWork.getsplist(SPF.getString(getContext(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), type, pageindex + "", "10", HomeFragment.this);
            }
        });

    }

    private void initBanner() {
        images = new ArrayList<>();
        for (int i = 0; i < listBanner.size(); i++) {
            images.add(listBanner.get(i).getUrllink());
        }
        //设置banner样式 显示圆形指示器
        myBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置指示器位置（当banner模式中有指示器时）
        myBanner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片加载器
        myBanner.setImageLoader(new BannerAdapter());
        //设置图片集合
        myBanner.setImages(images);
        //设置banner动画效果
        myBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        myBanner.isAutoPlay(true);
        //设置轮播时间
        myBanner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        myBanner.start();
        myBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ((MainActivity) getActivity()).goToWebView(((MainActivity) getActivity()), listBanner.get(position).getWwwlink());
            }
        });
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_BANNER_LIST:
                    listBanner = gson.fromJson(result, BannerBean.class).getData().getList();
                    initBanner();
                    break;
                case API.GET_SP_LIST:
                    listVideo2 = gson.fromJson(result, VideoListBean.class).getData().getList();
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
            ToastUtil.showShort(getContext(), msg);
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

    @OnClick({R.id.tv_fl, R.id.tv_jf, R.id.tv_qx, R.id.tv_bz, R.id.tv_zx, R.id.tv_tj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fl:
                getActivity().startActivity(new Intent(getActivity(), CategoryActivity.class));
                break;
            case R.id.tv_jf:
                getActivity().startActivity(new Intent(getActivity(), IntegralRankActivity.class));
                break;
            case R.id.tv_qx:
                getActivity().startActivity(new Intent(getActivity(), Holographic1Activity.class));
                break;
            case R.id.tv_bz:
                ((MainActivity) getActivity()).goToWebView(getActivity(), "https://www.baidu.com");
                break;
            case R.id.tv_zx:
                type = "R";
                pageindex = 1;
                NetWork.getsplist(SPF.getString(getContext(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), type, pageindex + "", "10", HomeFragment.this);
                break;
            case R.id.tv_tj:
                type = "T";
                pageindex = 1;
                NetWork.getsplist(SPF.getString(getContext(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), type, pageindex + "", "10", HomeFragment.this);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                refreshLayout.finishRefresh();
                listVideo.clear();
                listVideo.addAll(listVideo2);
                adapter.notifyDataSetChanged();
            } else {
                refreshLayout.finishLoadmore();
                listVideo.addAll(listVideo2);
                adapter.notifyItemRangeInserted(listVideo.size() - listVideo2.size(), listVideo2.size());
            }
        }
    };
}
