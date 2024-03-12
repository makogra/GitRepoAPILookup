package com.pietrzak.maciek.gitrepoapilookup;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record GitHubBranch(
        String name,
        String sha
) {
    @JsonProperty("commit")
    public GitHubBranch unpackSha(Map<String, String> commit) {
        return new GitHubBranch(this.name(), commit.get("sha"));
    }
}
