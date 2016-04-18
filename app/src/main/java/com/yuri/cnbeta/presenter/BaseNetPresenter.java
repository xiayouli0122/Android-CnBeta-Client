package com.yuri.cnbeta.presenter;

/**
 * 网络数据操作相关的基类
 * <p>意思网络相关都会有获取数据和取消请求这些方法</p>
 * <p>所以之后如果涉及到网络请求相关的界面，presenter可以直接implements这个接口</p>
 * 这种设计模式好像就是叫工厂模式
 * Created by Yuri on 2016/4/13.
 */
public interface BaseNetPresenter<T> extends BasePresenterInterface{
    void cancelRequestBySign(Class<T> clazz);
}
