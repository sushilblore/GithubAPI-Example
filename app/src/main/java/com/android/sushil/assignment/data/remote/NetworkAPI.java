package com.android.sushil.assignment.data.remote;

import com.android.sushil.assignment.data.models.Repositories;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sushiljha on 14/08/2017.
 */

public interface NetworkAPI {

    @GET("users/JakeWharton/repos")
    Observable<List<Repositories>> getRepoList(@Query("page") int pageNo, @Query("per_page") int noOfItems);

}
