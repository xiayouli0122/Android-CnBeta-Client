package com.yuri.cnbeta.favorite;

import android.content.Context;

import com.yuri.cnbeta.db.model.NewsItem;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.base.BasePresenter;

import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class FavoritePresenter extends FavoriteContract.Presenter {

    public FavoritePresenter(FavoriteContract.View mView) {
        super(mView);
    }

    @Override
    public void getFavoriteData() {
        mView.showData(mModel.getFavoriteData());
    }
}
