package com.yuri.cnbeta.newscomment;

import com.yuri.cnbeta.base.BaseNetModel;
import com.yuri.cnbeta.base.BaseNetPresenter;
import com.yuri.cnbeta.model.bean.CommentItem;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.BaseView;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface NewsCommentContract {

    interface View extends BaseView {
        void showData(List<CommentItem> commentItemList);
    }

    abstract class Model extends BaseNetModel {
        abstract void getNewsComment(int page, String sid,  HttpListResultListener<CommentItem> listener);
    }

    abstract class Presenter extends BaseNetPresenter<View, Model> {

        public Presenter(View view) {
            super(view, new NewsCommentModel());
        }

        abstract void getNewsComment(int page, String sid);
    }

}
