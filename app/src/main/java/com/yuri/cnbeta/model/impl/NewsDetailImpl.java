package com.yuri.cnbeta.model.impl;

import android.content.Context;

import com.activeandroid.Model;
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

    @Override
    public boolean isFavorited(String sid) {
        NewsItem newsItem = new Select().from(NewsItem.class).where("sid=?", sid).executeSingle();
        return newsItem != null;
    }

    @Override
    public boolean doFavorite(Content content, String topicLogo) {
        if (isFavorited(content.sid)) {
            return true;
        }
        NewsItem newsItem = new NewsItem();
        newsItem.sid = content.sid;
        newsItem.aid = content.aid;
        newsItem.bodytext = content.bodytext;
        newsItem.comments = content.comments;
        newsItem.counter = content.counter;
        newsItem.hometext = content.hometext;
        newsItem.source = content.source;
        newsItem.title = content.title;
        newsItem.topic = content.topic;
        newsItem.time = content.time;
        newsItem.topicLogo = topicLogo;
        if (newsItem.save() == -1) {
            //insert error
            return false;
        }
        return true;
    }
}
