package com.ys.tvnews.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 类名: BaseFragment 描述: Fragment基础抽象类，提供规范的方法 作者：杨松
 */
public abstract class BaseFragment extends Fragment {
	protected Context mContext;
	private ProgressDialog progress;
	String mtype;// 设备号
	String imei;// 唯一标识
//	protected AbHttpUtil mAbHttpUtil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int layout = loadLayout();
		View view = inflater.inflate(layout, null);
		mContext = getActivity();
//		mAbHttpUtil = AbHttpUtil.getInstance(mContext);
		findView(view);
		regListener();
		loadData();
		requestServer();
		return view;
	}

	/** 加载布局 */
	protected abstract int loadLayout();

	/** 初始化组件 */
	protected abstract void findView(View view);

	/** 注册监听 */
	protected abstract void regListener();

	/** 加载数据 */
	protected abstract void loadData();

	/** 请求网络 */
	public abstract void requestServer();

	public void requestFail() {
		Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
	}

	public void NetFail() {
		Toast.makeText(getActivity(), "网络不给力哦！请检查网络", Toast.LENGTH_SHORT)
				.show();
	}

	public void showToast(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int resource) {
		Toast.makeText(mContext, getString(resource), Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 描述：显示进度框.
	 */
	public void showProgressDialog() {
		progress = new ProgressDialog(mContext);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.show();
	}

	public void removeProgressDialog() {
		progress.dismiss();
	}

}
