package io.thavelka.githubbasic.api;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    public static final String BASE_URL = "https://api.github.com/";

    @Provides
    Gson getGson() {
        return new Gson();
    }

    @Provides
    Retrofit getRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();
    }

    @Provides
    @Singleton
    GithubService getGithubService(Retrofit retrofit) {
        return retrofit.create(GithubService.class);
    }
}
