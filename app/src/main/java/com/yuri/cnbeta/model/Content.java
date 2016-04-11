package com.yuri.cnbeta.model;

import java.io.Serializable;

/**
 * Bead Content(News Content)
 * Created by Wayne on 2016/2/21.
 */
public class Content implements Comparable<Content>,Serializable {
    private String sid;
    private String topic;
    private String aid;
    private String title;
    private String hometext;
    private String bodytext;
    private String source;
    private String comments;
    private String counter;
    public String inputtime;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHometext() {
        return hometext;
    }

    public void setHometext(String hometext) {
        this.hometext = hometext;
    }

    public String getBodytext() {
        return bodytext;
    }

    public void setBodytext(String bodytext) {
        this.bodytext = bodytext;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    @Override
    public int compareTo(Content another) {
        int aSid = Integer.parseInt(this.getSid());
        int bSid = Integer.parseInt(another.getSid());
        return bSid - aSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Content content = (Content) o;
        return this.getSid().equals(content.getSid());
    }

    @Override
    public int hashCode() {
        return 7 * sid.hashCode()
                + 11 * hometext.hashCode()
                + 13 * bodytext.hashCode();
    }

    @Override
    public String toString() {
        return title +
                "\n " + hometext +
                "\n " + bodytext;
    }
}
