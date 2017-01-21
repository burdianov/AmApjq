package com.testography.amgradle.mvp.models;

public class AuthModel extends AbstractModel {

    public AuthModel() {

    }

    public boolean isAuthUser() {
        return mDataManager.isAuthUser();
    }

    public void loginUser(String email, String password) {
        mDataManager.loginUser(email, password);
    }
}
