package com.ys.tvnews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ys.tvnews.activity.LoginActivity;
import com.ys.tvnews.activity.PersonalActivity;

/**
 * Created by sks on 2015/11/26.
 */
public class LoginBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(LoginActivity.login_action)){
            Log.e("broadcastreceiver", "收到了广播");
            Intent login_intent = new Intent(context,PersonalActivity.class);
            login_intent.putExtra("head_img_url",intent.getStringExtra("head_img_url"));
            context.startActivity(login_intent);
        }
    }
}
