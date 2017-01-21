package com.testography.amgradle.di.components;

import com.testography.amgradle.di.modules.ModelModule;
import com.testography.amgradle.mvp.models.AbstractModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ModelModule.class)
@Singleton
public interface ModelComponent {
    void inject(AbstractModel abstractModel);
}
