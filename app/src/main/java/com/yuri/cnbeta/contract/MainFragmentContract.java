package com.yuri.cnbeta.contract;

import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.presenter.BaseNetPresenter;
import com.yuri.cnbeta.view.BaseView;
import com.yuri.cnbeta.view.ui.MainFragment;

import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public interface MainFragmentContract {

    interface View extends BaseView {
        /**
         * 获取到新闻数据的时候，将之显示到UI上
         * @param articleList 数据列表
         */
        void showData(boolean isMore, List<Article> articleList);
    }


    interface Presenter extends BaseNetPresenter<MainFragment> {
        void getData();
        void getMoreData(String lastSid);
    }
}

