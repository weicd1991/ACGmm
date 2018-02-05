package com.jsmy.acgmm.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class ActivityTack {
    public List<Activity> activityList = new ArrayList<Activity>();

    public static ActivityTack tack = new ActivityTack();

    public static ActivityTack getInstanse() {
        return tack;
    }

    private ActivityTack() {

    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void removeAllActivity() {
        while (activityList.size() > 0) {
            popActivity(activityList.get(activityList.size() - 1));
        }
    }

    /**
     * 完全退出
     *
     */
    public void exit() {
        while (activityList.size() > 0) {
            popActivity(activityList.get(activityList.size() - 1));
        }
        // 结束进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    /**
     * 弹出activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        removeActivity(activity);
        activity.finish();
        activity = null;
    }
}
