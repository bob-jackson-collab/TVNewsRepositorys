package com.ys.tvnews.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ys.tvnews.R;


public class TitleViews extends RelativeLayout {
	private Context context;
	private TextView titleTextView;                    //标题
	private ImageView backImageView;                  // 左侧返回按钮
	private TextView rightTextView;                  // 右侧按钮
	private ImageView rightImageView;               // 右侧图标
	private RelativeLayout titleLayout;
	private View view;


	public TextView getTitleTextView() {
		return titleTextView;
	}

	public void setTitleTextView(TextView titleTextView) {
		this.titleTextView = titleTextView;
	}

	public ImageView getBackImageView() {
		return backImageView;
	}

	public void setBackImageView(ImageView backImageView) {
		this.backImageView = backImageView;
	}

	public TextView getRightTextView() {
		return rightTextView;
	}

	public void setRightTextView(TextView rightTextView) {
		this.rightTextView = rightTextView;
	}

	public ImageView getRightImageView() {
		return rightImageView;
	}

	public void setRightImageView(ImageView rightImageView) {
		this.rightImageView = rightImageView;
	}

	public TitleViews(Context context) {
		super(context);
		this.context = context;
		if (isInEditMode()){
			Log.e("info", "执行了这个方法");
			return;
		}

		initView();
	}

	public TitleViews(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		if (isInEditMode())
			return;
		initView();
	}

	public View getView(){
		if(view!=null) {
			return view;
		}
		return null;
	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.title_view, this);
		view.setBackgroundColor(Color.WHITE);
		titleTextView = (TextView) view.findViewById(R.id.title_title_textview);
		backImageView = (ImageView) view
				.findViewById(R.id.title_back_imageview);
		titleLayout = (RelativeLayout) view.findViewById(R.id.title_layout);
		rightImageView = (ImageView) view
				.findViewById(R.id.title_right_imageview);
		rightTextView = (TextView) view.findViewById(R.id.title_right_textview);
	}

	/**
	 * R.id.title_back_imageview
	 * 
	 * @param click
	 */
	public void setBackClick(OnClickListener click) {
		backImageView.setOnClickListener(click);
	}

	/**
	 * 设置标题背景色
	 * 
	 * @param colorId
	 */
	public void setBackground(int colorId) {
		titleLayout.setBackgroundColor(colorId);
	}

	/**
	 * 设置右侧点击事件 R.id.title_right_textview R.id.title_right_imageview
	 * 
	 * @param click
	 */
	public void setRightClick(OnClickListener click) {
		rightImageView.setOnClickListener(click);
		rightTextView.setOnClickListener(click);
	}

	/**
	 * 设置右侧按钮文字
	 */
	public void setRightText(String str) {
		setRightTextViewVisible(View.VISIBLE);
		rightTextView.setText(str);
	}

	/**
	 * 设置右侧按钮文字
	 */
	public void setRightText(int str) {
		setRightTextViewVisible(View.VISIBLE);
		rightTextView.setText(str);
	}

	/**
	 * 设置右侧文字颜色
	 */
	public void setRightTextColor(int color){
		setRightTextViewVisible(View.VISIBLE);
		rightTextView.setTextColor(color);
	}

	/**
	 * 设置右侧按钮显示与否
	 */
	public void setRightTextViewVisible(int visible) {
		rightTextView.setVisibility(visible);
	}

	/**
	 * 设置右侧imageview图片
	 */
	public void setRightImage(int resourceId) {
		setRightImageViewVisible(View.VISIBLE);
		rightImageView.setImageResource(resourceId);
	}

	/**
	 * 设置图片显示与否
	 */
	public void setRightImageViewVisible(int visible) {
		rightImageView.setVisibility(visible);
	}

	/**
	 * 设置标题文字
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	/**
	 * 设置标题文字
	 * 
	 * @param title
	 */
	public void setTitle(int titleId) {
		titleTextView.setText(titleId);
	}

	/**
	 * 设置标题文字颜色
	 * 
	 * @param title
	 */
	public void setTitleTextColor(int colorId) {
		titleTextView.setTextColor(colorId);
	}

	/**
	 * 设置左侧图标显示与否
	 * 
	 * @param visible
	 */
	public void setLeftVisible(int visible) {
		backImageView.setVisibility(visible);
	}

}
