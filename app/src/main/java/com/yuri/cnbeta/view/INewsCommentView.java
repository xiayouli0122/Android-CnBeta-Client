package com.yuri.cnbeta.view;

import com.yuri.cnbeta.model.CommentItem;

import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public interface INewsCommentView extends IBaseView{
    void showData(List<CommentItem> commentItemList);
}
