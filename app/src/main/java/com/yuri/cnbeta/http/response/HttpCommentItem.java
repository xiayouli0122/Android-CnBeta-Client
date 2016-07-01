package com.yuri.cnbeta.http.response;

/**
 * Created by Yuri on 2016/6/12.
 */
public class HttpCommentItem {
    public int score;
    public String tid;
    public String pid;
    public String parent;//parent id
    public int sid;
    public int reason;
    public String icon;
    public String date;
    public String name;
    public String comment;
    public String host_name;
    public String refContent;

    public void copy(HttpCommentItem commentItem) {
        this.score = commentItem.score;
        this.reason = commentItem.reason;
        this.icon = commentItem.icon;
        this.date = commentItem.date;
        this.name = commentItem.name;
        this.comment = commentItem.comment;
        this.host_name = commentItem.host_name;
    }
}
