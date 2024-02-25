package com.pietrzak.maciek.gitrepoapilookup;

import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyntheticData {

    private Map<Repo, List<GitHubBranch>> reposMap = new HashMap<>();

    public SyntheticData(String userName) {
        String file = readFile(userName);
        String[] repos = file.split("#");
        for(String repo : repos) {
            String[] repoAllData = repo.split("\\|");
            String[] repoData = repoAllData[0].split(",");
            Repo r = new Repo();
            r.setName(repoData[0]);
            r.setLogin(repoData[1]);
            r.setFork(Boolean.parseBoolean(repoData[2]));

            List<GitHubBranch> branchesList = extractBranches(repoAllData);
            reposMap.put(r, branchesList);
        }
    }

    private static List<GitHubBranch> extractBranches(String[] repoAllData) {
        List<GitHubBranch> branchesList = new ArrayList<>();
        if (repoAllData.length > 1) {
            String[] branches = repoAllData[1].split(":");
            for(String branch : branches) {
                String[] branchData = branch.split(",");
                GitHubBranch b = new GitHubBranch();
                b.setName(branchData[0]);
                b.setSha(branchData[1]);
                branchesList.add(b);
            }
        }
        return branchesList;
    }

    private static String readFile(String userName) {
        try(BufferedReader br = new BufferedReader(new FileReader("src/test/resources/input" + userName))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void printFile() {
        try(BufferedReader br = new BufferedReader(new FileReader("src/test/resources/inputmakogra1"))) {
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Flux<Repo> getRepos() {
        return Flux.fromStream(reposMap.keySet().stream());
    }

    public Flux<GitHubBranch> getBranches(String repoName) {
        return Flux.fromIterable(reposMap.entrySet()
                .stream()
                .filter(repoListEntry -> repoListEntry.getKey().getName().equals(repoName))
                .map(Map.Entry::getValue)
                .findAny()
                .orElse(new ArrayList<>()));
    }
}
