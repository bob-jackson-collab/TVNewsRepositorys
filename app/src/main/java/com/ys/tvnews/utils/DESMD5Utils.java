package com.ys.tvnews.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.annotation.SuppressLint;

/**
 * http请求处理参数类，有DES加密方法，有MD5加密。还有参数排序方法 sortParams排序。 doDESEncode加密DES MD5 加密MD5
 * 
 * @author Administrator
 * 
 */

@SuppressLint("DefaultLocale")
public class DESMD5Utils {
	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	/**
	 * http请求时候调用 字典排序 在请求的时候需要把参数进行排序。调用方法
	 * 
	 * @param params
	 * @return
	 */
	public static String sortParams(HashMap<String, String> params) {
		if (params == null) {
			return "";
		}
		Collection<String> keyset = params.keySet();
		List<String> list = new ArrayList<String>(keyset);
		// 对key键�?按字典升序排�?
		Collections.sort(list);
		String signCode = "";
		for (int i = 0; i < list.size(); i++) {
			// signCode = signCode + list.get(i) + params.get(list.get(i));
			if (isChinese(params.get(list.get(i)))) {
				signCode = signCode + list.get(i) + params.get(list.get(i));
			} else {
				signCode = signCode + list.get(i) + params.get(list.get(i));
			}
		}
		return signCode;
	}

	// 完整的判断中文汉字和符号
	public static boolean isChinese(String strName) {
		if (strName == null) {
			return false;
		}
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * http请求时候调用 根据传�?的密钥对明文进行加密 DES加密
	 * 
	 * @param plainText
	 *            明文
	 * @param key
	 *            密钥
	 * @return 密文
	 */
	public static String doDESEncode(String plainText, String key) {
		// 密文
		String cipherText = null;

		byte[] keyByte = key.getBytes();
		byte[] inputData = plainText.getBytes();
		try {

			inputData = encrypt(inputData, BASE64Encoder(keyByte));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			cipherText = BASE64Encoder(inputData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;

	}

	public static String BASE64Encoder(byte[] data) {
		int start = 0;
		int len = data.length;
		StringBuffer buf = new StringBuffer(data.length * 3 / 2);

		int end = len - 3;
		int i = start;
		int n = 0;

		while (i <= end) {
			int d = (((data[i]) & 0x0ff) << 16)
					| (((data[i + 1]) & 0x0ff) << 8) | ((data[i + 2]) & 0x0ff);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append(legalChars[d & 63]);

			i += 3;

			if (n++ >= 14) {
				n = 0;
				// buf.append(" ");
			}
		}

		if (i == start + len - 2) {
			int d = (((data[i]) & 0x0ff) << 16) | (((data[i + 1]) & 255) << 8);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append("=");
		} else if (i == start + len - 1) {
			int d = ((data[i]) & 0x0ff) << 16;

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append("==");
		}

		return buf.toString();
	}

	public static byte[] encrypt(byte[] data, String key) throws Exception {
		Key k = toKey(BASE64Decoder(key));
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	private static Key toKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(dks);

		// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
		// SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
		return secretKey;
	}

	/**
	 * Decodes the given Base64 encoded String to a new byte array. The byte
	 * array holding the decoded data is returned.
	 */
	public static byte[] BASE64Decoder(String s) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			_base64Decode(s, bos);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		byte[] decodedBytes = bos.toByteArray();
		try {
			bos.close();
			bos = null;
		} catch (IOException ex) {
			System.err.println("Error while decoding BASE64: " + ex.toString());
		}
		return decodedBytes;
	}

	private static void _base64Decode(String s, OutputStream os)
			throws IOException {
		int i = 0;

		int len = s.length();

		while (true) {
			while (i < len && s.charAt(i) <= ' ')
				i++;
			if (i == len)
				break;
			int tri = (_base64Decode(s.charAt(i)) << 18)
					+ (_base64Decode(s.charAt(i + 1)) << 12)
					+ (_base64Decode(s.charAt(i + 2)) << 6)
					+ (_base64Decode(s.charAt(i + 3)));
			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=')
				break;
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=')
				break;
			os.write(tri & 255);
			i += 4;
		}
	}

	private static int _base64Decode(char c) {
		if (c >= 'A' && c <= 'Z')
			return (c) - 65;
		else if (c >= 'a' && c <= 'z')
			return (c) - 97 + 26;
		else if (c >= '0' && c <= '9')
			return (c) - 48 + 26 + 26;
		else
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new RuntimeException("unexpected code: " + c);
			}
	}

	/**
	 * MD5加密
	 * 
	 * @param str
	 * @param isUp
	 *            true是大写
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String MD5(String str, boolean isUp) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		if (isUp) {
			return (hexValue.toString()).toUpperCase();
		} else {
			return hexValue.toString();
		}
	}

	// 对请求参数进行排序，加密。具体加密措施后台配合
	public static String dsigndispost(HashMap<String, String> DesParameter) {
		// String str = DESMD5Utils.sortParams(DesParameter);
		// String str1 = DESMD5Utils.doDESEncode(str,HttpParameter.DESKEY);
		// Log.i("TAG", "MD5加密以前=="+str1.toString());

		String str3 = DESMD5Utils.MD5(
				DESMD5Utils.doDESEncode(DESMD5Utils.sortParams(DesParameter),
						"deskeydesket") + "md5key", true);
		return str3;
	}

	private static String checkEmail = "^\\w+((_-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

	public static boolean isCorrectEmail(String email) {
		Pattern regex = Pattern.compile(checkEmail);
		Matcher matcher = regex.matcher(email);
		return matcher.matches();
	}

	private static String checkxshlw = "(?!0\\d|[.]+$)\\d+(\\.\\d{0,2})?$";

	public static boolean isxiaoshudian(String xiaoshu) {
		Pattern paxs = Pattern.compile(checkxshlw);
		Matcher xiaosma = paxs.matcher(xiaoshu);
		return xiaosma.matches();
	}

	private static String checkPhone = "^((14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,2-9]))\\d{8}$";

	public static boolean isCorrectPhone(String phone) {
		Pattern p = Pattern.compile(checkPhone);
		Matcher m = p.matcher(phone);
		return m.matches();
	}

}
