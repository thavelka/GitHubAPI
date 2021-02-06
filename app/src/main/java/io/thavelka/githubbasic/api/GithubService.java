package io.thavelka.githubbasic.api;

import java.util.List;

import io.reactivex.Observable;
import io.thavelka.githubbasic.models.Commit;
import retrofit2.http.GET;

public interface GithubService {
    @GET("users/thavelka/Spambot/commits")
    Observable<List<Commit>> getCommits();
}
