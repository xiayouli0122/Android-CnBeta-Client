package com.yuri.cnbeta.model.listener;

import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface HttpResultListener<T> {

    void onSuccess(T result);
    void onFail(String message);
}
