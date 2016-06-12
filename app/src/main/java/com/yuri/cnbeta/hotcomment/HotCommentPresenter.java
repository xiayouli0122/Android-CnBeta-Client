package com.yuri.cnbeta.hotcomment;

import com.yuri.cnbeta.http.response.HotComment;
import com.yuri.cnbeta.model.listener.HttpListResultListener;

import java.util.List;

/**
 * 新闻详情的Presenter
 * Created by Yuri on 2016/4/13.
 */
public class HotCommentPresenter extends HotCommentContract.Presenter {

    public HotCommentPresenter(HotCommentContract.View mView) {
        super(mView);
    }

    @Override
    public void getHotComment() {
        mModel.getHotComment(new HttpListResultListener<HotComment>() {
            @Override
            public void onSuccess(List<HotComment> resultList) {
                mView.showData(resultList);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }

}
