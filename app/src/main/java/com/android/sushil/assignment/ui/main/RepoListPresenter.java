package com.android.sushil.assignment.ui.main;

import android.util.Log;

import com.android.sushil.assignment.data.models.Repositories;
import com.android.sushil.assignment.data.remote.Service;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sushiljha on 14/08/2017.
 */

public class RepoListPresenter implements RepoListContract.Presenter {

    private static final String TAG = "RepoListPresenter";
    private final Service mService;
    private final RepoListContract.View mView;
    private CompositeSubscription mSubscriptions;


    public RepoListPresenter(Service service, RepoListContract.View view) {
        this.mService = service;
        this.mView = view;
        this.mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void loadRepoItems(int pageNumber, int noOfItems) {
        Subscription subscription = mService.getRepoList(new Service.GetRepoListCallback() {

            @Override
            public void onSuccess(List<Repositories> repoListResponse) {
                Log.i(TAG, "loadRepoItems : onSuccess");
                mView.onRepoListSuccess(repoListResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "loadRepoItems : onError");
                mView.onFailure(e);
            }

        }, pageNumber, noOfItems);

        mSubscriptions.add(subscription);
    }

    public void onStop() {
        mSubscriptions.unsubscribe();
    }
}
