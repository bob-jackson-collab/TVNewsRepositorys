package com.ys.tvnews.httpurls;

public class HttpUrls {
	public static String desKey = "gpaykey123";
	// Au测试
	// private static String
	// host="http://gpay.paynews.net:8081/gpayv2/appuser/";
	// public static String ipHost = "http://gpay.paynews.net:8081/gpayv2/";
	// public static String picUrlHost
	// ="http://gpay.paynews.net:8081/gpayv2/upload/src/";
	// public static String
	// headUrlHost="http://gpay.paynews.net:8081/gpayv2/upload/headpic/";
	// public static final String NOTIFY_URL = host + "zfbCallback_Gold.action";
	// public static String COUPONS_USER_REGULAR = ipHost + "help/rule.jsp";

	// 內地网址
	// http://10.0.1.13:8081/gpay/appuser/

	private static String host = "http://101.200.190.151:8080/gpay/appuser/";
	public static String ipHost = "http://101.200.190.151:8080/gpay/";
	public static String picUrlHost = "http://101.200.190.151:8080/gpay/upload/src/";
	public static String headUrlHost = "http://101.200.190.151:8080/gpay/upload/headpic/";
	public static final String NOTIFY_URL = host + "zfbCallback_Gold.action";
	public static String COUPONS_USER_REGULAR = ipHost + "help/rule.jsp";
	// 真实环境 https://www.32gold.com/
//	private static String host = "https://www.32gold.com/appuser/";
//	public static String ipHost = "https://www.32gold.com/";
//	public static String picUrlHost = "https://www.32gold.com/upload/src/";
//	public static String headUrlHost = "https://www.32gold.com/upload/headpic/";
//	public static final String NOTIFY_URL = host + "zfbCallback_Gold.action";
//	public static String COUPONS_USER_REGULAR = ipHost + "help/rule.jsp";

	/**
	 * 提现
	 */
	public static String WITHDRAWALS = host + "tx_Money.action";
	/**
	 * 绑定银行卡
	 */
	public static String BINDBANK = host + "bindBank_Money.action";
	/**
	 * 可绑卡银行
	 */
	public static String BINDBANKNAME = host + "listBank_Pro.action";
	/**
	 * 关于我们
	 */
	public static String ABOUTUS = ipHost + "html5/detail.jsp";
	/**
	 * 删除地址
	 */
	public static String DELETEADDRESS = host + "delete_Address.action";
	/**
	 * 现金交易记录
	 */
	public static String CASHTRANSACTIONRECORD = host + "list_Money.action";
	/**
	 * 修改支付密码
	 */
	public static String CHANGEPAYPWD = host + "updatePayPwd_User.action";
	/**
	 * 用户详情
	 */
	public static String USERDETAIL = host + "userDetail_User.action";
	/**
	 * 上传头像
	 */
	public static String UPLOADHEAD = host + "upHeadPic_User.action";
	/**
	 * 理财问答
	 */
	public static String MANAGEFINANCESQUESTION = host + "list_Help.action";
	/**
	 * 修改收货地址
	 */
	public static String UPDATEADDRESS = host + "edit_Address.action";
	/**
	 * 开黄金交易发票
	 */
	public static String GOLDINVOICING = host + "openReceipt_Gold.action";
	/**
	 * 黄金交易记录
	 */
	public static String GOLDTRANSACTIONRECORD = host + "list_Gold.action";
	/**
	 * 充值
	 */
	public static String RECHARGE = host + "recharge_Money.action";
	/**
	 * 卖出黄金
	 */
	public static String SELLGOLD = host + "sellGold_Gold.action";
	/**
	 * /** 登陆
	 */
	public static String LOGINURL = host + "login_User.action";
	/**
	 * 发送验证码
	 */
	public static String SENDCAPTCHA = host + "sendSmscode_User.action";
	/**
	 * 验证验证码
	 */
	public static String VERIFICATIONCODE = host + "checkSmscode_User.action";
	/**
	 * 注册
	 */
	public static String REGISTER = host + "reg_User.action";
	/**
	 * 黄金价格走势
	 */
	public static String GOLDPRICECHARTS = host + "listPrice_Set.action";
	/**
	 * 发送找回密码验证码
	 */
	public static String SENDFORGOTPWD = host + "findPwd_User.action";
	/**
	 * 找回登陆密码设置密码
	 */
	public static String SETFORGOTPWD = host + "updatePwd_User.action";
	/**
	 * 获取黄金账户信息
	 */
	public static String GOLDACCOUNT = host + "detailGold_Account.action";

	/**
	 * 获取余额
	 */
	public static String BALANCE = host + "";

	/**
	 * 百克收益
	 */
	public static String HUNDREDGRAMEARNINGS = host + "listGramRate_Set.action";

	/**
	 * 黄金收益率
	 */
	public static String GOLDYIELDS = host + "listRate_Set.action";
	/**
	 * 累计收益
	 */
	public static String TOTALEARNINGS = host + "listIncome_Account.action";

	/**
	 * 黄金最新价格
	 */
	public static String NOWGOLDPRICE = host + "newPrice_Set.action";
	/**
	 * 账户各种信息
	 */
	public static String ACCOUNTINFO = host + "account_Account.action";

	/**
	 * 玩黄金广告轮播
	 */
	public static String PLAYADIMG = host + "list_NoticePic.action";
	/**
	 * 我的银行卡
	 */
	public static String BANKCARDLIST = host + "listUserBank_Money.action";
	/**
	 * 验证登录密码
	 */
	public static String VERIFYLOGINPWD = host + "modifyPwd1_User.action";
	/**
	 * 修改登录密码
	 */
	public static String CHANGELOGINPWD = host + "modifyPwd2_User.action";
	/**
	 * 你提我改
	 */
	public static String OPINION = host + "addSuggest_User.action";
	/**
	 * 地址列表
	 */
	public static String ADDRESSLIST = host + "list_Address.action";
	/**
	 * 添加收货地址
	 */
	public static String ADDADDRESS = host + "add_Address.action";
	/**
	 * 买入黄金预期收益时间
	 */
	public static String BUYGOLDEARNINGSDate = host + "incomeDate_Gold.action";

	/**
	 * 定期理财
	 */
	public static String REGULARFINANCIALREGUL = host
			+ "list_LimitMoney.action";

	/**
	 * 定期理财-购买
	 */

	public static String BUYLIMITMONTY = host + "buy_LimitMoney.action";

	/**
	 * 定期理财-交易记录
	 */

	public static String TRANSACTIONRECORD = host
			+ "tradeList_LimitMoney.action";

	/**
	 * 定期理财-提前赎回
	 */

	public static String EARLYREDEM = host + "shuhui_LimitMoney.action";
	/**
	 * 买入黄金
	 */
	public static String BUYGOLD = host + "buyGold_Gold.action";

	/**
	 * 线下存金提交
	 */

	public static String GOLDOFLINECOMMIT = host + "takeGoldInfo_UnLine.action";

	/**
	 * 黄金兑换列表
	 */

	public static String GOLDEXCHANGELIST = host + "list_Change.action";

	/**
	 * 存金记录
	 */

	public static String GOLDRECORD = host + "list_UnLine.action";

	/**
	 * 根据地址ID获取地址信息
	 */

	public static String GOLDRECORDDETAIL = host + "detailOrder_UnLine.action";

	/**
	 * 确认收货
	 */
	public static String GOLDCONFIMRETURN = host + "conreceipt_UnLine.action";

	/**
	 * 兑换记录
	 */
	public static String GOLDEXCHANGERECORD = host + "listOrder_Change.action";

	/**
	 * 兑换
	 */

	public static String GOLDEXCHANGE = host + "change_Change.action";
	/**
	 * 兑换-图片集
	 */

	public static String GOLDEXCHANGEURLS = host + "listPic_Change.action";
	/**
	 * 存金戴-列表
	 */
	public static String GOLDDEPOSIT = host + "list_GoldWear.action";
	/**
	 * 购买存金戴
	 */
	public static String BUYGOLDDEPOSIT = host + "buy_GoldWear.action";

	/**
	 * 存金戴记录
	 */
	public static String GOLDDEPOSITRECORD = host + "listOrder_GoldWear.action";

	/**
	 * 归还存金戴产品
	 */
	public static String RETURNGOLDDEPOSIT = host + "ret_GoldWear.action";

	/**
	 * 存金戴-图片集
	 */

	public static String GOLDDEPOSITDETAILURLS = host
			+ "listPic_GoldWear.action";

	/**
	 * 存金戴-确认收货
	 */
	public static String GOLDDEPOSITCONFIRMRECEIPT = host
			+ "receive_GoldWear.action";

	/**
	 * 存金贷
	 */
	public static String DEPOSITLOAN = host + "list_GoldLoan.action";

	/**
	 * 存金贷-购买
	 */
	public static String BUYDEPOSITLOAN = host + "buy_GoldLoan.action";

	/**
	 * 存金贷-交易记录
	 */

	public static String DEPOSITLOANRECORD = host + "listOrder_GoldLoan.action";

	/**
	 * 存金贷-返还
	 */

	public static String DEPOSITLOANREPAYMENT = host
			+ "returnLoan_GoldLoan.action";

	/**
	 * 版本更新
	 */

	public static String VERSIONUPDATE = ipHost
			+ "versionInter/listVersionInter.action";

	/**
	 * 金保险产品列表
	 */
	public static String GOLDINSURELIST = host + "list_GoldSafe.action";

	/**
	 * 买入黄金保险
	 */
	public static String BUYGOLDSURE = host + "buy_GoldSafe.action";

	/**
	 * 购买金保险记录
	 */
	public static String GOLDINSURANCERECORD = host
			+ "listOrder_GoldSafe.action";

	/**
	 * 金商城
	 */
	public static String GOLDSHOPLIST = host + "list_GoldShop.action";
	/**
	 * 金商城-图片集
	 */

	public static String GOLDSHOPPICLIST = host + "picList_GoldShop.action";

	/**
	 * 线下存金提交快递信息
	 */
	public static String SENDGOLDONLINE = host + "sendGold_UnLine.action";

	/**
	 * 找回支付密码
	 */
	public static String FINDPAYPASS = host + "regetPayPwd_User.action";
	/**
	 * 黄金兑换确认收货
	 */
	public static String EXCHANGESHOUHUO = host + "receive_Change.action";

	/**
	 * 银行卡解绑
	 */
	public static String DELETEBANK = host + "delUserBank_Money.action";
	/**
	 * 消息列表
	 */
	public static String LISTMSG_NOTICE = host + "listMsg_Notice.action";
	/**
	 * 消息列表详情页
	 */
	public static String MSG_NOTICE = host + "read_Notice.action";
	/**
	 * 请求优惠券接口
	 */
	// public static String MYCUPONS=host+"activity_Activity.action";
	public static String MYCUPONS = host + "myCoupons_Activity.action";
	/**
	 * 分享成功 的优惠券接口
	 */
	public static String SHARE_SUCESS = host
			+ "grabRedPaperCoupons_Activity.action";
	/**
	 * 分享内容 回调
	 */
	public static String SHARE_CONTENT = host + "listShare_NoticePic.action";
}
