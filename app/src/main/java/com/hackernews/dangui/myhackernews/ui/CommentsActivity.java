package com.hackernews.dangui.myhackernews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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

        Story story = getStory();
        mCommentList = (RecyclerView) findViewById(R.id.comment_list);
        mLayoutManager = new LinearLayoutManager(this);
        mCommentList.setLayoutManager(mLayoutManager);
        mAdapter = new CommentListAdapter(this, mComments, story, this);
        mCommentList.setAdapter(mAdapter);

        getSupportActionBar().setTitle(getTitle(story));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getTitle(Story story) {
        String type  = story.getType();
        if (type == null) {
            return "";
        }
        if (type.equals("story")) {
            return getString(R.string.story);
        } else if (type.equals("job")) {
            return getString(R.string.job);
        } else if (type.equals("poll")) {
            return getString(R.string.poll);
        } else {
            return "";
        }

    }
    private Story getStory() {
        Intent intent = getIntent();
        Long storyId = intent.getLongExtra("storyId", 0L);
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        Long ts = intent.getLongExtra("timestamp", 0L);
        String by = intent.getStringExtra("by");
        int descendants = intent.getIntExtra("descendants", 0);
        String type = intent.getStringExtra("type");

        Story story = new Story(storyId);
        story.setUrl(url);
        story.setTitle(title);
        story.setTime(ts);
        story.setBy(by);
        story.setDescendants(descendants);
        story.setType(type);

        mComments = new ArrayList<>();
        Long[] kids = (Long[]) intent.getSerializableExtra("kids");
        if (kids != null) {
            for (Long kid : kids) {
                Comment comment = new Comment(kid);
                mComments.add(comment);
            }
        } else {
            kids = new Long[0];
        }
        story.setKids(kids);

        return story;
    }

    @Override
    public void onEmptyCommentShown(Comment comment) {
        if (comment.getStatus() == ItemFetchStatus.NEVER_FETCHED) {
            HackerNewsApi.getInstance().fetchCommentDetail(this, comment, new FetchCommentDetailListener() {
                @Override
                public void onActionSuccess(Comment comment) {
                    refreshListDelay();
                    if (comment.shouldFetchLatestReply()) {
                        fetchLatestReply(comment);
                    }
                }

                @Override
                public void onActionFail(String errorMessage) {
                    Toast.makeText(CommentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void fetchLatestReply(final Comment comment) {
        Long id = comment.getLastestReplyId();
        if (id != null) {
            final Comment reply = new Comment(id);
            HackerNewsApi.getInstance().fetchCommentDetail(this, reply, new FetchCommentDetailListener() {
                @Override
                public void onActionSuccess(Comment fetchedReply) {
                    comment.setLatestReply(fetchedReply);
                    refreshListDelay();
                }

                @Override
                public void onActionFail(String errorMessage) {

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
