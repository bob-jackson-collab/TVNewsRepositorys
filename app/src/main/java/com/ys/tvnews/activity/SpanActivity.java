package com.ys.tvnews.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ys.tvnews.R;
import com.ys.tvnews.views.TitleViews;

/**
 * Created by sks on 2015/12/6.
 */
public class SpanActivity extends BaseActivity implements View.OnClickListener{

    private TextView span_title;
    private ImageView back_img;

    @Override
    protected int loadLayout() {
        return R.layout.activity_span;
    }

    @Override
    protected void findView() {
        span_title = (TextView) findViewById(R.id.title_title_textview);
        back_img = (ImageView) findViewById(R.id.title_back_imageview);
    }

    @Override
    protected void regListener() {
        back_img.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
       span_title.setText("扫一扫");
    }

    @Override
    protected void reqServer() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
