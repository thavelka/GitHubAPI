package io.thavelka.githubbasic.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.thavelka.githubbasic.R;
import io.thavelka.githubbasic.api.GithubService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommitsFragment extends Fragment {

    private static final String TAG = "CommitsFragment";

    public CommitsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: Inflating fragment view");
        View view = inflater.inflate(R.layout.fragment_commits, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .baseUrl("https://api.github.com/")
                .build();

        GithubService service = retrofit.create(GithubService.class);
        service.getCommits()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commits -> {
                    Log.d(TAG, "Retrieved commits");
                }, throwable -> {
                    Log.e(TAG, "Failed to retrieve commits", throwable);
                });

        return view;
    }
}