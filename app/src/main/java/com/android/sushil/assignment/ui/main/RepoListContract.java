package com.android.sushil.assignment.ui.main;

import com.android.sushil.assignment.data.models.Repositories;
import com.android.sushil.assignment.ui.base.BasePresenter;
import com.android.sushil.assignment.ui.base.BaseView;

import java.util.List;

/**
 * Created by sushiljha on 14/08/2017.
 */

public class RepoListContract {
    public interface View extends BaseView<Presenter> {
        void onRepoListSuccess(List<Repositories> repoListResponse);
        void onFailure(Throwable t);
        void showErrorView(Throwable t);
        void hideErrorView();
    }

    public interface Presenter extends BasePresenter {
        void loadRepoItems(int pageNumber, int noOfPages);
    }
}
