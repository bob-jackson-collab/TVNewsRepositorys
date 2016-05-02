package com.ys.tvnews.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.lidroid.xutils.DbUtils;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.PlatformConfig;
import com.ys.tvnews.sqlite.OperateDB;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by sks on 2015/11/23.
 */
public class MyApplication extends Application{

    private static List<Activity> list_activity;
    private static Context mContext;
    private static OperateDB operateDB;
    private static DbUtils mDbUtils;
    private static MyApplication myApplication;
    private static Tencent mTencent;

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
        JPushInterface.init(mContext);
        JPushInterface.setDebugMode(true);
        SDKInitializer.initialize(getApplicationContext());
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //微信 appid appsecret
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        //新浪微博 appkey appsecret
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    public static DbUtils getDbInstance(){

        if(mDbUtils==null) {
            mDbUtils = DbUtils.create(mContext);
            return mDbUtils;
        }
        return mDbUtils;
    }

    /**
     * 初始化数据库的操作
     * @return
     */
    public static OperateDB getInstance(){
        if(operateDB==null){
            operateDB = new OperateDB(mContext);
        }
        return operateDB;
    }

    /**
     *初始化activity
     */
    private void initData(){
        list_activity = new ArrayList<Activity>();
        mContext = getApplicationContext();
    }

    public void addActivity(Activity activity){
        list_activity.add(activity);
    }

    public void removeActivity(Activity activity){
        list_activity.remove(activity);
    }

    /**
     * 退出应用程序所做的操作
     */
    public static void exit(){
        for(Activity activity:list_activity){
            if(activity!=null){
                activity.finish();
            }
        }

        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        //activityManager.restartPackage(mContext.getPackageName());
        activityManager.killBackgroundProcesses(mContext.getPackageName());
        System.exit(0);
    }

    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        try {
                            resultData = applicationInfo.metaData.getString(key);
                        } catch (Exception e) {
                            resultData = applicationInfo.metaData.getInt(key)+"";
                            e.printStackTrace();
                        }

                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    public static MyApplication getAppInstance(){
        if(myApplication==null){
            myApplication = new MyApplication();
            return myApplication;
        }
        return myApplication;
    }

    public static Tencent getmTencent(){
        if(mTencent==null){
            mTencent = Tencent.createInstance("222222",mContext);
        }
        return mTencent;
    }
}
