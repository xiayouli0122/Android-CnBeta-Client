package com.yuri.cnbeta.model.impl;

import com.activeandroid.query.Select;
import com.yuri.cnbeta.db.NewsItem;
import com.yuri.cnbeta.model.IFavorite;
import com.yuri.cnbeta.model.listener.HttpListResultListener;

import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class FavoriteImpl implements IFavorite {
    @Override
    public void getFavoriteData(HttpListResultListener listResultListener) {
        List<NewsItem> newsItemList = new Select().from(NewsItem.class).execute();
        if (listResultListener != null) {
            listResultListener.onSuccess(newsItemList);
        }
    }
}
