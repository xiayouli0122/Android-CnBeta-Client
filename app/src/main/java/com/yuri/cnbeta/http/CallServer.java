package com.yuri.cnbeta.http;

import android.content.Context;
import android.telecom.Call;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestQueue;

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

//    public void add(Context context, int what, Request request,  )

    /**
     * 添加一个请求到请求队列.
     *
     * @param context   context用来实例化dialog.
     * @param what      用来标志请求, 当多个请求使用同一个{@link HttpListener}时, 在回调方法中会返回这个what.
     * @param request   请求对象.
     * @param callback  结果回调对象.
     * @param canCancel 是否允许用户取消请求.
     */
    public <T> void add(Context context, int what, Request<T> request, HttpListener<T> callback, boolean canCancel) {
        mRequestQueue.add(what, request, new HttpResponseListener<T>(context, request, callback, canCancel));
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
