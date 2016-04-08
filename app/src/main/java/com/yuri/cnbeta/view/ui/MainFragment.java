package com.yuri.cnbeta.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.view.adapter.BaseViewHolder;
import com.yuri.cnbeta.view.ui.core.BaseListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yuri on 2016/4/7.
 */
public class MainFragment extends BaseListFragment {

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_list_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onRefresh(int action) {

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

        public SampleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
//            mSampleListItemLabel.setVisibility(View.GONE);
//            Glide.with(mSampleListItemImg.getContext())
//                    .load(mDataList.get(position))
//                    .centerCrop()
//                    .placeholder(R.color.app_primary_color)
//                    .crossFade()
//                    .into(mSampleListItemImg);
        }

        @Override
        public void onItemClick(View view, int position) {

        }

    }
}
