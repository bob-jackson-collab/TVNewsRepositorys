package com.ys.tvnews.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.ys.tvnews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2015/11/23.
 */
public abstract class BaseActivity extends Activity{

    public String mtype;// 设备号
    public String imei;// 唯一标识
    protected Context mContext;
    public HttpUtils httpUtils;

    /** 加载布局 */
    protected abstract int loadLayout();

    /** 初始化组件 */
    protected abstract void findView();

    /** 注册监听 */
    protected abstract void regListener();

    /** 加载数据 */
    protected abstract void loadData();

    /** 请求网络 */
    protected abstract void reqServer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    public void showToast(String text) {
        Toast.makeText(this, "" + text, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int resourse) {
        Toast.makeText(this, resourse, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取IMEI号，IESI号，手机型号 返回值：0为唯一设备号 1为手机型号
     */
    public ArrayList<String> getInfo() {
        TelephonyManager mTm = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        imei = mTm.getDeviceId();// 唯一设备号
        mtype = android.os.Build.MODEL;// 手机型号
        ArrayList<String> minfo = new ArrayList<String>();

        if (null == imei && imei.equals("") && imei.length() <= 0) {
            minfo.add(0, "其他");
        } else {
            minfo.add(0, imei);
        }

        if (null == mtype && mtype.equals("") && mtype.length() <= 0) {
            minfo.add(1, "其他");
        } else {
            minfo.add(1, mtype);
        }

        return minfo;
    }

    private void initActivity(){
        mContext = this;
        int layout = loadLayout();
        setContentView(layout);
        httpUtils = new HttpUtils();
        findView();
        regListener();
        loadData();
        reqServer();
    }

    public boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            // 获取输入框当前的location位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}
