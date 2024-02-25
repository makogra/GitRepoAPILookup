package com.pietrzak.maciek.gitrepoapilookup;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"repositoryName", "OwnerLogin"})
public class GitHubRepoOfAccountResponse {

    String repositoryName;
    //TODO check if it's the same as Username that is passed as pathVariable
    String OwnerLogin;
    List<GitHubBranch> branches;

    public GitHubRepoOfAccountResponse(String repositoryName, String ownerLogin, Flux<GitHubBranch> branches) {
        System.out.println("repositoryName = " + repositoryName + ", ownerLogin = " + ownerLogin + ", branches = " + branches);
        this.repositoryName = repositoryName;
        OwnerLogin = ownerLogin;
        this.branches = new ArrayList<>();
        branches.subscribe(this.branches::add);
    }

    public GitHubRepoOfAccountResponse(String repositoryName, String ownerLogin, List<GitHubBranch> branches) {
        this.repositoryName = repositoryName;
        OwnerLogin = ownerLogin;
        this.branches = branches;
    }

    public GitHubRepoOfAccountResponse(String repositoryName, String ownerLogin) {
        this.repositoryName = repositoryName;
        OwnerLogin = ownerLogin;
    }

    public GitHubRepoOfAccountResponse() {
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getOwnerLogin() {
        return OwnerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        OwnerLogin = ownerLogin;
    }

    public List<GitHubBranch> getBranches() {
        return branches;
    }

    public void setBranches(List<GitHubBranch> branches) {
        branches.forEach(System.out::println);
        this.branches = branches;
    }
}
