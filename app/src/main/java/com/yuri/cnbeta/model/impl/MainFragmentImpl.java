package com.yuri.cnbeta.model.impl;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.MainFragmentModel;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.ui.MainFragment;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 数据操作的实现类
 * Created by Yuri on 2016/4/13.
 */
public class MainFragmentImpl extends BaseNetImpl implements MainFragmentModel {

    /**
     * 实现获取新闻列表数据的操作
     * @param context context对象
     * @param listener 结果回调
     */
    @Override
    public void getData(Context context, final HttpListResultListener listener) {
        getNews(context, null, listener);
    }

    @Override
    public void getMoreData(Context context, String lastSid, HttpListResultListener listener) {
        getNews(context, lastSid, listener);
    }

    private void getNews(Context context, String lastSid, final HttpListResultListener listener) {
        Type type = new TypeToken<ApiResponse<List<Article>>>(){}.getType();
        String url;
        if (TextUtils.isEmpty(lastSid)) {
            url = HttpConfigure.getLatestNews();
        } else {
            url = HttpConfigure.getMoreLatestNews(lastSid);
        }
        Request<ApiResponse> request = new JsonRequest(url, type);
        request.setCancelSign(MainFragment.class);
        CallServer.getInstance().add(context, 0, request, new HttpListener<ApiResponse>() {
            @Override
            public void onSuccess(int what, Response<ApiResponse> response) {
                ApiResponse<List<Article>> apiResponse = response.get();
                Log.d("status:" + apiResponse.status);
                List<Article> articleList = apiResponse.result;
//                for (Article article : articleList) {
//                    Log.d(article.getTitle());
//                }
                if (listener != null) {
                    listener.onSuccess(articleList);
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
