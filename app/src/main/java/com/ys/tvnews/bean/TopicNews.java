package com.ys.tvnews.bean;

import java.io.Serializable;

/**
 * Created by sks on 2016/1/25.
 */
public class TopicNews implements Serializable {

    private String voteid;
    private String voteimage;
    private String votetitle;
    private String weburl;

    public String getVoteid() {
        return voteid;
    }

    public void setVoteid(String voteid) {
        this.voteid = voteid;
    }

    public String getVoteimage() {
        return voteimage;
    }

    public void setVoteimage(String voteimage) {
        this.voteimage = voteimage;
    }

    public String getVotetitle() {
        return votetitle;
    }

    public void setVotetitle(String votetitle) {
        this.votetitle = votetitle;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }
}
