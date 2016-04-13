package com.ys.tvnews.bean;

/**
 * Created by Administrator on 2016/4/11.
 */
public class CommentBean {
    private int id;
    private String userName;
    private String comment;
    private String like;
    private String unLike;
    private String comment_time;
    private String head_image;

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getUnLike() {
        return unLike;
    }

    public void setUnLike(String unLike) {
        this.unLike = unLike;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }
}
