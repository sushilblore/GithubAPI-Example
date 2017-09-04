package com.android.sushil.assignment.data.local;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.android.sushil.assignment.data.models.LocalRepositories;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sushiljha on 17/08/2017.
 */

public class RealmRepositoriesController {

    private static RealmRepositoriesController instance;
    private final Realm realm;

    public RealmRepositoriesController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmRepositoriesController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmRepositoriesController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmRepositoriesController with(Activity activity) {

        if (instance == null) {
            instance = new RealmRepositoriesController(activity.getApplication());
        }
        return instance;
    }

    public static RealmRepositoriesController with(Application application) {

        if (instance == null) {
            instance = new RealmRepositoriesController(application);
        }
        return instance;
    }

    public static RealmRepositoriesController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(LocalRepositories.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<LocalRepositories> getRepositories() {

        return realm.where(LocalRepositories.class).findAll();
    }

    //query a single item with the given id
    public LocalRepositories getRepository(String id) {

        return realm.where(LocalRepositories.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasRepositories() {

        return !realm.allObjects(LocalRepositories.class).isEmpty();
    }


}
