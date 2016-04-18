package com.yuri.cnbeta.contract;

import com.yuri.cnbeta.http.response.HotComment;
import com.yuri.cnbeta.presenter.BaseNetPresenter;
import com.yuri.cnbeta.view.BaseView;
import com.yuri.cnbeta.view.ui.HotCommentsFragment;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface HotCommentContract {

    interface View extends BaseView {
        void showData(List<HotComment> hotCommentList);
    }

    interface Presenter extends BaseNetPresenter<HotCommentsFragment> {
        void getHotComment();
    }
}
