package com.yuri.cnbeta.newscomment;

import com.yuri.cnbeta.model.bean.CommentItem;
import com.yuri.cnbeta.model.listener.HttpListResultListener;

import java.util.List;

/**
 * Created by Yuri on 2016/6/12.
 */
public class NewsCommentPresenter extends NewsCommentContract.Presenter {

    public NewsCommentPresenter(NewsCommentContract.View view) {
        super(view);
    }

    @Override
    void getNewsComment(int page, String sid) {
        mModel.getNewsComment(page, sid, new HttpListResultListener<CommentItem>() {
            @Override
            public void onSuccess(List<CommentItem> resultList) {
                mView.showData(resultList);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }
}
