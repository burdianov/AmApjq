package com.testography.amgradle.mvp.models;

import com.testography.amgradle.data.network.res.CommentRes;

public class DetailModel extends AbstractModel {
    public void saveComment(String productId,
                            CommentRes commentRes) {
        mDataManager.saveCommentToNetworkAndRealm(productId, commentRes);
    }
}
