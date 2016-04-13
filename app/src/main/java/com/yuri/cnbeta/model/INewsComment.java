package com.yuri.cnbeta.model;

import android.content.Context;

import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.model.listener.HttpResultListener;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface INewsComment extends IBaseNetModel {

    /**
     * 获取新闻评论
     * @param context context
     * @param page 页数
     * @param sid 新闻的sid
     * @param listener 结果回调
     */
    void getNewsComment(Context context, int page, String sid, HttpListResultListener listener);
}
