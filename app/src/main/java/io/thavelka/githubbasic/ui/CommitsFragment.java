package io.thavelka.githubbasic.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.thavelka.githubbasic.R;
import io.thavelka.githubbasic.api.DaggerNetworkComponent;
import io.thavelka.githubbasic.api.GithubService;
import io.thavelka.githubbasic.models.Commit;

public class CommitsFragment extends Fragment {

    private static final String TAG = "CommitsFragment";
    private GithubService service;
    private Disposable disposable;
    private CommitsRecyclerViewAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView emptyText;

    public CommitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: Inflating fragment view");
        View view = inflater.inflate(R.layout.fragment_commits, container, false);

        // Bind views
        swipeRefreshLayout = view.findViewById(R.id.commits_swiperefreshlayout);
        recyclerView = view.findViewById(R.id.commits_recyclerview);
        emptyText = view.findViewById(R.id.commits_text_empty);

        // Configure views
        adapter = new CommitsRecyclerViewAdapter(getActivity(), null, this::onItemClick);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.teal_200));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Loading already in progress
            if (disposable != null && !disposable.isDisposed()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            refreshCommits();
        });

        // Get commits
        service = DaggerNetworkComponent.create().getGithubService();
        refreshCommits();

        return view;
    }

    @Override
    public void onDestroy() {
        adapter = null;
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
        super.onDestroy();
    }

    private void refreshCommits() {
        if (disposable != null && !disposable.isDisposed()) {
            // Loading in progress, return
            return;
        }

        disposable = service.getCommits()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                })
                .subscribe(commits -> {
                    Log.d(TAG, "Retrieved commits");
                    if (adapter != null) {
                        adapter.setCommits(commits);
                    }
                    if (emptyText != null) {
                        emptyText.setVisibility(commits.isEmpty() ? View.VISIBLE : View.GONE);
                    }

                }, throwable -> {
                    Log.e(TAG, "Failed to retrieve commits", throwable);
                    Snackbar.make(getView(), R.string.commits_loading_error, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.action_retry, v -> refreshCommits())
                            .setActionTextColor(getResources().getColor(R.color.teal_200))
                            .show();
                });
    }

    /**
     * Launches commit details in browser or GitHub app
     */
    private void onItemClick(Commit commit) {
        if (TextUtils.isEmpty(commit.url)) return;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(commit.url));
        if (browserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(browserIntent);
        }
    }
}