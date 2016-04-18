package com.yuri.cnbeta.model;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface BaseNetModel extends BaseModel {
    /**
     * 取消当前请求通过标识
     * @param clazz 标识对象
     */
    void cancelRequestBySign(Class clazz);
}
