package io.thavelka.githubbasic.models;

import com.google.gson.annotations.SerializedName;

public class Commit {
    public CommitDetails commit;
    public String sha;
    @SerializedName("html_url") public String url;
}
