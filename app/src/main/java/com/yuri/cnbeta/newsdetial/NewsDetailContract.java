package com.yuri.cnbeta.newsdetial;

import com.yuri.cnbeta.base.BaseNetModel;
import com.yuri.cnbeta.base.BaseNetPresenter;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.view.BaseView;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface NewsDetailContract {

    interface View extends BaseView {
        void showData(Content content);
    }

    abstract class Model extends BaseNetModel {
        abstract void getDetailData(String sid, HttpResultListener<Content> listener);
        abstract void getDetailDataApi(String sid, HttpResultListener<Content> listener);
    }

    abstract class Presenter extends BaseNetPresenter<View, Model> {
        public Presenter(View view) {
            super(view, new NewsDetailModel());
        }
        abstract void getDetailData(String sid);
        abstract void getDetailDataApi(String sid);
        abstract boolean isFavorited(String sid);
        abstract boolean doFavorite(Content content, String topicLogo);
    }
}

