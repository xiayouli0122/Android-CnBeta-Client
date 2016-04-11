package com.yuri.cnbeta.http.response;

import java.io.Serializable;

/**
 * Bean Comment(News Comment)
 * Created by Wayne on 2016/2/21.
 */
public class Comment implements Comparable<Comment>, Serializable {
    /**
     * id of this Comment
     */
    private String tid;
    /**
     * pid, point to the original {@link Comment}
     */
    private String pid;
    private String username;
    private String content;
    private String createdTime;
    private String support;
    private String against;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getAgainst() {
        return against;
    }

    public void setAgainst(String against) {
        this.against = against;
    }

    @Override
    public int compareTo(Comment another) {
        int aTid = Integer.parseInt(this.getTid());
        int bTid = Integer.parseInt(another.getTid());
        return aTid - bTid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;
        return this.getTid().equals(comment.getTid());
    }

    @Override
    public int hashCode() {
        return 7 * tid.hashCode()
                + 11 * pid.hashCode()
                + 13 * createdTime.hashCode();
    }

    @Override
    public String toString() {
        return tid +
                " " + content +
                " " + createdTime;
    }
}
