package com.yuri.cnbeta.db;

import com.activeandroid.query.Select;
import com.yuri.cnbeta.db.model.NewsItem;
import com.yuri.cnbeta.http.response.Content;

/**
 * Created by Yuri on 2016/6/7.
 */

public class NewsDBManager {

    public static NewsItem getFavorite(String sid) {
        return new Select().from(NewsItem.class).where("sid=?", sid).executeSingle();
    }

    public static boolean doFavorite(Content content, String topicLogo) {
        NewsItem newsItem = getFavorite(content.sid);
        if (newsItem != null) {
            return true;
        } else {
            newsItem = new NewsItem();
        }
        newsItem.sid = content.sid;
        newsItem.aid = content.aid;
        newsItem.bodytext = content.bodytext;
        newsItem.comments = content.comments;
        newsItem.counter = content.counter;
        newsItem.hometext = content.hometext;
        newsItem.source = content.source;
        newsItem.title = content.title;
        newsItem.topic = content.topic;
        newsItem.time = content.time;
        newsItem.topicLogo = topicLogo;
        return newsItem.save() != -1;
    }
}
