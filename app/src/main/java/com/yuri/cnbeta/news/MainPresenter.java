package com.yuri.cnbeta.news;

import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.model.bean.NewsType;
import com.yuri.cnbeta.model.listener.HttpListResultListener;

import java.util.List;

 class MainPresenter extends MainContract.Presenter {

    MainPresenter(MainContract.View view) {
        super(view);
    }

    @Override
    void getNews(NewsType newsType, String param, String lastSid) {
        final boolean isMore = (lastSid != null);
        mModel.getNews(newsType, null, lastSid, new HttpListResultListener<Article>() {
            @Override
            public void onSuccess(List<Article> resultList) {
                mView.showData(isMore, resultList);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }

}
