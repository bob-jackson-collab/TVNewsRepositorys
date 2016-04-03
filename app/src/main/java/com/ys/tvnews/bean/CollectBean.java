package com.ys.tvnews.bean;

import java.io.Serializable;

/**
 * Created by sks on 2016/1/25.
 */
public class CollectBean implements Serializable {

    private String imageUrl1;
    private String detailUrl;
    private String itemTitle;


    public CollectBean(String imageUrl1, String detailUrl, String itemTitle) {
        this.imageUrl1 = imageUrl1;
        this.detailUrl = detailUrl;
        this.itemTitle = itemTitle;
    }

    public CollectBean(){

    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
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
}
