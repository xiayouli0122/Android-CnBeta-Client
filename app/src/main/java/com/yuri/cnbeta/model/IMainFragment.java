package com.yuri.cnbeta.model;

import android.content.Context;

import com.yuri.cnbeta.model.listener.HttpListResultListener;

/**
 * MainFragment业务逻辑(数据读写)的接口定义，实现交给了{@link com.yuri.cnbeta.model.impl.MainFragmentImpl}
 * Created by Yuri on 2016/4/13.
 */
public interface IMainFragment extends IBaseNetModel{
    void getData(Context context, HttpListResultListener listener);
}
