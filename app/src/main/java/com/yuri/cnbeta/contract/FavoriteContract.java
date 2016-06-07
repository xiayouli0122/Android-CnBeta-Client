package com.yuri.cnbeta.contract;

import com.yuri.cnbeta.db.model.NewsItem;
import com.yuri.cnbeta.presenter.BasePresenterInterface;
import com.yuri.cnbeta.view.BaseView;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface FavoriteContract {

    interface View extends BaseView {
        void showData(List<NewsItem> newsItemList);
    }

    interface Presenter extends BasePresenterInterface{
        void getFavoriteData();
    }
}
