package com.testography.amgradle.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.testography.amgradle.data.storage.realm.CommentRealm;

public class SendMessageJob extends Job {
    protected SendMessageJob(String productId, CommentRealm comment) {
        super(new Params(JobPriority.MID));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

    }

    @Override
    protected void onCancel(int i, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int i, int i1) {
        return null;
    }
}
