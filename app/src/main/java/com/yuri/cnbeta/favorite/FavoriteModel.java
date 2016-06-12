package com.yuri.cnbeta.favorite;

import com.activeandroid.query.Select;
import com.yuri.cnbeta.db.model.NewsItem;
import com.yuri.cnbeta.model.listener.HttpListResultListener;

import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class FavoriteModel extends FavoriteContract.Model{
    @Override
    List<NewsItem> getFavoriteData() {
        return new Select().from(NewsItem.class).execute();
    }
}
