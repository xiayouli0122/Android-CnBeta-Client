package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.model.CommentItem;
import com.yuri.cnbeta.model.INewsComment;
import com.yuri.cnbeta.model.impl.NewsCommentImpl;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.INewsCommentView;
import com.yuri.cnbeta.view.ui.NewsCommentActivity;
import com.yuri.cnbeta.view.ui.NewsDetailActivity;

import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public class NewsCommentPresenter extends BasePresenter<INewsCommentView>
        implements  IBaseNetPresenter<NewsDetailActivity>{

    private INewsComment mNewsComment;

    public NewsCommentPresenter(Context mContext, INewsCommentView mView) {
        super(mContext, mView);
        mNewsComment = new NewsCommentImpl();
    }

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
    public void cancelRequestBySign(Class<NewsDetailActivity> clazz) {
        mNewsComment.cancelRequestBySign(clazz);
    }
}
