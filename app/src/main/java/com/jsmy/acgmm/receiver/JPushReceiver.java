package com.jsmy.acgmm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jsmy.acgmm.util.MyLog;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        MyLog.showLog(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            MyLog.showLog(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            MyLog.showLog(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
            MyLog.showLog(TAG, "" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String str = bundle.getString(JPushInterface.EXTRA_MESSAGE);

            if ("huodong".equals(str)) {

//                processCustomMessage(context, bundle);
            } else {

            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            MyLog.showLog(TAG, "[MyReceiver] 接收到推送下来的通知");
            String str = bundle.getString(JPushInterface.EXTRA_ALERT);
            MyLog.showLog(TAG, "" + str);

            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            MyLog.showLog(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            MyLog.showLog(TAG, "[MyReceiver] 用户点击打开了通知");
//
            //打开自定义的Activity
//            Intent i = new Intent(context, XunChaMingLingActivity.class);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            MyLog.showLog(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            MyLog.showLog(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            MyLog.showLog(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        //省略了
        return null;
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        Intent intent = new Intent(context, FloatWindowService.class);
//        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("msg", MyApplication.getMyApplication().userInfo.getYhnc() + bundle.getString(JPushInterface.EXTRA_ALERT));
//        context.startService(intent);
    }
}
