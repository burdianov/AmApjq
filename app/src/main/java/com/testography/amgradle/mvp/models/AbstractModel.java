package com.testography.amgradle.mvp.models;

import com.birbit.android.jobqueue.JobManager;
import com.testography.amgradle.data.managers.DataManager;
import com.testography.amgradle.di.DaggerService;
import com.testography.amgradle.di.components.DaggerModelComponent;
import com.testography.amgradle.di.components.ModelComponent;
import com.testography.amgradle.di.modules.ModelModule;

import javax.inject.Inject;

public abstract class AbstractModel {

    @Inject
    DataManager mDataManager;
    @Inject
    JobManager mJobManager;

    public AbstractModel() {
        ModelComponent component = DaggerService.getComponent(ModelComponent.class);
        if (component == null) {
            component = createDaggerComponent();
            DaggerService.registerComponent(ModelComponent.class, component);
        }
        component.inject(this);
    }

    private ModelComponent createDaggerComponent() {
        return DaggerModelComponent.builder()
                .modelModule(new ModelModule())
                .build();
    }
}