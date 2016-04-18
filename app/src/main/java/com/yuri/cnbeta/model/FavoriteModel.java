package com.yuri.cnbeta.model;

import com.yuri.cnbeta.model.listener.HttpListResultListener;

/**
 * Created by Yuri on 2016/4/14.
 */
public interface FavoriteModel extends BaseModel {

    void getFavoriteData(HttpListResultListener listResultListener);
}
