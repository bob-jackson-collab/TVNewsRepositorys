package com.ys.tvnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sks on 2016/1/26.
 */

public class ShareUtils {
    private static SharedPreferences userShare;
    private static SharedPreferences rememberUserShare;
    private static SharedPreferences loadShare;
    private static SharedPreferences handPassShare;

    //存储用户名信息
    public static void setUserName(Context context, String userName) {
        if (userShare == null)
            userShare = context.getSharedPreferences("userInfo",
                    Context.MODE_PRIVATE);
        userShare.edit().putString("userName", userName).commit();
    }

    public static String getUserName(Context context) {
        if (userShare == null)
            userShare = context.getSharedPreferences("userInfo",
                    Context.MODE_PRIVATE);
        return userShare.getString("userName", "");
    }
    //存储登录的用户名信息
    public static void setLoginUser(Context context, String username) {
        if (rememberUserShare == null)
            rememberUserShare = context.getSharedPreferences("loginUser",
                    Context.MODE_PRIVATE);
        rememberUserShare.edit().putString("loginUser", username).commit();
    }

    public static String getLoginUser(Context context) {
        if (rememberUserShare == null)
            rememberUserShare = context.getSharedPreferences("loginUser",
                    Context.MODE_PRIVATE);
        return rememberUserShare.getString("loginUser", "");
    }

    public static void clearUserName(Context context) {
        if (userShare == null)
            userShare = context.getSharedPreferences("userInfo",
                    Context.MODE_PRIVATE);
        userShare.edit().putString("userName", "").commit();
    }
}

