package com.yuri.cnbeta.news;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.xlog.Log;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Yuri on 2016/6/7.
 */
public class MainModel extends MainContract.Model {

    @Override
    public void getNews(NewsType newsType, String param, String lastSid, final HttpListResultListener listener) {
        Type type = new TypeToken<ApiResponse<List<Article>>>(){}.getType();
        String url;
        Log.d("newsType:" + newsType + ",param:" + param + "lastSid:" + lastSid);
        if (newsType == NewsType.LATEST) {
            if (TextUtils.isEmpty(lastSid)) {
                url = HttpConfigure.getLatestNews();
            } else {
                url = HttpConfigure.getMoreLatestNews(lastSid);
            }
        } else if (newsType == NewsType.MONTHLY) {
            if (TextUtils.isEmpty(lastSid)) {
                url = HttpConfigure.getMonthlyTopTen();
            } else {
                //get more
                return;
            }
        } else if (newsType == NewsType.DAILY) {
            if (TextUtils.isEmpty(lastSid)) {
                url = HttpConfigure.getDialyRank(param);
            } else {
                //get more
                return;
            }
        } else if (newsType == NewsType.TOPIC) {
            if (TextUtils.isEmpty(lastSid)) {
                url = HttpConfigure.getTopicNews(param);
            } else {
                url = HttpConfigure.getMoreTpicNews(lastSid, param);
            }
        } else {
            Log.e("type:" + newsType);
            return;
        }
        Request<ApiResponse> request = new JsonRequest(url, type);
        addRequest(url, request);
        request(request, new HttpListener<ApiResponse>() {
            @Override
            public void onSuccess(int what, Response<ApiResponse> response) {
                ApiResponse<List<Article>> apiResponse = response.get();
                Log.d("status:" + apiResponse.status);
                List<Article> articleList = apiResponse.result;
                if (listener != null) {
                    listener.onSuccess(articleList);
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
