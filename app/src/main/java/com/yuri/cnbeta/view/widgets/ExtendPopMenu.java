package com.yuri.cnbeta.view.widgets;

import android.app.Activity;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.model.CommentItem;
import com.yuri.cnbeta.view.adapter.BaseListAdapter;

import org.json.JSONObject;

public class ExtendPopMenu extends PopupMenu {
    public int SUPPORT = 1;
    public int AGAINST = 2;
    public int REPORT = 3;
    private int action;
    private CommentItem commentItem;
    private Context mContext;
    private BaseListAdapter adapter;
    private String token;

    public ExtendPopMenu(Context context, View anchor) {
        super(context, anchor);
        this.mContext = context;
        inflate(R.menu.menu_comment);
        setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.comment_support:
                        action = SUPPORT;
//                        NetKit.getInstance().setCommentAction("support", commentItem.getSid()+"", commentItem.getTid(), token, chandler);
                        break;
                    case R.id.comment_against:
                        action = AGAINST;
//                        NetKit.getInstance().setCommentAction("against", commentItem.getSid()+"", commentItem.getTid(), token, chandler);
                        break;
                    case R.id.comment_report:
                        action = REPORT;
//                        NetKit.getInstance().setCommentAction("report", commentItem.getSid()+"", commentItem.getTid(), token, chandler);
                        break;
                    case R.id.comment_replay:
//                        if(mContext instanceof Activity) {
//                            AddNewCommentFragment fragment = AddNewCommentFragment.getInstance(commentItem.getSid(), commentItem.getTid(), token);
//                            fragment.show(((Activity) mContext).getFragmentManager(), "new comment");
//                        }else{
//                            Toast.makeText(mContext,"function not impletment", Toast.LENGTH_SHORT).show();
//                        }
                        break;
                }
                return true;
            }
        });

    }

//    private JsonHttpResponseHandler chandler = new JsonHttpResponseHandler() {
//        @Override
//        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//            Toast.makeText(mContext, "操作失败", Toast.LENGTH_LONG).show();
//            throwable.printStackTrace();
//        }
//
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            try {
//                if ("success".equals(response.getString("state"))) {
//                    String actionString;
//                    if (action == SUPPORT) {
//                        actionString = "支持";
//                        commentItem.setScore(commentItem.getScore() + 1);
//                    } else if (action == AGAINST) {
//                        actionString = "反对";
//                        commentItem.setReason(commentItem.getReason() + 1);
//                    } else {
//                        actionString = "举报";
//                    }
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(mContext, actionString + "成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    throw new Exception();
//                }
//            } catch (Exception e) {
//                onFailure(statusCode, headers, e, response);
//            }
//        }
//    };

    public void setCommentItem(CommentItem commentItem) {
        this.commentItem = commentItem;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
    }
}