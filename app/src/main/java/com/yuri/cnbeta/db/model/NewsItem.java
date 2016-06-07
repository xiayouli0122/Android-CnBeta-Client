package com.yuri.cnbeta.db.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Yuri on 2016/4/13.
 */
@Table(name = "NewsItem")
public class NewsItem extends Model {
    @Column(name = "sid")
    public String sid;
    @Column(name = "topic")
    public String topic;
    @Column(name = "aid")
    public String aid;
    @Column(name = "title")
    public String title;
    @Column(name = "hometext")
    public String hometext;
    @Column(name = "bodytext")
    public String bodytext;
    @Column(name = "source")
    public String source;
    @Column(name = "comments")
    public String comments;
    @Column(name = "counter")
    public String counter;
    @Column(name = "time")
    public String time;
    @Column(name = "topicLogo")
    public String topicLogo;
}
