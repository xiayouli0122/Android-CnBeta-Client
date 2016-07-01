package com.yuri.cnbeta.http.response;

import com.yuri.xlog.Log;

/**
 * Created by Yuri on 2016/6/12.
 */
public class HttpCommentItem {
    public String score;
    public String tid;
    public String pid;
    public String sid;
    public String reason;
    public String icon;
    public String date;
    public String name;
    public String comment;
    public String host_name;
    public String refContent;

    public void copy(CommentResponse.CommentDetailItem commentItem) {
        this.tid = commentItem.tid;
        this.pid = commentItem.pid;
        this.sid = commentItem.sid;
        this.score = commentItem.score;
        this.reason = commentItem.reason;
        this.icon = commentItem.icon;
        this.date = commentItem.date;
        this.name = commentItem.name;
        this.comment = commentItem.comment;
        this.host_name = commentItem.host_name;
    }
}
