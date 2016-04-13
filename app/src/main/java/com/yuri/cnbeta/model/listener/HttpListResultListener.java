package com.yuri.cnbeta.model.listener;

import com.yuri.cnbeta.http.response.Article;

import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface HttpListResultListener<T> {

    void onSuccess(List<T> resultList);
    void onFail(String message);
}
