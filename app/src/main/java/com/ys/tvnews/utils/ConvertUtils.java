package com.ys.tvnews.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 参数转换实体类
 * @author Master
 */
public class ConvertUtils 
{
	/**
	 * 将dp值转化为像素px值
	 * @param mContext 
	 * @param convertValue
	 * @return px像素值
	 */
	public static int dp2px(Context mContext , float convertValue)
	{
		final float density = mContext.getResources().getDisplayMetrics().density;
		return (int) (convertValue * density + 0.5f);
	}
	
	/**
	 * 系统的方法将dp值转换为px像素值
	 * @param convertValue
	 * @param mContext
	 * @return
	 */
	public static int dp2px(float convertValue,Context mContext) 
	{
		return (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP
				,convertValue
				,mContext.getResources().getDisplayMetrics());
	}
	
	/**
	 * 将px像素值转换成dp值
	 * @param mContext
	 * @param convertValue
	 * @return
	 */
	public static int px2dp(Context mContext , float convertValue)
	{
		final float density = mContext.getResources().getDisplayMetrics().density;
		return (int) (convertValue / density + 0.5f);
	}
	
	/**
	 * 将px像素值转换为sp字体大小
	 * @param mContext
	 * @param convertValue
	 * @return
	 */
	public static int px2sp(Context mContext,float convertValue)
	{
		final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
		return (int) (convertValue / fontScale + 0.5f);
	}
	
	/**
	 * 将sp字体大小转换为px像素值 
	 * @param mContext
	 * @param convertValue
	 * @return
	 */
	public static int sp2px(Context mContext , float convertValue) 
	{
		final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
		return (int) (convertValue * fontScale + 0.5f);
	}
	
	/**
	 * 系统方法将sp字体大小值转换为px像素值
	 * @param convertValue
	 * @param mContext
	 * @return
	 */
	public static int sp2px(float convertValue,Context mContext) 
	{
		return (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP
				,convertValue
				,mContext.getResources().getDisplayMetrics());
	}
	
}
