package com.ys.tvnews.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.ys.tvnews.R;
import com.ys.tvnews.httpurls.HttpUrl;
import com.ys.tvnews.utils.DESMD5Utils;
import com.ys.tvnews.views.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by sks on 2015/11/25.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{


    private TextView title_textView,right_textView;
    private EditText user_phone,user_code,user_passwd,user_confirm_passwd;
  //  private CheckBox check_agreement;
    private Button btn_register,img_accetp_code;
    private ImageView back_imageView;
    private boolean flag = false;   //判断所有的EditText是否都有值

    private String u_phone,u_passwd,u_confirm_passwd,u_code;
    private HttpUtils httpUtils;
    private CountDownTimer countDownTimer;

    private MyProgressDialog mDialog;

    @Override
    protected int loadLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void findView() {
        title_textView = (TextView) findViewById(R.id.title_title_textview);
        user_phone = (EditText) findViewById(R.id.user_phone);
        user_code = (EditText) findViewById(R.id.register_code);
       // user_passwd = (EditText) findViewById(R.id.user_passwd);
       // user_confirm_passwd = (EditText) findViewById(R.id.user_confirm_passwd);
        back_imageView = (ImageView) findViewById(R.id.title_back_imageview);
        img_accetp_code = (Button) findViewById(R.id.img_accept_code);
        right_textView = (TextView) findViewById(R.id.title_right_textview);
        btn_register = (Button) findViewById(R.id.btn_register);
        //check_agreement = (CheckBox) findViewById(R.id.check_agreement);
        mDialog = MyProgressDialog.createLoadingDialog(RegisterActivity.this,"正在加载!");
    }

    @Override
    protected void regListener() {
//        user_phone.setOnFocusChangeListener(this);
//        user_code.setOnFocusChangeListener(this);
       // user_passwd.setOnFocusChangeListener(this);
      //  user_confirm_passwd.setOnFocusChangeListener(this);
        back_imageView.setOnClickListener(this);
        img_accetp_code.setOnClickListener(this);
        right_textView.setOnClickListener(this);
        btn_register.setOnClickListener(this);
      //  check_agreement.setOnCheckedChangeListener(this);

    }

    @Override
    protected void loadData() {

      //  u_passwd = user_passwd.getText().toString().trim();
    //    u_confirm_passwd = user_confirm_passwd.getText().toString().trim();

        title_textView.setText("注册");
        right_textView.setText("登录");
        right_textView.setTextColor(Color.BLUE);
        right_textView.setVisibility(View.VISIBLE);
        httpUtils = new HttpUtils();
        //计时发送验证码的时间
        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                img_accetp_code.setBackgroundResource(R.mipmap.code_time);
                img_accetp_code.setText(String.valueOf(millisUntilFinished / 1000) + "s");
                img_accetp_code.setTextColor(Color.parseColor("#ffffff"));
            }

            @Override
            public void onFinish() {
                img_accetp_code.setClickable(true);
                img_accetp_code.setBackgroundResource(R.mipmap.fasongyzm);
                img_accetp_code.setText("");
            }
        };


        user_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                u_phone = user_phone.getText().toString().trim();

                if(!TextUtils.isEmpty(u_phone)&&!TextUtils.isEmpty(s.toString())){
                    Log.e("info","文本不为空==================");
                    btn_register.setEnabled(true);
                }else{
                    btn_register.setEnabled(false);
                    Log.e("info", "文本为空==================");
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
           case R.id.title_right_textview:
               Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
               startActivity(intent);
               break;
           case R.id.img_accept_code:
               acceptCode();
               break;
           case R.id.btn_register:
               startRegister();
               break;
       }
    }


    /**
     * 注册
     */
    private void startRegister() {
        if (TextUtils.isEmpty(u_phone)) {
            showToast("请输入手机号");
            return;
        }

        if(!isMobileNO(u_phone)){
            showToast("请输入正确的手机号");
            return;
        }

        final String phone = user_phone.getText().toString().trim();
        String SmsCode = user_code.getText().toString().trim();


        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", DESMD5Utils.doDESEncode(phone, HttpUrl.desKey));
        map.put("smsCode", SmsCode);
        String sign = DESMD5Utils.dsigndispost(map);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userName", DESMD5Utils.doDESEncode(phone, HttpUrl.desKey));
        params.addBodyParameter("smsCode", SmsCode);
        params.addBodyParameter("sign", sign);

        httpUtils.send(HttpRequest.HttpMethod.POST, HttpUrl.VERIFICATIONCODE, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                   if(responseInfo!=null){
                       Log.e("info",responseInfo.result);
                       try {
                           JSONObject jsonObject = new JSONObject(responseInfo.result);
                           JSONObject msg = jsonObject.getJSONObject("msg");
                           if ("0".equals(msg.getString("code"))) {
                              Intent register_intent = new Intent(mContext,RegisterNextActivity.class);
                               register_intent.putExtra("phone",phone);
                               startActivity(register_intent);
                               RegisterActivity.this.finish();
                           }else{
                               showToast(msg.getString("text"));
                           }
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                   }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }

    private void acceptCode(){

       String phone = user_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }
        if(!isMobileNO(phone)){
            showToast("请输入正确的手机号");
            return;
        }
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userName", DESMD5Utils.doDESEncode(phone, HttpUrl.desKey));
        String sign = DESMD5Utils.dsigndispost(map);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userName", DESMD5Utils.doDESEncode(phone, HttpUrl.desKey));
        params.addBodyParameter("sign", sign);
        httpUtils.send(HttpRequest.HttpMethod.POST, HttpUrl.SENDCAPTCHA, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                Log.e("info", "执行了start方法");
                mDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                 if(responseInfo!=null){
                     mDialog.dismiss();
                     try {
                         JSONObject jsonObject = new JSONObject(responseInfo.result);
                         JSONObject msg = jsonObject.getJSONObject("msg");
                         if(msg.getString("code").equals("0")){
                             img_accetp_code.setClickable(false);
                             countDownTimer.start();
                         }else{
                             showToast(msg.getString("text"));
                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }else{
                     showToast("网络加载失败");
                 }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast("网络加载失败");
                mDialog.dismiss();
            }

        });
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
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

}
