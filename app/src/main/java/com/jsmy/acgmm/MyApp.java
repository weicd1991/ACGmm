package com.jsmy.acgmm;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.jsmy.acgmm.bean.LogInBean;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Administrator on 2018/1/15.
 */

public class MyApp extends MultiDexApplication {
    public static MyApp myApp;
    public LogInBean.DataBean bean;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        //友盟
        MobclickAgent.enableEncrypt(true);
        MobclickAgent.openActivityDurationTrack(false);
        //极光
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
    }

    public static MyApp getMyApp() {
        return myApp;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        JMessageClient.logout();
    }

}
