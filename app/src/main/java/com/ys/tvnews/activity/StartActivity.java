package com.ys.tvnews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.ys.tvnews.R;

/**
 * Created by sks on 2015/12/21.
 */
public class StartActivity extends Activity {

    private CountDownTimer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        mTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //3s之后进入主界面
                Intent intent = new Intent(StartActivity.this,IndexActivity.class);
                StartActivity.this.startActivity(intent);
                StartActivity.this.finish();
            }
        };
        mTimer.start();
        if(checkNetworkAvailable(StartActivity.this)){
            Toast.makeText(StartActivity.this,"当前手机联网了哦",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(StartActivity.this,"当前手机没有联网哦",Toast.LENGTH_SHORT).show();
        }
    }


    // 检测网络
    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return true;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;

    }
}
