package com.ys.tvnews.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2015/12/7.
 */
public class NewsBean implements Serializable{

    private String detailUrl;
    private String itemTitle;
    private String imgUrl1;

    public static List<NewsBean> getNewsList(String content){
        List<NewsBean> list_newsBean = new ArrayList<>();
        return null;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }
}
