package com.yuri.cnbeta.base;

/**
 * view 接口类的基类
 * Created by Yuri on 2016/4/13.
 */
public interface BaseView {

    /**
     * 当数据操作失败的时候
     * @param message 失败信息
     */
    void showError(String message);
}
