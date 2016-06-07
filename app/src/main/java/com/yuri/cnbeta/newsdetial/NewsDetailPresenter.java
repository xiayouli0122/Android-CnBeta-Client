package com.yuri.cnbeta.newsdetial;

import com.yuri.cnbeta.db.NewsDBManager;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.model.listener.HttpResultListener;

/**
 * 新闻详情的Presenter
 * Created by Yuri on 2016/4/13.
 */
class NewsDetailPresenter extends NewsDetailContract.Presenter {

    NewsDetailPresenter(NewsDetailContract.View view) {
        super(view);
    }

    @Override
    void getDetailData(String sid) {
        mModel.getDetailData(sid, new HttpResultListener<Content>() {
            @Override
            public void onSuccess(Content result) {
                mView.showData(result);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }

    @Override
    public boolean isFavorited(String sid) {
        return NewsDBManager.getFavorite(sid) != null;
    }

    @Override
    public boolean doFavorite(Content content, String topicLogo) {
        return NewsDBManager.doFavorite(content, topicLogo);
    }

}
