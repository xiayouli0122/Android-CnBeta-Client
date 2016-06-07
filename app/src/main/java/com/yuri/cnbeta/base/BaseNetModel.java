package com.yuri.cnbeta.base;

import com.yolanda.nohttp.rest.Request;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.xlog.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Yuri on 2016/6/7.
 */

public class BaseNetModel {

    /**
     * 网络数据请求，肯定要用到CallServer
     */
    private CallServer mCallServer;

    /**
     * 当前模块，请求map集合，用于取消请求用
     * key:the request url
     * value:the request
     */
    private HashMap<String, Request> mRequestHashMap;

    public BaseNetModel() {
        mCallServer = CallServer.getInstance();
        mRequestHashMap = new HashMap<>();
    }

    protected void request(Request request, HttpListener listener) {
        request(0, request, listener);
    }

    protected void request(int what, Request request, HttpListener listener) {
        mCallServer.add(what, request, listener);
    }

    /**
     * 将请求添加到map中
     *
     * @param url     key，请求url
     * @param request value，请求request
     */
    protected void addRequest(String url, Request request) {
        if (mRequestHashMap != null) {
            mRequestHashMap.put(url, request);
        }
    }

    /**
     * 取消指定请求
     *
     * @param key 请求url
     */
    public void cancelRequest(String key) {
        if (mRequestHashMap == null) {
            return;
        }
        Log.net("key:" + key);
        Request request = mRequestHashMap.get(key);
        if (request != null) {
            request.cancel(true);
        }
    }

    /**
     * 取消当前已添加的所有请求
     */
    public void cancelAllRequest() {
        if (mRequestHashMap == null) {
            return;
        }
        Log.net();
        Iterator<Map.Entry<String, Request>> iterator = mRequestHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Request> entry = iterator.next();
            entry.getValue().cancel(true);
        }
        mRequestHashMap.clear();
        mRequestHashMap = null;
    }

}
