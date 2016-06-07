package com.yuri.cnbeta.news;

import com.yuri.cnbeta.base.BaseNetModel;
import com.yuri.cnbeta.base.BaseNetPresenter;
import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.model.bean.NewsType;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.BaseView;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface MainContract {

    interface View extends BaseView {
        /**
         * 获取到新闻数据的时候，将之显示到UI上
         * @param articleList 数据列表
         */
        void showData(boolean isMore, List<Article> articleList);
    }

    abstract class Model extends BaseNetModel {
        abstract void getNews(NewsType newsType, String param, String lastSid, HttpListResultListener<Article> listener);
    }

    abstract class Presenter extends BaseNetPresenter<View, Model> {
        public Presenter(View view) {
            super(view, new MainModel());
        }
        abstract void getNews(NewsType newsType, String param, String lastSid);
    }
}

