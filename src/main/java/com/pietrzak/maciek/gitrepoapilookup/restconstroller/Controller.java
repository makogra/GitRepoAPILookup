package com.pietrzak.maciek.gitrepoapilookup.restconstroller;

import com.pietrzak.maciek.gitrepoapilookup.GitHubBranch;
import com.pietrzak.maciek.gitrepoapilookup.GitHubRepoOfAccountResponse;
import com.pietrzak.maciek.gitrepoapilookup.Repo;
import com.pietrzak.maciek.gitrepoapilookup.service.GitHubRepoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class Controller {

    private final GitHubRepoService gitHubRepoService;

    public Controller(GitHubRepoService gitHubRepoService) {
        this.gitHubRepoService = gitHubRepoService;
    }

    @GetMapping("/api/{username}")
    public List<GitHubRepoOfAccountResponse> getReposThatAreNotForks(@PathVariable String username) {
        long start = System.currentTimeMillis();
        Mono<List<Repo>> repos = gitHubRepoService.getRepos(username);
        List<GitHubRepoOfAccountResponse> result = repos.block()
                .stream()
                .filter(repo -> !repo.isFork())
                .map(repo -> new GitHubRepoOfAccountResponse(repo.getName(), repo.getLogin(), getBranches(repo.getLogin(), repo.getName()))).toList();
        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start) + "ms");
        return result;
        //TODO add test for empty input. Now it's returning 404

    }

    @GetMapping("test/{username}")
    public List<Repo> getRepos(@PathVariable String username) {
        List<Repo> repos = gitHubRepoService.getRepos(username).block();
        StringBuilder sbRepos = new StringBuilder();
        if (repos != null) {
            repos.forEach(repo -> {
                sbRepos.append(repo).append("|");
                getBranches(repo.getLogin(), repo.getName()).forEach(branch -> sbRepos.append(branch).append(":"));
                sbRepos.deleteCharAt(sbRepos.length()-1);
                sbRepos.append("#");
            });
            sbRepos.deleteCharAt(sbRepos.length()-1);
            System.out.println(sbRepos.toString());
        }
        return repos;
    }

    public List<GitHubBranch> getBranches(String owner,String repoName) {
        return gitHubRepoService.getBranches(owner, repoName).block();
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(UserNotFoundException exc){
        UserErrorResponse errorResponse = new UserErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
