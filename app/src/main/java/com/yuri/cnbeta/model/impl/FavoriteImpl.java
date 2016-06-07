package com.yuri.cnbeta.model.impl;

import com.activeandroid.query.Select;
import com.yuri.cnbeta.db.model.NewsItem;
import com.yuri.cnbeta.model.FavoriteModel;
import com.yuri.cnbeta.model.listener.HttpListResultListener;

import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class FavoriteImpl implements FavoriteModel {
    @Override
    public void getFavoriteData(HttpListResultListener listResultListener) {
        List<NewsItem> newsItemList = new Select().from(NewsItem.class).execute();
        if (listResultListener != null) {
            listResultListener.onSuccess(newsItemList);
        }
    }
}
