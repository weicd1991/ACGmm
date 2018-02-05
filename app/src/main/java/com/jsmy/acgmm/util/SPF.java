package com.jsmy.acgmm.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/3/29.
 */

public class SPF {
    public final static String SP_NAME = "com.jsmy.acgmm";
    public final static String SP_ZH = "username";
    public final static String SP_MM = "psd";
    public final static String SP_ID = "yhid";
    public final static String SP_NC = "nic";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getString(key, defValue);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getBoolean(key, defValue);
    }

    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().clear().commit();
    }

    public static void saveString(Context context, String sp_name, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(sp_name, 0);
        sp.edit().putInt(key, value).commit();
    }

    public static int getString(Context context, String sp_name, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(sp_name, 0);
        return sp.getInt(key, defValue);
    }

}
