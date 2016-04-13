package com.yuri.cnbeta.view;

import com.yuri.cnbeta.http.response.Article;

import java.util.List;

/**
 * 定义MainFragment需要操作view的方法，
 * Created by Yuri on 2016/4/13.
 */
public interface IMainFragmentView extends IBaseView{
    /**
     * 获取到新闻数据的时候，将之显示到UI上
     * @param articleList 数据列表
     */
    void showData(List<Article> articleList);
}
