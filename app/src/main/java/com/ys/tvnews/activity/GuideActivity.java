package com.ys.tvnews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ys.tvnews.R;
import com.ys.tvnews.adapter.GuideAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by sks on 2015/12/21.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    private ViewPager guide_viewPager;
    private Button btn_experience;
    private GuideAdapter mAdapter;
    private LinearLayout guide_dots;
    private List<ImageView> list_guide_imageView;
    private List<ImageView> list_dots;
    private int image_id[] = new int[]{R.mipmap.first_guide,R.mipmap.second_guide,R.mipmap.third_guide};
    private SharedPreferences sp;
    private boolean first_start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        //进行手机临时存储文件的操作
        sp = GuideActivity.this.getSharedPreferences("sp_first", Context.MODE_PRIVATE);
        first_start = sp.getBoolean("first_start", true);

        if(!first_start){
            Intent intent = new Intent(GuideActivity.this,StartActivity.class);
            startActivity(intent);
        }else{
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first_start",false);
            editor.commit();
        }

        initView();
        loadData();
        setListener();
        btn_experience.setVisibility(View.GONE);
        mAdapter = new GuideAdapter(list_guide_imageView);
        guide_viewPager.setAdapter(mAdapter);

    }

    private void initView(){
        guide_viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        guide_dots = (LinearLayout) findViewById(R.id.guide_group_dots);
        btn_experience = (Button) findViewById(R.id.btn_experience);
    }

    private void loadData(){
        list_guide_imageView = new ArrayList<>();
        list_dots = new ArrayList<>();
           for(int i=0;i<image_id.length;i++){
                ImageView imageView = new ImageView(GuideActivity.this);
                imageView.setBackgroundResource(image_id[i]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                list_guide_imageView.add(imageView);
           }
        for(int i=0;i<guide_dots.getChildCount();i++){
            ImageView imageView = (ImageView) guide_dots.getChildAt(i);
            imageView.setEnabled(false);
            list_dots.add(imageView);
        }

        guide_dots.getChildAt(0).setEnabled(true);
    }

    private void setListener(){
        guide_viewPager.setOnPageChangeListener(this);
        btn_experience.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<list_dots.size();i++){
            list_dots.get(i).setEnabled(false);
        }
        list_dots.get(position).setEnabled(true);

        if(position==list_dots.size()-1){
            btn_experience.setVisibility(View.VISIBLE);
        }else{
            btn_experience.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_experience:
                Intent intent = new Intent(GuideActivity.this,StartActivity.class);
                startActivity(intent);
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(GuideActivity.this);
        this.finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(GuideActivity.this);

    }

}
