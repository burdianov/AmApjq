package com.testography.amgradle.di.components;

import com.squareup.picasso.Picasso;
import com.testography.amgradle.di.modules.PicassoCacheModule;
import com.testography.amgradle.di.scopes.RootScope;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = PicassoCacheModule.class)
@RootScope
public interface PicassoComponent {
    Picasso getPicasso();
}
