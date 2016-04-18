package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.view.BaseView;

/**
 * Created by Yuri on 2016/4/13.
 */
public class BasePresenter<T extends BaseView> {

    protected T mView;
    protected Context mContext;

    public BasePresenter(Context mContext) {
        this.mContext = mContext;
    }

    public BasePresenter(Context mContext, T mView) {
        this.mView = mView;
        this.mContext = mContext;
    }
}
