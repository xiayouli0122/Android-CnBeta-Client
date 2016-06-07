package com.yuri.cnbeta.base;

import com.yuri.cnbeta.view.BaseView;

/**
 * Created by Yuri on 2016/6/7.
 */

public class BaseNetPresenter<T extends BaseView, E extends BaseNetModel> {
    protected T mView;
    protected E mModel;

    public BaseNetPresenter(T view, E model) {
        mView = view;
        mModel = model;
    }

    public void cancelRequest(String key) {
        mModel.cancelRequest(key);
    }

    public void cancelRequest() {
        mModel.cancelAllRequest();
    }
}
