package com.testography.amgradle.ui.screens.product_details.comments;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.testography.amgradle.R;
import com.testography.amgradle.data.storage.realm.CommentRealm;
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

    public void showAddCommentDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View dialogView = inflater.inflate(R.layout.dialog_comment, null);

        AppCompatRatingBar ratingBar = (AppCompatRatingBar) dialogView.findViewById(R.id.comment_rb);
        EditText commentEt = (EditText) dialogView.findViewById(R.id.comment_et);

        dialogBuilder.setTitle("Comment the product?")
                .setView(dialogView)
                .setPositiveButton("Comment the product", (dialog, which) -> {
                    CommentRealm comment = new CommentRealm(ratingBar.getRating(),
                            commentEt.getText().toString());
                    //mPresenter.addComment(comment);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }
}
