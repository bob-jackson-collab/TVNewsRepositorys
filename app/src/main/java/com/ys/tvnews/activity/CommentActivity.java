package com.ys.tvnews.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.ys.tvnews.R;
import com.ys.tvnews.adapter.CommentAdapter;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.bean.CommentBean;
import com.ys.tvnews.views.TitleViews;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class CommentActivity extends BaseActivity implements View.OnClickListener{

    private TitleViews mTitleView;
    private ListView comment_list_view;
    private EditText et_comment;
    private Button send_comment;
    private Context mContext;
    private List<CommentBean> list_comment;
    private DbUtils mDbUtils;
    private CommentAdapter mAdapter;

    @Override
    protected int loadLayout() {
        return R.layout.activity_comment;
    }

    @Override
    protected void findView() {
        mContext = CommentActivity.this;
        mTitleView = new TitleViews(mContext);
        mTitleView.setTitle("评论");
        comment_list_view = (ListView) findViewById(R.id.comment_list_view);
        et_comment = (EditText) findViewById(R.id.et_comment);
        send_comment = (Button) findViewById(R.id.send_comment);
    }

    @Override
    protected void regListener() {
        et_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!=null || !"".equals(s.toString())){
                    send_comment.setBackgroundColor(Color.parseColor("#ff00ee"));
                }else{
                    send_comment.setBackgroundColor(Color.parseColor("#cccccc"));
                }
            }
        });

        send_comment.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        mDbUtils = MyApplication.getDbInstance();
        list_comment = new ArrayList<>();
        mAdapter = new CommentAdapter(CommentActivity.this);
        CommentBean commentBean = new CommentBean();
        commentBean.setComment("你好啊！！！！");
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式Simple
        String time = df.format(date);
        commentBean.setComment_time(time);
        commentBean.setLike("10");
        commentBean.setUnLike("2");
        commentBean.setUserName("张弘扬");
        try {
            mDbUtils.save(commentBean);
        } catch (DbException e) {
            e.printStackTrace();
        }
        list_comment.add(commentBean);
        comment_list_view.setAdapter(mAdapter);
        mAdapter.addAllList(list_comment);
        //    list_comment.add
    }

    @Override
    protected void reqServer() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_comment:
                break;
            default : break;
        }
    }
}
