/**
 * 
 */
package com.ys.tvnews.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ys.tvnews.R;

public class AppUtils {

	/**
	 * 获取屏幕分辨率
	 * 
	 * @param context
	 * @return
	 */
	public static int[] getScreenDispaly(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;// 手机屏幕的宽度
		int height = metrics.heightPixels;// 手机屏幕的高度
		int result[] = { width, height };
		return result;
	}

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	public static String getLocalIpAddress(Context context) {

		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (wifiManager.isWifiEnabled()) {
			// wifiManager.setWifiEnabled(true);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			String ip = intToIp(ipAddress);
			Log.e("wifi网络 客户端ip", ip);
			return ip;
		} else {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							Log.e("gprs  客户端ip", inetAddress.getHostAddress()
									.toString());
							return inetAddress.getHostAddress().toString();

						}
					}
				}

			} catch (SocketException ex) {
				Log.e("获取客户端ip", ex.toString());
			}
		}

		return null;
	}

	/**
	 * 判断微信客户端是否安装
	 * @param context
	 * @param api
	 * @return
	 */
//	public static boolean isWXAppInstalledAndSupported(Context context,
//			IWXAPI api) {
//		// LogOutput.d(TAG, "isWXAppInstalledAndSupported");
//		boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
//				&& api.isWXAppSupportAPI();
//		/*
//		 * if (!sIsWXAppInstalledAndSupported) { Log.w(TAG,
//		 * "~~~~~~~~~~~~~~微信客户端未安装，请确认"); GameToast.showToast(context,
//		 * "微信客户端未安装，请确认"); }
//		 */
//
//		return sIsWXAppInstalledAndSupported;
//	}
	/** 
	 * @author wxy  
	 * @Description:  TODO 判断应用进程是否存活  //包含  按back键，有时task栈被清空，但是进程并没有被后台回收的情况 推送消息要先启动主页面，再跳转到消息详细页面
	 * @data:  2015-9-26 下午1:14:34
	 */
	public static boolean isAppAlive(Context context) {
		 boolean isAppRunning=false;
		 ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		    List<RunningTaskInfo> list = am.getRunningTasks(100);
		    for (RunningTaskInfo info : list) {
		    	
		        if (info.topActivity.getPackageName().equals("com.mz.businesstreasure") && info.baseActivity.getPackageName().equals("com.mz.businesstreasure")) {
		            isAppRunning = true;
		            //find it, break
		            break;
		        }
		    }
		    if (isAppRunning) {
				Log.d("tag", "---------------app进程是活着");
			}else
				Log.d("tag", "---------------app进程是死着");
		 return isAppRunning;
	}
	/**
	 * @author wxy
	 * @Description:  TODO  判断App是否在前台运行。
	 * @data:  2015-10-8 下午4:50:35
	 */
	public static boolean isRunningForeground (Context context){
		 ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
		    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;  
		    String currentPackageName = cn.getPackageName();  
		    if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName()))  
		    {  
		        return true ;  
		    }  		   
		    return false ;  
	}  
	
	/**
	 * 判断app是在前台运行还是在后台运行
	 * @param context
	 * @return
	 */
	
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				/*
				BACKGROUND=400 EMPTY=500 FOREGROUND=100
				GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
				Log.i(context.getPackageName(), "此appimportace ="
						+ appProcess.importance
						+ ",context.getClass().getName()="
						+ context.getClass().getName());
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Log.i(context.getPackageName(), "处于后台"
							+ appProcess.processName);
					return true;
				} else {
					Log.i(context.getPackageName(), "处于前台"
							+ appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	
	/**
	 * 判断是否有前台在运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTopActivity(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		String packageName = context.getPackageName();
		Log.e("info",packageName+"");
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对API版本19以上的进行设置沉浸式状态栏
	 */

	public static void settingStatus(Activity context){

		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT ) {
			//透明状态栏
			context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
			context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

			SystemBarTintManager systemBarTintManager = new SystemBarTintManager(context);
			//设置状态栏为可用
			systemBarTintManager.setStatusBarTintEnabled(true);
			//设置导航栏为可用
			systemBarTintManager.setNavigationBarTintEnabled(true);

			//设置状态栏的颜色
			systemBarTintManager.setStatusBarTintColor(Color.parseColor("#ff0000"));
			//systemBarTintManager.setStatusBarTintResource(R.color.umeng_socialize_color_group);

			//设置导航栏的颜色
			//systemBarTintManager.setNavigationBarTintColor(Color.parseColor("#ffffffff"));
            //设置透明度
			systemBarTintManager.setTintAlpha(150);
		}
	}
}
