package com.yuri.cnbeta.base;

/**
 * Created by Yuri on 2016/4/13.
 */
public class BasePresenter<T, E> {

    protected T mView;
    protected E mModel;

    public BasePresenter(T view, E model) {
        mView = view;
        mModel = model;
    }
}
