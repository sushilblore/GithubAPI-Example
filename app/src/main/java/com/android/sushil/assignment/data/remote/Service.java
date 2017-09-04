package com.android.sushil.assignment.data.remote;

import com.android.sushil.assignment.data.models.Repositories;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sushiljha on 14/08/2017.
 */

public class Service {
    private final NetworkAPI networkService;

    public Service(NetworkAPI networkService) {
        this.networkService = networkService;
    }
    public Subscription getRepoList(final GetRepoListCallback callback,
                                    int pageNumber, int noOfPages) {

        return networkService.getRepoList(pageNumber, noOfPages)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Repositories>>>() {
                    @Override
                    public Observable<? extends List<Repositories>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<List<Repositories>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);

                    }

                    @Override
                    public void onNext(List<Repositories> repoListResponse) {
                        callback.onSuccess(repoListResponse);

                    }
                });
    }
    public interface GetRepoListCallback{
        void onSuccess(List<Repositories> repoListResponse);
        void onError(Throwable e);
    }
}

