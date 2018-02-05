package com.jsmy.acgmm.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/3/29.
 */

public class MyLog {
    private static boolean isDebug = true;

    public static void showLog(String TAG, String showContants) {
        if (isDebug) {
            Log.e(TAG, showContants);
        } else {

        }
    }
}
