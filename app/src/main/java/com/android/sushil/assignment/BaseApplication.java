package com.android.sushil.assignment;

import android.app.Application;


import com.android.sushil.assignment.Injection.component.DaggerDependency;
import com.android.sushil.assignment.Injection.component.Dependency;
import com.android.sushil.assignment.Injection.module.NetworkModule;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {
    Dependency mDependency;

    @Override
    public void onCreate() {
        super.onCreate();
        File cacheFile = new File(getCacheDir(), "responses");
        mDependency = DaggerDependency.builder().networkModule(new NetworkModule(cacheFile)).build();


        //Realm
        RealmConfiguration realmConfiguration = new
                RealmConfiguration.Builder(this).name(Realm.DEFAULT_REALM_NAME).schemaVersion(0).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public Dependency getDependency() {
        return mDependency;
    }
}
