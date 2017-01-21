package com.testography.amgradle;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.testography.amgradle.di.DaggerService;
import com.testography.amgradle.di.components.AppComponent;
import com.testography.amgradle.di.components.DaggerAppComponent;
import com.testography.amgradle.di.modules.AppModule;
import com.testography.amgradle.di.modules.PicassoCacheModule;
import com.testography.amgradle.di.modules.RootModule;
import com.testography.amgradle.mortar.ScreenScoper;
import com.testography.amgradle.ui.activities.DaggerRootActivity_RootComponent;
import com.testography.amgradle.ui.activities.RootActivity;

import io.realm.Realm;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

public class App extends Application {
    private static SharedPreferences sSharedPreferences;
    private static Context sAppContext;
    private static Context sContext;
    private static AppComponent sAppComponent;
    private static RootActivity.RootComponent sRootActivityRootComponent;

    private MortarScope mRootScope;
    private MortarScope mRootActivityScope;

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    @Override
    public Object getSystemService(String name) {
        return mRootScope.hasService(name) ? mRootScope.getService(name) : super
                .getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        createAppComponent();
        createRootActivityComponent();

        sContext = getApplicationContext();

        mRootScope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, sAppComponent)
                .build("Root");

        mRootActivityScope = mRootScope.buildChild()
                .withService(DaggerService.SERVICE_NAME, sRootActivityRootComponent)
                .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                .build(RootActivity.class.getName());

        ScreenScoper.registerScope(mRootScope);
        ScreenScoper.registerScope(mRootActivityScope);

        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sAppContext = getApplicationContext();
    }

    private void createAppComponent() {
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

    private void createRootActivityComponent() {
        sRootActivityRootComponent = DaggerService.createComponent(RootActivity
                        .RootComponent.class, DaggerRootActivity_RootComponent.class,
                getAppComponent(),
                new PicassoCacheModule(),
                new RootModule());
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static RootActivity.RootComponent getRootActivityRootComponent() {
        return sRootActivityRootComponent;
    }

    public static Context getContext() {
        return sContext;
    }
}
