package com.ys.tvnews.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.ys.tvnews.R;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.httpurls.HttpUrl;
import com.ys.tvnews.receiver.LoginBroadCastReceiver;
import com.ys.tvnews.utils.AppUtils;
import com.ys.tvnews.utils.DESMD5Utils;
import com.ys.tvnews.views.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by sks on 2015/11/25.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{


    public static final String login_action = "com.ys.tvnews.qqlogin";
    private LoginBroadCastReceiver loginBroadCastReceiver;

    private EditText username,userpasswd;
    private Button btn_login;
    private TextView titleView,rightTextView,forgotpasswd;
    private ImageView backImageView,iv_qq_login;
    private String u_name;
    private String u_passwd;
    private HttpUtils httpUtils;
    private Dialog loading_dialog;

    /**
     * qq登录
     * @return
     */
    private Tencent mTencent;
    public static QQAuth mQQAuth;
    public static String mAppid;

    @Override
    protected int loadLayout() {
        return R.layout.activity_login;

    }

    @Override
    protected void findView() {
        //设置沉浸式状态栏
        AppUtils.settingStatus(LoginActivity.this);
        username = (EditText) findViewById(R.id.username);
        userpasswd = (EditText) findViewById(R.id.userpasswd);
        btn_login = (Button) findViewById(R.id.btn_login);
        titleView = (TextView) findViewById(R.id.title_title_textview);
        rightTextView = (TextView) findViewById(R.id.title_right_textview);
        forgotpasswd = (TextView) findViewById(R.id.forgot_passwd);
        backImageView = (ImageView) findViewById(R.id.title_back_imageview);
        iv_qq_login = (ImageView) findViewById(R.id.iv_qq_login);
    }

    @Override
    protected void regListener() {

        btn_login.setOnClickListener(this);
        rightTextView.setOnClickListener(this);
        forgotpasswd.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        iv_qq_login.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        //初始化QQ登录的一些操作
        mTencent = MyApplication.getmTencent();
        httpUtils = new HttpUtils();
        httpUtils.configRequestThreadPoolSize(3);

        titleView.setText("登录");
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText("注册");
        rightTextView.setTextColor(Color.BLUE);
        loading_dialog = MyProgressDialog.createLoadingDialog(LoginActivity.this, "正在登录....");

        /**
         * 判断用户名和密码是否为空不为空，则可以点击按钮
         */
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                u_passwd = userpasswd.getText().toString().trim();
                if(!s.toString().equals("")&& !TextUtils.isEmpty(u_passwd)){
                    btn_login.setEnabled(true);
                }else{
                    btn_login.setEnabled(false);
                }
            }
        });

        userpasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                u_name = username.getText().toString().trim();
                if(!s.toString().equals("")&& !TextUtils.isEmpty(u_name)){
                    btn_login.setEnabled(true);
                }else{
                    btn_login.setEnabled(false);
                }
            }
        });

    }

    @Override
    protected void reqServer() {

    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.title_back_imageview:
               finish();
               break;
           case R.id.btn_login:
               String login_name = username.getText().toString().trim();
               String login_passwd = userpasswd.getText().toString().trim();
               if(TextUtils.isEmpty(login_name)){
                   showToast("请输入用户名");
                   return;
               }
               if(TextUtils.isEmpty(login_passwd)){
                   showToast("请输入密码");
                   return;
               }
               if(!isMobileNO(login_name)){
                   showToast("请输入正确的手机号");
               }
               if(login_passwd.length()<6){
                   showToast("请输入至少6位密码");
               }
              startLogin();
               break;
           case R.id.iv_qq_login:
               qqLogin();
               iv_qq_login.setEnabled(false);
               break;
           case R.id.title_right_textview:
               Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
               startActivity(intent);
               this.finish();
               break;
       }
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    private void startLogin(){
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("data.userName", DESMD5Utils.doDESEncode(u_name, HttpUrl.desKey));
        hashMap.put("data.loginPwd",DESMD5Utils.doDESEncode(u_passwd,HttpUrl.desKey));
        String sign = DESMD5Utils.dsigndispost(hashMap);

        RequestParams params = new RequestParams();
        params.addBodyParameter("data.userName",DESMD5Utils.doDESEncode(u_name,HttpUrl.desKey));
        params.addBodyParameter("data.loginPwd",DESMD5Utils.doDESEncode(u_passwd,HttpUrl.desKey));
        params.addBodyParameter("sign",sign);

        httpUtils.send(HttpRequest.HttpMethod.POST, HttpUrl.LOGINURL, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
                loading_dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo != null) {
                    loading_dialog.dismiss();
                    Log.e("info", responseInfo.result);
                    try {
                        if ("0".equals(new JSONObject(responseInfo.result).getJSONObject("msg").get("code"))) {
                            Intent login_intent = new Intent(LoginActivity.this,PersonalActivity.class);
                            login_intent.putExtra("phone", username.getText().toString().trim());
                            startActivity(login_intent);
                            LoginActivity.this.finish();
                        } else {
                            showToast("用户名或密码错误哦");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                loading_dialog.dismiss();
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }

        });
    }

    //qq登录
    private void qqLogin(){
        //这里的APP_ID请换成你应用申请的APP_ID，我这里使用的是DEMO中官方提供的测试APP_ID 222222
        mAppid = HttpUrl.APP_ID;
        //第一个参数就是上面所说的申请的APPID，第二个是全局的Context上下文，这句话实现了调用QQ登录
        //mTencent = Tencent.createInstance(mAppid,getApplicationContext());
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类 */
        mTencent.login(LoginActivity.this,"all", new BaseUiListener());
    }

    class BaseUiListener implements IUiListener{

        @Override
        public void onComplete(Object o) {
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(getApplicationContext(), qqToken);
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Log.e("info","执行了这里");
                    try {
                    JSONObject json = (JSONObject)o;
                         if(json.has("figureurl")){
                             String head_img_url = json.getString("figureurl_qq_2");
                             String nickName = json.getString("nickname");
                             Intent intent = new Intent(LoginActivity.this,PersonalActivity.class);
                             intent.putExtra("head_img_url",head_img_url);
                             intent.putExtra("nickname",nickName);
                             startActivity(intent);
                             LoginActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(login_action);
        loginBroadCastReceiver = new LoginBroadCastReceiver();
        registerReceiver(loginBroadCastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(loginBroadCastReceiver);
    }
}
