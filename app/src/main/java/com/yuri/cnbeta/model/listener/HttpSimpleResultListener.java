package com.yuri.cnbeta.model.listener;

/**
 * Created by Yuri on 2016/6/12.
 */
public interface HttpSimpleResultListener {
    void onSuccess();
    void onFail(String errorMsg);
}
