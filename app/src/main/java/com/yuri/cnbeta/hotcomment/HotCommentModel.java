package com.yuri.cnbeta.hotcomment;

import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.HotComment;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.xlog.Log;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class HotCommentModel extends HotCommentContract.Model {

    @Override
    void getHotComment(final HttpListResultListener<HotComment> listener) {
        String url = HttpConfigure.hotComments();
        Type type = new TypeToken<ApiResponse<List<HotComment>>>(){}.getType();
        Request<ApiResponse> request = new JsonRequest(url, type);
        addRequest(url, request);

        request(request, new HttpListener<ApiResponse>() {
            @Override
            public void onSuccess(int what, Response<ApiResponse> response) {
                ApiResponse<List<HotComment>> apiResponse = response.get();
                Log.d("status:" + apiResponse.status);
                List<HotComment> hotCommentList = apiResponse.result;
                if (listener != null) {
                    listener.onSuccess(hotCommentList);
                }
            }

            @Override
            public void onFailed(int what, String errorMsg) {
                if (listener != null) {
                    listener.onFail(errorMsg);
                }
            }
        });
    }
}
