package com.ys.tvnews.bean;

import java.io.Serializable;

/**
 * Created by sks on 2016/2/20.
 */
public class PushNews implements Serializable{

    private int id;
    private String message;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
