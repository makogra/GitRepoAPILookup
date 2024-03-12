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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class Controller {

    private final GitHubRepoService gitHubRepoService;

    public Controller(GitHubRepoService gitHubRepoService) {
        this.gitHubRepoService = gitHubRepoService;
    }

    @GetMapping("/api/{username}")
    public Flux<GitHubRepoOfAccountResponse> getReposThatAreNotForks(@PathVariable String username) {
        Flux<Repo> repos = gitHubRepoService.getRepos(username);

        Flux<GitHubRepoOfAccountResponse> result = repos
                .parallel()
                .runOn(Schedulers.parallel())
                .filter(repo -> !repo.fork())
                .flatMap(repo -> getBranches(repo.login(), repo.name())
                        .collectList()
                        .flatMap(branches -> Mono.just(new GitHubRepoOfAccountResponse(
                                repo.name(),
                                repo.login(),
                                branches))))
                .sequential();
        return result;
    }

    @GetMapping("/api/")
    public ResponseEntity<UserErrorResponse> egtEmptyAPI(){
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.BAD_REQUEST.value(), "Empty username");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public Flux<GitHubBranch> getBranches(String owner, String repoName) {
        return gitHubRepoService.getBranches(owner, repoName);
    }

    @GetMapping("/error")
    public ResponseEntity<UserErrorResponse> error() {
        return handleUserNotFoundException(new UserNotFoundException("Page not found."));
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(UserNotFoundException exc){
        UserErrorResponse errorResponse = new UserErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
