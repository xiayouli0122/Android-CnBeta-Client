package com.yuri.cnbeta.view.ui;

import android.content.Intent;
import android.provider.Settings;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yuri.cnbeta.BuildConfig;
import com.yuri.cnbeta.CnbetaService;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.HttpResponseListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.view.adapter.BaseViewHolder;
import com.yuri.cnbeta.view.ui.core.BaseListFragment;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Retrofit;

/**
 * Created by Yuri on 2016/4/7.
 */
public class MainFragment extends BaseListFragment<Article> {

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_list_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        Log.d("action:" + action);
        Type type = new TypeToken<ApiResponse<List<Article>>>(){}.getType();
        Request<ApiResponse> request2 = new JsonRequest(HttpConfigure.buildArtistUrl(), type);
        CallServer.getInstance().add(getActivity(), 0, request2, new HttpListener<ApiResponse>() {
            @Override
            public void onSuccess(int what, Response<ApiResponse> response) {
                ApiResponse<List<Article>> apiResponse = response.get();
                Log.d("status:" + apiResponse.status);
                List<Article> articleList = apiResponse.result;
                for (Article article : articleList) {
                    Log.d("" + article.getTitle());
                }
                mDataList = articleList;
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {

            }
        }, true);
    }


    class SampleViewHolder extends BaseViewHolder {

        @Bind(R.id.tv_news_title)
        TextView mTitleView;
        @Bind(R.id.tv_news_date)
        TextView mDateView;
        @Bind(R.id.iv_news_icon)
        ImageView mNewsIconView;
        @Bind(R.id.tv_news_summary)
        TextView mSummaryView;
        @Bind(R.id.tv_news_read_count)
        TextView mCounterView;//总共阅读量
        @Bind(R.id.tv_news_comment_count)
        TextView mCommentView;//评论数

        public SampleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            Article article = mDataList.get(position);
            mTitleView.setText(article.getTitle());
            mSummaryView.setText(article.getSummary());
            mDateView.setText(article.getPubtime());
            mCommentView.setText(article.getComments());
            mCounterView.setText(article.getCounter());
            Glide.with(getActivity())
                    .load(article.getTopicLogo())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(mNewsIconView);
        }

        @Override
        public void onItemClick(View view, int position) {
            Article article = mDataList.get(position);
            Log.d("" + article.getSid());

            Intent intent = NewsDetailActivity.getIntent(getActivity(), article.getSid());
            startActivity(intent);
        }

    }
}
