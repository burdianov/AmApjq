package com.testography.amgradle.di.components;

import com.testography.amgradle.data.managers.DataManager;
import com.testography.amgradle.di.modules.LocalModule;
import com.testography.amgradle.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(dependencies = AppComponent.class,
        modules = {NetworkModule.class, LocalModule.class})
@Singleton
public interface DataManagerComponent {
    void inject(DataManager dataManager);
}
