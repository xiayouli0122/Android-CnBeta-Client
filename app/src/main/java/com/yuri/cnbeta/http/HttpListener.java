package com.yuri.cnbeta.http;

import com.yolanda.nohttp.rest.Response;

/**
 * 接收回调结果
 * Created by Yuri on 2016/4/8.
 */
public interface HttpListener<T> {
    void onSuccess(int what, Response<T> response);
    void onFailed(int what, String errorMsg);
}
