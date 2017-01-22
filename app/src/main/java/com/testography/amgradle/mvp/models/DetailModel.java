package com.testography.amgradle.mvp.models;

import com.testography.amgradle.data.network.res.CommentRes;
import com.testography.amgradle.data.storage.realm.CommentRealm;
import com.testography.amgradle.jobs.SendMessageJob;

public class DetailModel extends AbstractModel {
    public void saveComment(String productId,
                            CommentRes commentRes) {
        mDataManager.saveCommentToNetworkAndRealm(productId, commentRes);
    }

    public void sendComment(String id, CommentRealm commentRealm) {
        SendMessageJob job = new SendMessageJob(id, commentRealm);
        mJobManager.addJobInBackground(job);
    }
}
