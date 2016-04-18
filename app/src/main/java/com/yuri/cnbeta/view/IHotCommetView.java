package com.yuri.cnbeta.view;

import com.yuri.cnbeta.db.NewsItem;
import com.yuri.cnbeta.http.response.HotComment;

import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface IHotCommetView extends IBaseView {

    void showData(List<HotComment> hotCommentList);
}
