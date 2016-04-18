package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.contract.NewsCommentContract;
import com.yuri.cnbeta.model.bean.CommentItem;
import com.yuri.cnbeta.model.NewsCommentModel;
import com.yuri.cnbeta.model.impl.NewsCommentImpl;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.ui.NewsCommentActivity;

import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public class NewsCommentPresenter extends BasePresenter<NewsCommentContract.View>
        implements NewsCommentContract.Presenter {

    private NewsCommentModel mNewsComment;

    public NewsCommentPresenter(Context mContext, NewsCommentContract.View mView) {
        super(mContext, mView);
        mNewsComment = new NewsCommentImpl();
    }

    @Override
    public void getNewsComment(int page, String sid) {
        mNewsComment.getNewsComment(mContext, page, sid, new HttpListResultListener<CommentItem>() {
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

    @Override
    public void cancelRequestBySign(Class<NewsCommentActivity> clazz) {
        mNewsComment.cancelRequestBySign(clazz);
    }
}
