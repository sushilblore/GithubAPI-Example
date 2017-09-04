package com.android.sushil.assignment.Injection.component;

import com.android.sushil.assignment.Injection.module.NetworkModule;
import com.android.sushil.assignment.ui.main.MainActivity;
import com.android.sushil.assignment.ui.main.RepoListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sushiljha on 14/08/2017.
 */

@Singleton
@Component(modules = {NetworkModule.class,})
public interface Dependency {
    void inject(RepoListFragment repoListFragment);
}
