package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.model.INewsDetail;
import com.yuri.cnbeta.model.impl.NewsDetailImpl;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.view.INewsDetailView;
import com.yuri.cnbeta.view.ui.NewsDetailActivity;

/**
 * 新闻详情的Presenter
 * Created by Yuri on 2016/4/13.
 */
public class NewsDetailPresenter extends BasePresenter<INewsDetailView>
        implements IBaseNetPresenter<NewsDetailActivity>{

    private INewsDetail mNewsDetail;

    public NewsDetailPresenter(Context mContext, INewsDetailView mView) {
        super(mContext, mView);

        mNewsDetail = new NewsDetailImpl();
    }

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
    public void cancelRequestBySign(Class<NewsDetailActivity> clazz) {
        mNewsDetail.cancelRequestBySign(clazz);
    }
}
