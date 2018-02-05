package com.jsmy.acgmm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.ActivityTack;
import com.jsmy.acgmm.util.MyLog;
import com.jsmy.acgmm.util.SPF;
import com.umeng.analytics.MobclickAgent;

import java.util.Set;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.api.BasicCallback;

public abstract class BaseActivity extends FragmentActivity implements NetWork.CallListener {
    public Gson gson;
    public String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContenView() != 0) {
            setContentView(getContenView());
        }
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        ButterKnife.bind(this);
        ActivityTack.getInstanse().addActivity(this);
        JMessageClient.registerEventReceiver(this);
        gson = new Gson();
        initView();
        initData();
        TAG = getClass().getName();
        MyLog.showLog(TAG, getClass().getName());
    }


    public void onEventMainThread(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        cn.jpush.im.android.api.model.UserInfo myInfo = event.getMyInfo();//获取当前被登出账号的信息
        MyLog.showLog(TAG, "" + reason + "  " + myInfo);
        switch (reason) {
            case user_password_change:
                //用户密码在服务器端被修改
                break;
            case user_logout:
                //用户换设备登录
                showNormalDialog(this);
                break;
            case user_deleted:
                //用户被删除
                break;
        }
    }

    private void showNormalDialog(final Context context) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
        normalDialog.setTitle("下线提示:");
        normalDialog.setMessage("您的账号已经在另一台设备上登录，您被迫下线!");
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                        startActivity(intent);
                        SPF.clear(context);
                        ActivityTack.getInstanse().removeAllActivity();
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
    }

    protected abstract int getContenView();

    protected abstract void initView();

    protected abstract void initData();

    private long lastClickTime;// 上次点击时间点

    /**
     * 判断事件出发时间间隔是否超过预定值
     *
     * @return
     * @Description
     */
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 防止连续点击
     *
     * @Description
     */
    public void startActivity(Intent intent) {
        if (isFastDoubleClick()) {
            MyLog.showLog(TAG, "startActivity() 重复调用");
            return;
        }
        super.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 删除集合存储记录
        ActivityTack.getInstanse().removeActivity(this);
        JMessageClient.unRegisterEventReceiver(this);
    }

    public void loginJMessage(final String yhid) {
        //极光登录
        JMessageClient.login(yhid, "123456", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    MyLog.showLog(TAG, "登录成功");
                    //设置别名
                    setAliasByYhid(yhid);
                } else if (i == 800005) {
                    MyLog.showLog(TAG, "用户ID未注册（appkey无该UID）");
                } else if (i == 800006) {
                    MyLog.showLog(TAG, "用户ID不存在（数据库中无该UID）");
                } else if (i == 800012) {
                    MyLog.showLog(TAG, "发起的用户处于登出状态，账号注册以后从未登录过，需要先登录");
                } else if (i == 800013) {
                    MyLog.showLog(TAG, "发起的用户处于登出状态，请求的用户已经登出，需要先登录");
                } else if (i == 800014) {
                    MyLog.showLog(TAG, "发起的用户appkey与目标不匹配");
                } else if (i == 801003) {
                    MyLog.showLog(TAG, "登录的用户名未注册，登录失败");
                    registerJMessage(yhid);
                } else if (i == 801004) {
                    MyLog.showLog(TAG, "登录的用户密码错误，登录失败");
                } else if (i == 801005) {
                    MyLog.showLog(TAG, "登录的用户设备有误，登录失败");
                } else if (i == 802002) {
                    MyLog.showLog(TAG, "登出用户名和登录用户名不匹配，登出失败");
                } else if (i == 871201) {
                    MyLog.showLog(TAG, "登录失败，响应超时！");
                    loginJMessage(yhid);
                } else {
                    MyLog.showLog(TAG, "错误码 " + i + " 登录失败，请联系管理员！");
                }
            }
        });
    }

    public void registerJMessage(final String yhid) {
        //极光注册
        JMessageClient.register(yhid, "123456", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    MyLog.showLog(TAG, "注册成功！");
                    loginJMessage(yhid);
                } else if (i == 871504) {
                    MyLog.showLog(TAG, "Push 注册未完成，请稍后重试。如果持续出现这个问题，可能你的 JPush 配置不正确。");
                    registerJMessage(yhid);
                } else if (i == 871505) {
                    MyLog.showLog(TAG, "Push 注册失败,对应包名在控制台上不存在。");
                } else if (i == 871506) {
                    MyLog.showLog(TAG, "Push 注册失败，设备IMEI不合法");
                } else {
                    MyLog.showLog(TAG, "错误码 " + i + " 注册失败，请联系管理员！");
                }
            }
        });
    }

    public void setAliasByYhid(final String yhid) {
        //这是别名
        JPushInterface.setAlias(this, yhid, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                MyLog.showLog(TAG, " --- " + yhid + " ---" + i);
                switch (i) {
                    case 0:

                        break;
                    case 6002:

                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void goToWebView(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static float getScreenWidth(Activity context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static float getScreenHeight(Activity context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
