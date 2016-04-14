package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.db.NewsItem;
import com.yuri.cnbeta.model.IFavorite;
import com.yuri.cnbeta.model.impl.FavoriteImpl;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.IFavoriteView;

import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class FavoritePresenter extends BasePresenter<IFavoriteView>  {

    private IFavorite mFavorite;

    public FavoritePresenter(Context mContext, IFavoriteView mView) {
        super(mContext, mView);
        mFavorite = new FavoriteImpl();
    }

    public void getData() {
        mFavorite.getFavoriteData(new HttpListResultListener<NewsItem>() {
            @Override
            public void onSuccess(List<NewsItem> resultList) {
                mView.showData(resultList);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }
}
