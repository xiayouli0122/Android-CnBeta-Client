package com.yuri.cnbeta.model;

import android.content.Context;

import com.yuri.cnbeta.model.listener.HttpListResultListener;

/**
 * Created by Yuri on 2016/4/14.
 */
public interface HotCommentModel extends BaseNetModel {

    void getHotComments(Context context, HttpListResultListener listener);
}
