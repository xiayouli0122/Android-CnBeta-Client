package com.yuri.cnbeta.view;

import com.yuri.cnbeta.db.NewsItem;
import com.yuri.cnbeta.http.response.Content;

import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface IFavoriteView extends IBaseView {

    void showData(List<NewsItem> newsItemList);
}
