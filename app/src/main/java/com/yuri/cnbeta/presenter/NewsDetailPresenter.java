package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.contract.NewsDetailContract;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.model.NewsDetailModel;
import com.yuri.cnbeta.model.impl.NewsDetailImpl;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.view.ui.NewsDetailActivity;

/**
 * 新闻详情的Presenter
 * Created by Yuri on 2016/4/13.
 */
public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.View>
        implements NewsDetailContract.Presenter {

    private NewsDetailModel mNewsDetail;

    public NewsDetailPresenter(Context mContext, NewsDetailContract.View mView) {
        super(mContext, mView);
        mNewsDetail = new NewsDetailImpl();
    }

    @Override
    public void getData(String sid) {
        mNewsDetail.getData(mContext, sid, new HttpResultListener<Content>() {
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
        return mNewsDetail.isFavorited(sid);
    }

    @Override
    public boolean doFavorite(Content content, String topicLogo) {
        return mNewsDetail.doFavorite(content, topicLogo);
    }

    @Override
    public void cancelRequestBySign(Class<NewsDetailActivity> clazz) {
        mNewsDetail.cancelRequestBySign(clazz);
    }

}
