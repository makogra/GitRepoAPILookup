package com.pietrzak.maciek.gitrepoapilookup;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GitHubBranch {

    String name;
    String sha;


    public GitHubBranch() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    @JsonProperty("commit")
    public void unpackSha(Map<String, String> commit) {
        this.sha = commit.get("sha");
    }
}
