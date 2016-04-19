package com.yuri.cnbeta.model;

import android.content.Context;
import android.os.Bundle;

import com.yuri.cnbeta.model.bean.NewsType;
import com.yuri.cnbeta.model.listener.HttpListResultListener;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface ArticleModel extends BaseNetModel{
    void setBundle(Bundle bundle);
    void setNewsType(NewsType newsType);
    void getData(Context context, HttpListResultListener listener);
    void getMoreData(Context context, String lastSid, HttpListResultListener listener);
}
