package com.yuri.cnbeta.contract;

import com.yuri.cnbeta.model.bean.CommentItem;
import com.yuri.cnbeta.presenter.BaseNetPresenter;
import com.yuri.cnbeta.view.BaseView;
import com.yuri.cnbeta.view.ui.NewsCommentActivity;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface NewsCommentContract {

    interface View extends BaseView {
        void showData(List<CommentItem> commentItemList);
    }

    interface Presenter extends BaseNetPresenter<NewsCommentActivity> {
        void getNewsComment(int page, String sid);
    }

}
