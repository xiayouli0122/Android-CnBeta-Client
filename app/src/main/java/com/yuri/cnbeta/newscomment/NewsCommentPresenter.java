package com.yuri.cnbeta.newscomment;

import android.graphics.Bitmap;

import com.yuri.cnbeta.http.response.HttpCommentItem;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.model.listener.HttpSimpleResultListener;

import java.util.List;

/**
 * Created by Yuri on 2016/6/12.
 */
public class NewsCommentPresenter extends NewsCommentContract.Presenter {

    public NewsCommentPresenter(NewsCommentContract.View view) {
        super(view);
    }

    @Override
    void getNewsComment(int page, String sid, String sn) {
        mModel.getNewsComment(page, sid, sn, new HttpListResultListener<HttpCommentItem>() {
            @Override
            public void onSuccess(List<HttpCommentItem> resultList) {
                mView.showData(resultList);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }

    @Override
    void getCodeImage() {
        mModel.getCodeImage(new HttpResultListener<Bitmap>() {
            @Override
            public void onSuccess(Bitmap result) {
                mView.onGetCodeImage(result);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }

    @Override
    void writeComment(String sid, String pid, String seccode, String content) {
        mModel.writeComment(sid, pid, seccode, content, new HttpSimpleResultListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(String errorMsg) {
                mView.showError(errorMsg);
            }
        });
    }
}
