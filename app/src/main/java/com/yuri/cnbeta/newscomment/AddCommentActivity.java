package com.yuri.cnbeta.newscomment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.base.BaseActivity;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.http.response.HttpCommentItem;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.utils.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Yuri on 2016/6/12.
 */
public class AddCommentActivity extends BaseActivity implements NewsCommentContract.View {

    @Bind(R.id.add_comment_edittext)
    EditText mAddEditText;

    @Bind(R.id.code_edittext)
    EditText mCodeEditText;
    @Bind(R.id.iv_code)
    ImageView mCodeImageView;
    @Bind(R.id.bar_code_image)
    ProgressBar mProgressBar;

    NewsCommentPresenter mPresenter;

    private static final String EXTRA_SID = "extra_sid";
    private static final String EXTRA_PID = "extra_pid";
    private String mSid;
    private String mPid;

    public static void start(Context context, String sid, String pid) {
        Intent intent = new Intent();
        intent.setClass(context, AddCommentActivity.class);
        intent.putExtra(EXTRA_SID, sid);
        intent.putExtra(EXTRA_PID, pid);
        context.startActivity(intent);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_add_comment);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpView() {
        mCodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mCodeImageView.setVisibility(View.GONE);
                mPresenter.getCodeImage();
            }
        });
    }

    @Override
    protected void setUpData() {
        Intent intent = getIntent();
        if (intent == null)
            return;

        mSid = intent.getStringExtra(EXTRA_SID);
        mPid = intent.getStringExtra(EXTRA_PID);

        mPresenter = new NewsCommentPresenter(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mCodeImageView.setVisibility(View.GONE);
        mPresenter.getCodeImage();
    }

    @OnClick(R.id.btn_send)
    void writeComment() {
        String comment = mAddEditText.getText().toString().trim();
        String code = mCodeEditText.getText().toString().trim();

        View requestView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(code)) {
            mCodeEditText.setError("请输入验证码");
            requestView = mCodeEditText;
            cancel = true;
        } else if (code.length() < 4){
            mCodeEditText.setError("验证码格式不正确");
            requestView = mCodeEditText;
            cancel = true;
        }

        if (comment.equals("")) {
            mAddEditText.setError("评论不能为空");
            requestView = mAddEditText;
            cancel = true;
        }

        if (cancel) {
            requestView.requestFocus();
        } else {
            mPresenter.writeComment(mSid, mPid, code, comment);
        }
    }

    @Override
    public void showData(List<HttpCommentItem> commentItemList) {

    }

    @Override
    public void onGetCodeImage(Bitmap bitmap) {
        mProgressBar.setVisibility(View.GONE);
        mCodeImageView.setVisibility(View.VISIBLE);
        if (bitmap != null) {
            mCodeImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void showError(String message) {
        ToastUtil.showToast(this, message);
    }
}
