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
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.HotCommentModel;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.ui.HotCommentsFragment;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class HotCommentImpl extends BaseNetImpl implements HotCommentModel {

    @Override
    public void getHotComments(Context context, final HttpListResultListener listener) {
        Type type = new TypeToken<ApiResponse<List<com.yuri.cnbeta.http.response.HotComment>>>(){}.getType();
        Request<ApiResponse> request = new JsonRequest(HttpConfigure.hotComments(), type);
        request.setCancelSign(HotCommentsFragment.class);

        CallServer.getInstance().add(context, 0, request, new HttpListener<ApiResponse>() {
            @Override
            public void onSuccess(int what, Response<ApiResponse> response) {
                ApiResponse<List<com.yuri.cnbeta.http.response.HotComment>> apiResponse = response.get();
                Log.d("status:" + apiResponse.status);
                List<com.yuri.cnbeta.http.response.HotComment> hotCommentList = apiResponse.result;
                if (listener != null) {
                    listener.onSuccess(hotCommentList);
                }
            }
            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {
                if (listener != null) {
                    listener.onFail(exception.getMessage());
                }
            }
        }, true);
    }
}
