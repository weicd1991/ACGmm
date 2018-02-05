package com.jsmy.acgmm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/11/6.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList_;
    List<String> titleList_;
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        fragmentList_ = fragmentList;
        titleList_ = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList_.get(position);
    }

    @Override
    public int getCount() {
        return titleList_.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList_.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
