package com.yuri.cnbeta.model;

import android.content.Context;

import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.model.listener.HttpResultListener;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface INewsDetail extends IBaseNetModel{
    void getData(Context context, String sid, HttpResultListener listener);
}
