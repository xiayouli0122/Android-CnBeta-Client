package com.yuri.cnbeta.contract;

import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.presenter.BaseNetPresenter;
import com.yuri.cnbeta.view.BaseView;
import com.yuri.cnbeta.view.ui.NewsDetailActivity;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface NewsDetailContract {

    interface View extends BaseView {
        void showData(Content content);
    }

    interface Presenter extends BaseNetPresenter<NewsDetailActivity> {
        void getData(String sid);
        boolean isFavorited(String sid);
        boolean doFavorite(Content content, String topicLogo);
    }

}
