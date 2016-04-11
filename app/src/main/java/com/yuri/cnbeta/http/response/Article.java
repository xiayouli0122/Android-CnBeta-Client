package com.yuri.cnbeta.http.response;

import java.io.Serializable;

/**
 * Created by Wayne on 2016/2/21.
 */
public class Article implements Serializable, Comparable<Article> {

    private String sid;
    private String title;
    private String pubtime;
    private String summary;
    private String topic;
    private String counter;
    private String comments;
    private String topicLogo;
    private String thumb;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTopicLogo() {
        return topicLogo;
    }

    public void setTopicLogo(String topicLogo) {
        this.topicLogo = topicLogo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public int compareTo(Article another) {
        int aSid = Integer.parseInt(this.getSid());
        int bSid = Integer.parseInt(another.getSid());
        return bSid - aSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Article article = (Article) o;
        return this.getSid().equals(article.getSid());
    }

    @Override
    public int hashCode() {
        return 7 * pubtime.hashCode()
                + 11 * topicLogo.hashCode()
                + 13 * thumb.hashCode();
    }

    @Override
    public String toString() {
        return title +
                " " + pubtime +
                " " + comments +
                " " + counter +
                " " + summary;
    }


}
