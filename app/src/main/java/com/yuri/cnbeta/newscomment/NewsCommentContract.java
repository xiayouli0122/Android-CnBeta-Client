package com.yuri.cnbeta.newscomment;

import android.graphics.Bitmap;

import com.yuri.cnbeta.base.BaseNetModel;
import com.yuri.cnbeta.base.BaseNetPresenter;
import com.yuri.cnbeta.http.response.HttpCommentItem;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.base.BaseView;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.model.listener.HttpSimpleResultListener;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface NewsCommentContract {

    interface View extends BaseView {
        void showData(List<HttpCommentItem> commentItemList);
        void onGetCodeImage(Bitmap bitmap);
    }

    abstract class Model extends BaseNetModel {
        abstract void getNewsComment(int page, String sid, String sn, HttpListResultListener<HttpCommentItem> listener);
        abstract void getCodeImage(HttpResultListener<Bitmap> listener);
        abstract void writeComment(String sid, String pid, String seccode, String content,
                                   HttpSimpleResultListener listener);
    }

    abstract class Presenter extends BaseNetPresenter<View, Model> {

        public Presenter(View view) {
            super(view, new NewsCommentModel());
        }

        abstract void getNewsComment(int page, String sid, String sn);
        abstract void getCodeImage();
        abstract void writeComment(String sid, String pid, String seccode, String content);
    }

}
