package com.testography.amgradle.mvp.models;

import com.testography.amgradle.data.managers.DataManager;
import com.testography.amgradle.data.storage.realm.ProductRealm;

import rx.Observable;

public class AuthModel extends AbstractModel {

    public AuthModel() {

    }

    public boolean isAuthUser() {
        return mDataManager.isAuthUser();
    }

    public void loginUser(String email, String password) {
        mDataManager.loginUser(email, password);
    }

    public Observable<ProductRealm> getProductObsFromNetwork() {
        return DataManager.getInstance().getProductsObsFromNetwork();
    }
}
