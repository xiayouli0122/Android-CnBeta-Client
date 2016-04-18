package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.contract.FavoriteContract;
import com.yuri.cnbeta.db.NewsItem;
import com.yuri.cnbeta.model.FavoriteModel;
import com.yuri.cnbeta.model.impl.FavoriteImpl;
import com.yuri.cnbeta.model.listener.HttpListResultListener;

import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class FavoritePresenter extends BasePresenter<FavoriteContract.View> implements FavoriteContract.Presenter {

    private FavoriteModel mFavorite;

    public FavoritePresenter(Context mContext, FavoriteContract.View mView) {
        super(mContext, mView);
        mFavorite = new FavoriteImpl();
    }

    @Override
    public void getFavoriteData() {
        mFavorite.getFavoriteData(new HttpListResultListener<NewsItem>() {
            @Override
            public void onSuccess(List<NewsItem> resultList) {
                mView.showData(resultList);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }
}
