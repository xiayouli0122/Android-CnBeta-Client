package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.view.IBaseView;
import com.yuri.cnbeta.view.ui.MainFragment;

/**
 * Created by Yuri on 2016/4/13.
 */
public class BasePresenter<T extends IBaseView> {

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
