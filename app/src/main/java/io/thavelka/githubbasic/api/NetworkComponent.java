package io.thavelka.githubbasic.api;

import dagger.Component;

@Component(modules = NetworkModule.class)
public interface NetworkComponent {
    GithubService getGithubService();
}
