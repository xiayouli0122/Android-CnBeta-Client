package com.yuri.cnbeta.model.impl;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.INewsDetail;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.view.ui.NewsDetailActivity;

import java.lang.reflect.Type;

/**
 * Created by Yuri on 2016/4/13.
 */
public class NewsDetailImpl extends BaseNetImpl implements INewsDetail {

    @Override
    public void getData(Context context, String sid, final HttpResultListener listener) {
        String contentUrl = HttpConfigure.newsContent(sid);
        Log.d("contentUrl:" + contentUrl);

        Type type = new TypeToken<ApiResponse<Content>>() {}.getType();
        Request<ApiResponse> request = new JsonRequest(contentUrl, type);
        request.setCancelSign(NewsDetailActivity.class);

        CallServer.getInstance().add(context, 1, request, new HttpListener<ApiResponse>() {
            @Override
            public void onSuccess(int what, Response<ApiResponse> response) {
                ApiResponse<Content> apiResponse = response.get();
                Content content = apiResponse.result;
                if (listener != null) {
                    listener.onSuccess(content);
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception,
                                 int responseCode, long networkMills) {
                if (listener != null) {
                    listener.onFail(exception.getMessage());
                }
            }
        }, true);
    }
}
