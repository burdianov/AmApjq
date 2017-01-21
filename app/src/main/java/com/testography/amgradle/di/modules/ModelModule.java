package com.testography.amgradle.di.modules;

import com.testography.amgradle.data.managers.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule extends FlavorModelModule{
    @Provides
    @Singleton
    DataManager privateDataManager() {
        return DataManager.getInstance();
    }
}
