package com.jsmy.acgmm.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.FragmentAdapter;
import com.jsmy.acgmm.fragment.AllRankFragment;
import com.jsmy.acgmm.fragment.LocalRankFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IntegralRankActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.my_tab)
    TabLayout myTab;
    @BindView(R.id.my_viewpager)
    ViewPager myViewpager;
    private FragmentAdapter adapter;
    private List<Fragment> fragmentsList;//fragment容器
    private List<String> titleList;//标签容器
    private LocalRankFragment localRankFragment;
    private AllRankFragment allRankFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_integral_rank;
    }

    @Override
    protected void initView() {
        tvTitle.setText("积分排行");

        fragmentsList = new ArrayList<>();
        localRankFragment = new LocalRankFragment();
        allRankFragment = new AllRankFragment();
        fragmentsList.add(localRankFragment);
        fragmentsList.add(allRankFragment);

        titleList = new ArrayList<>();
        titleList.add("本校榜");
        titleList.add("全榜");

        myTab.setTabMode(TabLayout.MODE_FIXED);

        adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentsList, titleList);
        myViewpager.setAdapter(adapter);
        myViewpager.setOffscreenPageLimit(2);
        myTab.setupWithViewPager(myViewpager);
        myViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {

    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }
}
