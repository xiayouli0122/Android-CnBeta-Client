package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.contract.HotCommentContract;
import com.yuri.cnbeta.model.HotCommentModel;
import com.yuri.cnbeta.model.impl.HotCommentImpl;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.ui.HotCommentsFragment;

import java.util.List;

/**
 * 新闻详情的Presenter
 * Created by Yuri on 2016/4/13.
 */
public class HotCommentPresenter extends BasePresenter<HotCommentContract.View>
        implements HotCommentContract.Presenter {

    private HotCommentModel mHotComment;

    public HotCommentPresenter(Context mContext, HotCommentContract.View mView) {
        super(mContext, mView);

        mHotComment = new HotCommentImpl();
    }

    @Override
    public void getHotComment() {
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
