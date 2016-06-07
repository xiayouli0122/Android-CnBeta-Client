package com.yuri.cnbeta.http;

import android.content.Context;

import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.utils.ToastUtil;
import com.yuri.xlog.Log;

import java.net.ProtocolException;

/**
 * Created by Yuri on 2016/4/8.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    /**
     * 结果回调
     */
    private HttpListener<T> mCallBack;

    public HttpResponseListener(HttpListener<T> httpListener) {
        this.mCallBack = httpListener;
    }

    @Override
    public void onStart(int what) {
        Log.d("what:" + what);
    }

    @Override
    public void onSucceed(int what, Response<T> response) {
        Log.d("what:" + what);
        if (mCallBack != null) {
            mCallBack.onSuccess(what, response);
        }
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        if (exception instanceof NetworkError) {// 网络不好
//            ToastUtil.showToast(mContext, "请检查网络。");
        } else if (exception instanceof TimeoutError) {// 请求超时
//            ToastUtil.showToast(mContext, "请求超时，网络不好或者服务器不稳定。");
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
//            ToastUtil.showToast(mContext, "未发现指定服务器。");
        } else if (exception instanceof URLError) {// URL是错的
//            ToastUtil.showToast(mContext, "URL错误。");
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
//            ToastUtil.showToast(mContext, "没有发现缓存。");
        } else if (exception instanceof ProtocolException) {
//            ToastUtil.showToast(mContext, "系统不支持的请求方式。");
        } else {
//            ToastUtil.showToast(mContext, "未知错误。");
        }
        Log.e("错误：" + exception.getMessage());
        if (mCallBack != null)
            mCallBack.onFailed(what, exception.getMessage());

    }

    @Override
    public void onFinish(int what) {
        Log.d("what:" + what);
    }
}
