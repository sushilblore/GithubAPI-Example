package com.android.sushil.assignment.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.android.sushil.assignment.R;
import com.android.sushil.assignment.util.ActivityUtils;
import com.android.sushil.assignment.util.PaginationAdapterCallback;

public class MainActivity extends AppCompatActivity implements PaginationAdapterCallback {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.android.sushil.assignment.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";
    private RepoListFragment mRepoListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            mRepoListFragment = (RepoListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
            if (mRepoListFragment == null) {
                mRepoListFragment = RepoListFragment.newInstance();
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mRepoListFragment, R.id.contentFrame, "HomeScreen");
        }
    }

    @Override
    public void retryPageLoad() {
        mRepoListFragment.retryPageLoad();
    }

    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }
}
