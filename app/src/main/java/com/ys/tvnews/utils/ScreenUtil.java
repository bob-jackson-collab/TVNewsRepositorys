package com.ys.tvnews.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by admin on 2016/1/11.
 */
public class ScreenUtil
{
    /**
     * 获取手机屏幕宽高
     * @param mContext
     * @return
     */
    public static int[] getScreenSize(Context mContext)
    {
        WindowManager mManager = (WindowManager)
                mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mOutMetrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(mOutMetrics);

        return new int[]{mOutMetrics.widthPixels,mOutMetrics.heightPixels};
    }

}
