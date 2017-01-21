package com.testography.amgradle.di.modules;

import com.testography.amgradle.di.scopes.RootScope;
import com.testography.amgradle.mvp.models.AccountModel;
import com.testography.amgradle.mvp.presenters.RootPresenter;

import dagger.Provides;

@dagger.Module
public class RootModule {
    @Provides
    @RootScope
    RootPresenter provideRootPresenter() {
        return new RootPresenter();
    }

    @Provides
    @RootScope
    AccountModel provideAccountModel() {
        return new AccountModel();
    }
}
