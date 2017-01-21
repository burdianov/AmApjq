package com.testography.amgradle.mvp.views;

import android.support.annotation.Nullable;

import com.testography.amgradle.data.storage.dto.UserInfoDto;

public interface IRootView extends IView {
    void showMessage(String message);
    void showError(Throwable e);
    void showLoad();
    void hideLoad();

    void initDrawer(UserInfoDto userInfoDto);
    void changeCart(int resId);

    @Nullable
    IView getCurrentScreen();
}
