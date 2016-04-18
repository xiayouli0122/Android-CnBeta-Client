package com.yuri.cnbeta.model.impl;

import android.content.Context;

import com.activeandroid.query.Select;
import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yuri.cnbeta.db.NewsItem;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.http.response.HotComment;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.IFavorite;
import com.yuri.cnbeta.model.IHotComment;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.ui.HotCommentsFragment;
import com.yuri.cnbeta.view.ui.MainFragment;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Yuri on 2016/4/14.
 */
public class HotCommentImpl extends BaseNetImpl implements IHotComment {

    @Override
    public void getHotComments(Context context, final HttpListResultListener listener) {
        Type type = new TypeToken<ApiResponse<List<HotComment>>>(){}.getType();
        Request<ApiResponse> request = new JsonRequest(HttpConfigure.hotComments(), type);
        request.setCancelSign(HotCommentsFragment.class);

        CallServer.getInstance().add(context, 0, request, new HttpListener<ApiResponse>() {
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
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {
                if (listener != null) {
                    listener.onFail(exception.getMessage());
                }
            }
        }, true);
    }
}
