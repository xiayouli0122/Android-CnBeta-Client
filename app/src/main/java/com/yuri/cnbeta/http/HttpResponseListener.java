package com.yuri.cnbeta.http;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yolanda.nohttp.error.ClientError;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.utils.ToastUtil;

/**
 * Created by Yuri on 2016/4/8.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private Context mContext;

    private Request<?> mRequest;

    /**
     * 结果回调
     */
    private HttpListener<T> mCallBack;

    public HttpResponseListener(Context context, Request<?> mRequest, HttpListener<T> httpListener, boolean canCancel) {
        this.mContext = context;
        this.mRequest = mRequest;
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

        if (exception instanceof ClientError) {// 客户端错误
            if (responseCode == 400) {//服务器未能理解请求。
                ToastUtil.showToast(mContext, "错误的请求，服务器表示不能理解。");
            } else if (responseCode == 403) {// 请求的页面被禁止
                ToastUtil.showToast(mContext, "错误的请求，服务器表示不愿意。");
            } else if (responseCode == 404) {// 服务器无法找到请求的页面
                ToastUtil.showToast(mContext, "错误的请求，服务器表示找不到。");
            } else {// 400-417都是客户端错误，开发者可以自己去查询噢
                ToastUtil.showToast(mContext, "错误的请求，服务器表示伤不起。");
            }
        } else if (exception instanceof ServerError) {// 服务器错误
            if (500 == responseCode) {
                ToastUtil.showToast(mContext, "服务器遇到不可预知的情况。");
            } else if (501 == responseCode) {
                ToastUtil.showToast(mContext, "服务器不支持的请求。");
            } else if (502 == responseCode) {
                ToastUtil.showToast(mContext, "服务器从上游服务器收到一个无效的响应。");
            } else if (503 == responseCode) {
                ToastUtil.showToast(mContext, "服务器临时过载或当机。");
            } else if (504 == responseCode) {
                ToastUtil.showToast(mContext, "网关超时。");
            } else if (505 == responseCode) {
                ToastUtil.showToast(mContext, "服务器不支持请求中指明的HTTP协议版本。");
            } else {
                ToastUtil.showToast(mContext, "服务器脑子有问题。");
            }
        } else if (exception instanceof NetworkError) {// 网络不好
            ToastUtil.showToast(mContext, "请检查网络。");
        } else if (exception instanceof TimeoutError) {// 请求超时
            ToastUtil.showToast(mContext, "请求超时，网络不好或者服务器不稳定。");
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            ToastUtil.showToast(mContext, "未发现指定服务器。");
        } else if (exception instanceof URLError) {// URL是错的
            ToastUtil.showToast(mContext, "URL错误。");
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            ToastUtil.showToast(mContext, "没有发现缓存。");
        } else {
            ToastUtil.showToast(mContext, "未知错误。");
        }
        Logger.e("错误：" + exception.getMessage());
        if (mCallBack != null)
            mCallBack.onFailed(what, url, tag, exception, responseCode, networkMillis);

    }

    @Override
    public void onFinish(int what) {
        Log.d("what:" + what);
    }
}
