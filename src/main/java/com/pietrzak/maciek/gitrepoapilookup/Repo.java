package com.pietrzak.maciek.gitrepoapilookup;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record Repo(
        String name,
        String login,
        boolean fork
) {
    @JsonProperty("owner")
    public Repo unpackLogin(Map<String, String> owner) {
        return new Repo(this.name(), owner.get("login"), this.fork());
    }
}