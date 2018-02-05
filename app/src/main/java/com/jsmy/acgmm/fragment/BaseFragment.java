package com.jsmy.acgmm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.jsmy.acgmm.model.NetWork;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;

/**
 * Created by Administrator on 2018/1/15.
 */

public abstract class BaseFragment extends Fragment implements NetWork.CallListener {
    public Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getLocalClassName());
    }
}
