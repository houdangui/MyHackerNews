package com.hackernews.dangui.myhackernews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.model.Comment;
import com.hackernews.dangui.myhackernews.model.ItemFetchStatus;
import com.hackernews.dangui.myhackernews.model.Story;
import com.hackernews.dangui.myhackernews.util.CommentsListListener;
import com.hackernews.dangui.myhackernews.util.FetchCommentDetailListener;
import com.hackernews.dangui.myhackernews.util.FetchStoryDetailListener;
import com.hackernews.dangui.myhackernews.util.HackerNewsApi;

import java.util.ArrayList;

/**
 * Created by dangui on 2/3/17.
 */

public class CommentsActivity extends AppCompatActivity implements CommentsListListener {
    private ArrayList<Comment> mComments;
    private RecyclerView mCommentList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        Intent intent = getIntent();
        mComments = new ArrayList<>();
        Long[] kids = (Long[]) intent.getSerializableExtra("kids");
        if (kids != null) {
            for (Long kid : kids) {
                Comment comment = new Comment(kid);
                mComments.add(comment);
            }
        }

        mCommentList = (RecyclerView) findViewById(R.id.comment_list);
        mLayoutManager = new LinearLayoutManager(this);
        mCommentList.setLayoutManager(mLayoutManager);
        mAdapter = new CommentListAdapter(mComments, this);
        mCommentList.setAdapter(mAdapter);
    }

    @Override
    public void onEmptyCommentShown(Comment comment) {
        if (comment.getStatus() == ItemFetchStatus.NEVER_FETCHED) {
            HackerNewsApi.getInstance().fetchCommentDetail(this, comment, new FetchCommentDetailListener() {
                @Override
                public void onActionSuccess(Comment comment) {
                    refreshListDelay();
                }

                @Override
                public void onActionFail(String errorMessage) {
                    Toast.makeText(CommentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean isToRefresh = false;

    private void refreshListDelay() {
        if (isToRefresh) {
            return;
        }
        isToRefresh = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                isToRefresh = false;
            }
        }, 300);
    }
}
