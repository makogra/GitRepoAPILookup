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
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@RestController
public class Controller {

    private final GitHubRepoService gitHubRepoService;

    public Controller(GitHubRepoService gitHubRepoService) {
        this.gitHubRepoService = gitHubRepoService;
    }

    @GetMapping("/api/{username}")
    public Flux<GitHubRepoOfAccountResponse> getReposThatAreNotForks(@PathVariable String username) {
        long start = System.currentTimeMillis();
        System.out.println("Thread getReposThatAreNotForks = " + Thread.currentThread().getName());
        Flux<Repo> repos = gitHubRepoService.getRepos(username);
//        Flux<GitHubRepoOfAccountResponse> result = repos
//                .parallel()
//                .runOn(Schedulers.parallel())
//                .flatMap(repo -> {
//                    System.out.println("Thread " + repo.getName() + " = " + Thread.currentThread().getName());
//                    return Mono.fromCallable(() -> new GitHubRepoOfAccountResponse(
//                            repo.getName(),
//                            repo.getLogin(),
//                            getBranches(repo.getLogin(), repo.getName())
//                    )).subscribeOn(Schedulers.parallel());
//                }
//        ).sequential();

        Flux<GitHubRepoOfAccountResponse> result = repos
                .parallel() // Enable parallel processing
                .runOn(Schedulers.parallel()) // Run each element on a parallel scheduler
                .flatMap(repo -> getBranches(repo.getLogin(), repo.getName())
                        .collectList() // Collect branches into a list
                        .flatMap(branches -> Mono.just(new GitHubRepoOfAccountResponse(
                                repo.getName(),
                                repo.getLogin(),
                                branches))))
                .sequential();

        long stop = System.currentTimeMillis();
        System.out.println("Time: " + (stop - start) + "ms");
        return result;
        //TODO add test for empty input. Now it's returning 404

    }

    /*
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


     */
    public Flux<GitHubBranch> getBranches(String owner, String repoName) {
        return gitHubRepoService.getBranches(owner, repoName);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(UserNotFoundException exc){
        UserErrorResponse errorResponse = new UserErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
