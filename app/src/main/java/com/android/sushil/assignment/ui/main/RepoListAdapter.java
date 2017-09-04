package com.android.sushil.assignment.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.sushil.assignment.R;
import com.android.sushil.assignment.data.models.LocalRepositories;
import com.android.sushil.assignment.data.models.Repositories;
import com.android.sushil.assignment.util.EmptyRecyclerView;
import com.android.sushil.assignment.util.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sushiljha on 14/08/2017.
 */

public class RepoListAdapter extends EmptyRecyclerView.Adapter<EmptyRecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<LocalRepositories> mRepoResults;
    private Context mContext;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private PaginationAdapterCallback mCallback;
    private String errorMsg;


    public RepoListAdapter(Context context) {
        mContext = context;
        this.mCallback = (PaginationAdapterCallback) context;
        mRepoResults = new ArrayList<>();
    }

    @Override
    public EmptyRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        EmptyRecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }


    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        EmptyRecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.cardview_repo_item, parent, false);
        viewHolder = new RepoVH(v1);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(EmptyRecyclerView.ViewHolder holder, int position) {
        LocalRepositories result = mRepoResults.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final RepoVH repoVH = (RepoVH) holder;

                repoVH.mRepoName.setText(result.getName());
                repoVH.mRepoDesc.setText(result.getDescription());
                repoVH.mForksCount.setText(String.valueOf(result.getForksCount()));
                repoVH.mWatchersCount.setText(String.valueOf(result.getWatchersCount()));
                repoVH.mLanguage.setText(result.getLanguage());
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    mContext.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRepoResults == null ? 0 : mRepoResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mRepoResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    //Helpers
    public void add(LocalRepositories r) {
        mRepoResults.add(r);
        notifyItemInserted(mRepoResults.size() - 1);
    }

    public void addAll(List<LocalRepositories> moveResults) {
        for (LocalRepositories result : moveResults) {
            add(result);
        }
    }

    public void remove(Repositories r) {
        int position = mRepoResults.indexOf(r);
        if (position > -1) {
            mRepoResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new LocalRepositories());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mRepoResults.size() - 1;
        LocalRepositories result = getItem(position);

        if (result != null) {
            mRepoResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public LocalRepositories getItem(int position) {
        return mRepoResults.get(position);
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(mRepoResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    //View Holders

    protected class RepoVH extends RecyclerView.ViewHolder {
        private TextView mRepoName;
        private TextView mRepoDesc;
        private ProgressBar mProgress;
        TextView mForksIcon;
        TextView mWatchersIcon;
        TextView mForksCount;
        TextView mWatchersCount;
        TextView mLanguage;


        public RepoVH(View itemView) {
            super(itemView);

            mRepoName = (TextView) itemView.findViewById(R.id.tv_repo_name);
            mRepoDesc = (TextView) itemView.findViewById(R.id.tv_repo_description);
            mProgress = (ProgressBar) itemView.findViewById(R.id.repo_progress);

            mForksCount = (TextView) itemView.findViewById(R.id.tv_forks);
            mWatchersCount = (TextView) itemView.findViewById(R.id.tv_watchers);
            mLanguage = (TextView) itemView.findViewById(R.id.tv_language);

            mForksIcon = (TextView) itemView.findViewById(R.id.tv_forks_icon);
            mForksIcon.setText(R.string.forks);

            mWatchersIcon = (TextView) itemView.findViewById(R.id.tv_watchers_icon);
            mWatchersIcon.setText(R.string.watchers);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

}

