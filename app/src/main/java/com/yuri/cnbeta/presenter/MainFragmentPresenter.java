package com.yuri.cnbeta.presenter;

import android.content.Context;

import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.model.IMainFragment;
import com.yuri.cnbeta.model.impl.MainFragmentImpl;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.view.IMainFragmentView;
import com.yuri.cnbeta.view.ui.MainFragment;

import java.util.List;

/**
 * 主导类，通过IMainFragment和IMainFragmentView操作数据和操作UI显示
 * Created by Yuri on 2016/4/13.
 */
public class MainFragmentPresenter extends BasePresenter<IMainFragmentView>
        implements IBaseNetPresenter<MainFragment>{

    private IMainFragment mMainFragment;

    public MainFragmentPresenter(Context mContext, IMainFragmentView mView) {
        super(mContext, mView);

        mMainFragment = new MainFragmentImpl();
    }

    /**
     * 暴露方法给Activity，用于去获取数据，并将结果回调给Activity
     */
    public void getData() {
        mMainFragment.getData(mContext, new HttpListResultListener<Article>() {
            @Override
            public void onSuccess(List<Article> resultList) {
                mView.showData(resultList);
            }

            @Override
            public void onFail(String message) {
                mView.showError(message);
            }
        });
    }

    @Override
    public void cancelRequestBySign(Class<MainFragment> clazz) {
        mMainFragment.cancelRequestBySign(clazz);
    }
}
