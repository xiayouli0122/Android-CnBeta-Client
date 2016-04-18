package com.yuri.cnbeta.model;

import android.content.Context;

import com.yuri.cnbeta.model.listener.HttpListResultListener;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface NewsCommentModel extends BaseNetModel {

    /**
     * 获取新闻评论
     * @param context context
     * @param page 页数
     * @param sid 新闻的sid
     * @param listener 结果回调
     */
    void getNewsComment(Context context, int page, String sid, HttpListResultListener listener);
}
