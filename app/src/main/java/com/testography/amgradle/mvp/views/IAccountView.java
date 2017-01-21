package com.testography.amgradle.mvp.views;

public interface IAccountView extends IView {
    void changeState();
    void showEditState();
    void showPreviewState();
    void showPhotoSourceDialog();
    String getUserName();
    String getUserPhone();
}