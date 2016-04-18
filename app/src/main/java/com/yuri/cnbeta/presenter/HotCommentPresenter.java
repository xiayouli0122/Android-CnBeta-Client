package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.model.IHotComment;
import com.yuri.cnbeta.model.impl.HotCommentImpl;
import com.yuri.cnbeta.model.impl.NewsDetailImpl;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.view.IHotCommetView;
import com.yuri.cnbeta.view.ui.HotCommentsFragment;

import java.util.List;

/**
 * 新闻详情的Presenter
 * Created by Yuri on 2016/4/13.
 */
public class HotCommentPresenter extends BasePresenter<IHotCommetView>
        implements IBaseNetPresenter<HotCommentsFragment>{

    private IHotComment mHotComment;

    public HotCommentPresenter(Context mContext, IHotCommetView mView) {
        super(mContext, mView);

        mHotComment = new HotCommentImpl();
    }

    public void getData() {
        mHotComment.getHotComments(mContext, new HttpListResultListener() {
            @Override
            public void onSuccess(List resultList) {
                mView.showData(resultList);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }

    @Override
    public void cancelRequestBySign(Class<HotCommentsFragment> clazz) {
        mHotComment.cancelRequestBySign(clazz);
    }
}
