package com.yuri.cnbeta.favorite;

import com.yuri.cnbeta.base.BasePresenter;
import com.yuri.cnbeta.db.model.NewsItem;
import com.yuri.cnbeta.base.BaseView;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface FavoriteContract {

    interface View extends BaseView {
        void showData(List<NewsItem> newsItemList);
    }

    abstract class Model {
        abstract List<NewsItem> getFavoriteData();
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        public Presenter(View view) {
            super(view, new FavoriteModel());
        }

        abstract void getFavoriteData();
    }
}
