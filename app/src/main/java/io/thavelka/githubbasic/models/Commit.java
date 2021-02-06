package io.thavelka.githubbasic.models;

import com.google.gson.annotations.SerializedName;

public class Commit {
    @SerializedName("commit.author") User user;
    @SerializedName("commit.message") String message;
    String sha;
}
