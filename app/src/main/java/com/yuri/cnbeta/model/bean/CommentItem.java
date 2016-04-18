package com.yuri.cnbeta.model.bean;

import com.yuri.cnbeta.http.response.Comment;

/**
 * Created by Yuri on 2016/4/11.
 */
public class CommentItem {

    public String sid;
    /**
     * id of this Comment
     */
    public String tid;
    /**
     * pid, point to the original {@link Comment}
     */
    public String pid;
    public String username;
    public String content;
    public String parentComment;
    public String createdTime;
    public String support;
    public String against;

    public void copy(Comment comment) {
        this.tid = comment.getTid();
        this.pid = comment.getPid();
        this.username = comment.getUsername();
        this.content = comment.getContent();
        this.createdTime = comment.getCreatedTime();
        this.support = comment.getSupport();
        this.against = comment.getAgainst();
    }

}
