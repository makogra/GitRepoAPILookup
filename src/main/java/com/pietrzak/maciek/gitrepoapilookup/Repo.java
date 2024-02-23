package com.pietrzak.maciek.gitrepoapilookup;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Repo {

    private String name;
    private String login;
    private  boolean fork;

    public Repo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    @JsonProperty("owner")
    public void unpackLogin(Map<String, String> owner) {
        this.login = owner.get("login");
    }
}
