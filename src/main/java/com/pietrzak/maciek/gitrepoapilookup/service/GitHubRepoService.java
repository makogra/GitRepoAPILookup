package com.pietrzak.maciek.gitrepoapilookup.service;

import com.pietrzak.maciek.gitrepoapilookup.GitHubBranch;
import com.pietrzak.maciek.gitrepoapilookup.Repo;
import com.pietrzak.maciek.gitrepoapilookup.restconstroller.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GitHubRepoService {

    private final WebClient gitHubApi;
    @Value("${github.token}")
    private String token;

    public GitHubRepoService(WebClient gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public Mono<List<Repo>> getRepos(String owner) {
        // Whem endpoint is set to root directory then there is call with favicon.ico that accidentally interact with this method
        if (owner.equals("favicon.ico")){
            System.out.println("favicon.ico was passed to getRepos method");
            return Mono.error(new UserNotFoundException("favicon.ico was passed to getRepos method"));
        }
        WebClient.RequestHeadersSpec<?> uri = gitHubApi.get().uri("/users/{owner}/repos", owner);
        if (token != null && !token.isEmpty()){
            uri = uri.header("Authorization" , "token " + token );
        }

        return uri.retrieve()
                .onStatus(status -> status.isSameCodeAs(HttpStatus.NOT_FOUND),
                        response -> Mono.error(new UserNotFoundException("User not found: " + owner)))
                .bodyToFlux(Repo.class)
                .collectList();
    }

    public Mono<List<GitHubBranch>> getBranches(String owner, String repoName) {
        return gitHubApi.get()
                .uri("/repos/{owner}/{repoName}/branches", owner, repoName)
                .header("Authorization" , "token " + token )
                .retrieve()
                .onStatus(status -> status.isSameCodeAs(HttpStatus.NOT_FOUND),
                        response -> Mono.error(new UserNotFoundException("Branch not found: " + owner)))
                .bodyToFlux(GitHubBranch.class)
                .collectList();
    }
}
