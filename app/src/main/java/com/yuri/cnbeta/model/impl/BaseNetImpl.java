package com.yuri.cnbeta.model.impl;

import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.IBaseNetModel;

/**
 * Created by Yuri on 2016/4/13.
 */
public class BaseNetImpl implements IBaseNetModel {
    /**
     * 取消请求的实现操作
     * @param clazz 标识对象
     */
    @Override
    public void cancelRequestBySign(Class clazz) {
        Log.d("className:" + clazz.getName());
        CallServer.getInstance().cancelBySign(clazz);
    }
}