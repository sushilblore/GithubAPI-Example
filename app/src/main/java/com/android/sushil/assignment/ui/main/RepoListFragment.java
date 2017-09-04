package com.android.sushil.assignment.ui.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sushil.assignment.BaseApplication;
import com.android.sushil.assignment.R;
import com.android.sushil.assignment.data.models.LocalRepositories;
import com.android.sushil.assignment.data.models.Repositories;
import com.android.sushil.assignment.data.remote.Service;
import com.android.sushil.assignment.data.local.RealmRepositoriesController;
import com.android.sushil.assignment.util.ActivityUtils;
import com.android.sushil.assignment.util.Constants;
import com.android.sushil.assignment.util.EmptyRecyclerView;
import com.android.sushil.assignment.util.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;


public class RepoListFragment extends Fragment implements RepoListContract.View {

    private static final String TAG = "RepoListFragment";
    private RepoListPresenter mPresenter;
    private RepoListAdapter mAdapter;
    ProgressBar mProgressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    LinearLayoutManager mLayoutManager;

    EmptyRecyclerView mRecyclerView;
    private MainActivity mParent;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int mCurrentPage = PAGE_START;
    private Realm mRealm;

    @Inject
    public Service service;

    public RepoListFragment() {

    }

    public static RepoListFragment newInstance() {
        RepoListFragment fragment = new RepoListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParent = (MainActivity)getActivity();
        ((BaseApplication)mParent.getApplication()).getDependency().inject(this);

        setPresenter(new RepoListPresenter(service, this));
        mRealm = RealmRepositoriesController.with(this).getRealm();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_repo_list, container, false);

        mProgressBar = (ProgressBar) fragmentView.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) fragmentView.findViewById(R.id.error_layout);
        btnRetry = (Button) fragmentView.findViewById(R.id.error_btn_retry);
        txtError = (TextView) fragmentView.findViewById(R.id.error_txt_cause);
        mAdapter = new RepoListAdapter(mParent);

        mRecyclerView = (EmptyRecyclerView) fragmentView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(mParent, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                mCurrentPage += 1;

                mPresenter.loadRepoItems(mCurrentPage, 15);
            }

            @Override
            public int getTotalPageCount() {
                return Constants.TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        mPresenter.loadRepoItems(mCurrentPage, 15);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadRepoItems(mCurrentPage, 15);
            }
        });
        return fragmentView;
    }


    @Override
    public void setPresenter(RepoListContract.Presenter presenter) {
        mPresenter = (RepoListPresenter)presenter;
    }

    @Override
    public void onRepoListSuccess(List<Repositories> repoListResponse) {

        if(mCurrentPage == PAGE_START) {
            hideErrorView();
        }
        else {
            mAdapter.removeLoadingFooter();
            isLoading = false;
        }
        mProgressBar.setVisibility(View.GONE);
        List<LocalRepositories> cleanRepositoryList =  convertListRepositories(repoListResponse);
        mAdapter.addAll(cleanRepositoryList);

        if (mCurrentPage <= Constants.TOTAL_PAGES) mAdapter.addLoadingFooter();
        else isLastPage = true;

        insertIntoRealm(cleanRepositoryList);
    }




    @Override
    public void onFailure(Throwable t) {
        if(mAdapter.getItemCount() == 0) //No Items from service
        {
            Log.d(TAG, "The server didnt work get values from Realm");
            List<LocalRepositories> cleanRepositoryList = new ArrayList<>();
            cleanRepositoryList.addAll(RealmRepositoriesController.with(this).getRepositories());
            if(cleanRepositoryList.size() != 0) {
                mAdapter.addAll(cleanRepositoryList);
                mProgressBar.setVisibility(View.GONE);
            }
            else {
                showErrorView(t);
            }

        }
        else {
            mAdapter.showRetry(true, ActivityUtils.fetchErrorMessage(t, mParent));
        }

    }


    public void retryPageLoad() {
        mPresenter.loadRepoItems(mCurrentPage, 15);
    }

    @Override
    public void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

            txtError.setText(ActivityUtils.fetchErrorMessage(throwable, mParent));
        }
    }


    //Helper Methods
    @Override
    public void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private List<LocalRepositories> convertListRepositories(List<Repositories> repoListResponse) {
        List<LocalRepositories> list = new ArrayList<>();
        for (Repositories repository: repoListResponse)
        {
            LocalRepositories local = new LocalRepositories();
            local.setId(repository.getId());
            local.setName(repository.getName());
            local.setDescription(repository.getDescription());
            local.setForksCount(repository.getForksCount());
            local.setWatchersCount(repository.getWatchersCount());
            local.setLanguage(repository.getLanguage());
            list.add(local);
        }
        return list;
    }

    private void insertIntoRealm(List<LocalRepositories> cleanRepositoryList) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(cleanRepositoryList);
        mRealm.commitTransaction();
    }
}
