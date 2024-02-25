package com.pietrzak.maciek.gitrepoapilookup.service;

import com.pietrzak.maciek.gitrepoapilookup.GitHubBranch;
import com.pietrzak.maciek.gitrepoapilookup.Repo;
import com.pietrzak.maciek.gitrepoapilookup.SyntheticData;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class GitHubRepoServiceMock extends GitHubRepoService{

    public GitHubRepoServiceMock(WebClient gitHubApi) {
        super(gitHubApi);
    }

    @Override
    public Flux<Repo> getRepos(String owner) {
        return new SyntheticData(owner).getRepos();
    }

    @Override
    public Flux<GitHubBranch> getBranches(String owner, String repoName) {
        return new SyntheticData(owner).getBranches(repoName);
    }
}
