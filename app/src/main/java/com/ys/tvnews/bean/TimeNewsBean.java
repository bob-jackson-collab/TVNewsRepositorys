package com.ys.tvnews.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/27.
 */
public class TimeNewsBean implements Serializable{

    private String createTime;
    private String listImageUrl;
    private String link;
    private String description;

    public TimeNewsBean(){

    }

    public TimeNewsBean(String createTime, String listImageUrl, String link, String description) {
        this.createTime = createTime;
        this.listImageUrl = listImageUrl;
        this.link = link;
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getListImageUrl() {
        return listImageUrl;
    }

    public void setListImageUrl(String listImageUrl) {
        this.listImageUrl = listImageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
