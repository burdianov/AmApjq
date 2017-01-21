package com.testography.amgradle.di.components;

import android.content.Context;

import com.testography.amgradle.di.modules.AppModule;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
}
