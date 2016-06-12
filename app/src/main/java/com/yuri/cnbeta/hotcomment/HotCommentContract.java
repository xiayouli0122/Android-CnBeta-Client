package com.yuri.cnbeta.hotcomment;

import com.yuri.cnbeta.base.BaseNetModel;
import com.yuri.cnbeta.base.BaseNetPresenter;
import com.yuri.cnbeta.http.response.HotComment;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.base.BaseView;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface HotCommentContract {

    interface View extends BaseView {
        void showData(List<HotComment> hotCommentList);
    }

    abstract class Model extends BaseNetModel {
        abstract void getHotComment(HttpListResultListener<HotComment> listener);
    }

    abstract class Presenter extends BaseNetPresenter<View, Model> {

        public Presenter(View view) {
            super(view, new HotCommentModel());
        }

        abstract void getHotComment();
    }
}
