package com.yuri.cnbeta.http;

import android.content.Context;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by Yuri on 2016/4/8.
 */
public class CallServer {

    private static CallServer mInstance;

    /**
     * 请求队列
     */
    private RequestQueue mRequestQueue;

    private CallServer() {
        //使用默认配置创建一个请求队列，默认队列支持3个请求
        this.mRequestQueue = NoHttp.newRequestQueue();
    }

    public static CallServer getInstance() {
        if (mInstance == null) {
            synchronized (CallServer.class) {
                if (mInstance == null) {
                    mInstance = new CallServer();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param what      用来标志请求, 当多个请求使用同一个{@link HttpListener}时, 在回调方法中会返回这个what.
     * @param request   请求对象.
     * @param callback  结果回调对象.
     */
    public <T> void add(int what, Request<T> request, HttpListener<T> callback) {
        mRequestQueue.add(what, request, new HttpResponseListener<>(callback));
    }

    /**
     * 取消这个sign标记的所有请求.
     */
    public void cancelBySign(Object sign) {
        mRequestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求.
     */
    public void cancelAll() {
        mRequestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求.
     */
    public void stopAll() {
        mRequestQueue.stop();
    }

}
