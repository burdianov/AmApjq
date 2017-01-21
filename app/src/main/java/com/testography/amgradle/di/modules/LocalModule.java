package com.testography.amgradle.di.modules;

import android.content.Context;

import com.testography.amgradle.data.managers.PreferencesManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalModule extends FlavorLocalModule {
    @Provides
    @Singleton
    PreferencesManager providePreferencesManager(Context context) {
        return new PreferencesManager(context);
    }
}
