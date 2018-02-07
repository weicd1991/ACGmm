package com.jsmy.acgmm;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.jsmy.acgmm.bean.LogInBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.util.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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

    public static IWXAPI mWxApi;

    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, API.WEIXIN_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(API.WEIXIN_APP_ID);
    }

    public static void loginToWeiXin() {
        if (mWxApi != null && mWxApi.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test_neng";
            mWxApi.sendReq(req);
        } else {
//            Toast.makeText(context, "没有安装微信,请选择其他方式登录！", Toast.LENGTH_SHORT).show();
            ToastUtil.showShort(getMyApp().getApplicationContext(), "没有安装微信,请选择其他方式登录！");
        }
//        context.startActivity(new Intent(context, WXEntryActivity.class));
    }

}
