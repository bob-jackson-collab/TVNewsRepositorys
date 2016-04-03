package com.ys.tvnews.bean;

import java.io.Serializable;

/**
 * Created by sks on 2015/12/10.
 */
public class AdvBean implements Serializable{

    private String detailUrl;
    private String itemTitle;
    private String itemImage;

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

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
