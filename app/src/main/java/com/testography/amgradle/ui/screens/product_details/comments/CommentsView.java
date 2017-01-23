package com.testography.amgradle.ui.screens.product_details.comments;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.testography.amgradle.R;
import com.testography.amgradle.di.DaggerService;
import com.testography.amgradle.mvp.views.AbstractView;

import javax.inject.Inject;

import butterknife.BindView;

public class CommentsView extends AbstractView<CommentsScreen.CommentsPresenter> {

    private CommentsAdapter mAdapter = new CommentsAdapter();

    @Inject
    CommentsScreen.CommentsPresenter mCommentsPresenter;

    @BindView(R.id.comments_list)
    RecyclerView mCommentsList;

    public CommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<CommentsScreen.Component>getDaggerComponent(context).inject
                (this);
    }

    @Override
    public boolean viewOnBackPressed() {
        return false;
    }

    public CommentsAdapter getAdapter() {
        return mAdapter;
    }

    public void initView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mCommentsList.setLayoutManager(llm);
        mCommentsList.setAdapter(mAdapter);
    }
}
