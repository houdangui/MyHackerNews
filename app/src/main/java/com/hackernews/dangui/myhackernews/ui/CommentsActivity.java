package com.hackernews.dangui.myhackernews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.hackernews.dangui.myhackernews.R;
import com.hackernews.dangui.myhackernews.model.Comment;
import com.hackernews.dangui.myhackernews.util.CommentsListListener;

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

        mCommentList = (RecyclerView) findViewById(R.id.comment_list);
        mLayoutManager = new LinearLayoutManager(this);
        mCommentList.setLayoutManager(mLayoutManager);
        mComments = new ArrayList<>();
        mAdapter = new CommentListAdapter(mComments, this);
        mCommentList.setAdapter(mAdapter);
    }

    @Override
    public void onEmptyCommentShown(Comment comment) {

    }
}
