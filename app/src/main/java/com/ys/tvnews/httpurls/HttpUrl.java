package com.ys.tvnews.httpurls;

/**
 * Created by sks on 2015/11/25.
 */
public class HttpUrl {

    public static String desKey = "gpaykey123";
    private static String host = "http://101.200.190.151:8080/gpay/appuser/";
    public static String APP_ID = "1104993098";


    public static String news_host = "http://hot.news.cntv.cn/index.php?";
    /**
     * 要闻
     */

    public static String news_url = news_host + "controller=list&action=getHandDataInfoNew&handdata_id=TDAT1372928688333145&n1=3&n2=20&toutuNum=3";
    /**
     * /** 登陆
     */
    public static String LOGINURL = host + "login_User.action";
    /**
     * 发送验证码
     */
    public static String SENDCAPTCHA = host + "sendSmscode_User.action";
    /**
     * 注册
     */
    public static String REGISTER = host + "reg_User.action";

    /**
     * 验证验证码
     */
    public static String VERIFICATIONCODE = host + "checkSmscode_User.action";

    /**
     * 时间轴
     */
    public static String TIMENEWSURL = "http://chuang.36kr.com/api/actapply?page=1&pageSize=12";

}
