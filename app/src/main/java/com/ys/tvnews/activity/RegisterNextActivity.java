package com.ys.tvnews.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ys.tvnews.R;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.httpurls.HttpUrl;
import com.ys.tvnews.utils.DESMD5Utils;
import com.ys.tvnews.views.MyProgressDialog;
import com.ys.tvnews.views.TitleViews;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sks on 2016/2/23.
 */
public class RegisterNextActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private TitleViews titleViews;
    private EditText user_passwd,user_confirm_passwd;
    private CheckBox check_agreement;
    private ImageView backImageView;
    private Button btn_register;
    private HttpUtils httpUtils;
    private MyProgressDialog mDialog;
    private String phone;

    @Override
    protected int loadLayout() {
        return R.layout.activity_register_next;
    }

    @Override
    protected void findView() {
        titleViews = (TitleViews) findViewById(R.id.register_next_title);
        user_passwd = (EditText) findViewById(R.id.user_passwd);
        user_confirm_passwd = (EditText) findViewById(R.id.user_confirm_passwd);
        backImageView = (ImageView) findViewById(R.id.title_back_imageview);
        btn_register = (Button) findViewById(R.id.btn_register);
        check_agreement = (CheckBox) findViewById(R.id.check_agreement);
    }

    @Override
    protected void regListener() {
        check_agreement.setOnCheckedChangeListener(this);
        backImageView.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        titleViews.setTitle("注册");
        httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        httpUtils.configRequestThreadPoolSize(4);
        mDialog = MyProgressDialog.createLoadingDialog(RegisterNextActivity.this,"正在加载...");
    }

    @Override
    protected void reqServer() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back_imageview:
                this.finish();
                break;
            case R.id.btn_register:
                register();


                break;
            default:  break;
        }

    }

    private void register(){

        String passwd = user_passwd.getText().toString().trim();
        String confirm_passwd = user_confirm_passwd.getText().toString().trim();
        phone = getIntent().getStringExtra("phone");
        if (TextUtils.isEmpty(passwd)) {
            showToast("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(confirm_passwd)) {
            showToast("请输入第二次密码");
            return;
        }
        if(!passwd.equals(confirm_passwd)){
            showToast("两次密码输入不一致");
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("dauserName",
                DESMD5Utils.doDESEncode(phone, HttpUrl.desKey));
        map.put("data.loginPwd", DESMD5Utils.doDESEncode(passwd, HttpUrl.desKey));
        map.put("data.regChannel", "myweb");
        String sign = DESMD5Utils.dsigndispost(map);
        RequestParams params = new RequestParams();
        params.addBodyParameter("data.userName",
                DESMD5Utils.doDESEncode(phone, HttpUrl.desKey));
        params.addBodyParameter("data.loginPwd",
                DESMD5Utils.doDESEncode(passwd, HttpUrl.desKey));
        params.addBodyParameter("data.regChannel", "myweb");
        params.addBodyParameter("sign", sign);

        httpUtils.send(HttpRequest.HttpMethod.POST, HttpUrl.REGISTER, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
                mDialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if(responseInfo.result!=null){
                    mDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(responseInfo.result);
                        if ("0".equals(jsonObject.getJSONObject("msg").getString("code"))) {
                            showToast("恭喜您！注册成功!");
                            Intent success_intent = new Intent(RegisterNextActivity.this, PersonalActivity.class);
                            success_intent.putExtra("phone", phone);
                            startActivity(success_intent);
                            RegisterNextActivity.this.finish();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mDialog.dismiss();
                showToast("网络加载失败");
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String passwd = user_passwd.getText().toString().trim();
        String confirm_passwd = user_confirm_passwd.getText().toString().trim();
        if(isChecked){
            if(!TextUtils.isEmpty(passwd)&& !TextUtils.isEmpty(confirm_passwd)){
                btn_register.setEnabled(true);
            }else{
                btn_register.setEnabled(false);
            }
        }else{
            btn_register.setEnabled(false);
        }
    }
}
