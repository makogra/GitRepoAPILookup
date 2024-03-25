package com.pietrzak.maciek.gitrepoapilookup.restconstroller;

import com.pietrzak.maciek.gitrepoapilookup.GitHubBranch;
import com.pietrzak.maciek.gitrepoapilookup.GitHubRepoOfAccountResponse;
import com.pietrzak.maciek.gitrepoapilookup.Repo;
import com.pietrzak.maciek.gitrepoapilookup.service.GitHubRepoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ControllerTest {

    @Mock
    private static GitHubRepoService gitHubRepoServiceMock;

    private static Controller controller;
    private final static String owner1 = "owner1";

    private void initOwner1() {
        Flux<GitHubBranch> branchesOwnedByOwner1Repo1 = Flux.just(new GitHubBranch("branch1_O1_R1", "sha1_O1_R1"),
                new GitHubBranch("branch2_O1_R1", "sha2_O1_R1"),
                new GitHubBranch("branch3_O1_R1", "sha3_O1_R1"));

        Flux<GitHubBranch> branchesOwnedByOwner1Repo2 = Flux.just(new GitHubBranch("branch1_O1_R2", "sha1_O1_R2"),
                new GitHubBranch("branch2_O1_R2", "sha2_O1_R2"),
                new GitHubBranch("branch3_O1_R2", "sha3_O1_R2"));

        Flux<GitHubBranch> branchesOwnedByOwner1Repo3Fork = Flux.just(new GitHubBranch("branch1_O1_R3_F", "sha1_O1_R3_F"),
                new GitHubBranch("branch2_O1_R3_F", "sha2_O1_R3_F"),
                new GitHubBranch("branch3_O1_R3_F", "sha3_O1_R3_F"));


        Flux<Repo> reposOwnedByOwner1 = Flux.just(new Repo("repo1", owner1, false),
                new Repo("repo2", owner1, false),
                new Repo("repo3", owner1, true));

        when(gitHubRepoServiceMock.getRepos(owner1)).thenReturn(reposOwnedByOwner1);


        when(gitHubRepoServiceMock.getBranches(owner1, "repo1")).thenReturn(branchesOwnedByOwner1Repo1);
        when(gitHubRepoServiceMock.getBranches(owner1, "repo2")).thenReturn(branchesOwnedByOwner1Repo2);
//        when(gitHubRepoServiceMock.getBranches(owner1, "repo3")).thenReturn(branchesOwnedByOwner1Repo3Fork);

        updateController();
    }

    private void initOwner2() {
        Flux<GitHubBranch> branchesOwnedByOwner2Repo1Fork = Flux.just(new GitHubBranch("branch1_O2_R1_F", "sha1_O2_R1_F"),
                new GitHubBranch("branch2_O2_R1_F", "sha2_O2_R1_F"),
                new GitHubBranch("branch3_O2_R1_F", "sha3_O2_R1_F"));

        Flux<GitHubBranch> branchesOwnedByOwner2Repo1 = Flux.just(new GitHubBranch("branch1_O2_R2", "sha1_O2_R2"),
                new GitHubBranch("branch2_O2_R2", "sha2_O2_R2"));

        Flux<Repo> reposOwnedByOwner2 = Flux.just(new Repo("repo1", "owner2", true),
                new Repo("repo2", "owner2", false));

        when(gitHubRepoServiceMock.getRepos("owner2")).thenReturn(reposOwnedByOwner2);

        when(gitHubRepoServiceMock.getBranches("owner2", "repo1")).thenReturn(branchesOwnedByOwner2Repo1Fork);
        when(gitHubRepoServiceMock.getBranches("owner2", "repo2")).thenReturn(branchesOwnedByOwner2Repo1);

        updateController();
    }

    private void initOwner3() {
        Flux<GitHubBranch> branchesOwnedByOwner3Repo1Fork = Flux.just(new GitHubBranch("branch1_O3_R1_F", "sha1_O3_R1_F"),
                new GitHubBranch("branch2_O3_R1_F", "sha2_O3_R1_F"),
                new GitHubBranch("branch3_O3_R1_F", "sha3_O3_R1_F"));

        Flux<Repo> reposOwnedByOwner3 = Flux.just(new Repo("repo1", "owner3", true));

        when(gitHubRepoServiceMock.getRepos("owner3")).thenReturn(reposOwnedByOwner3);

//        when(gitHubRepoServiceMock.getBranches("owner3", "repo1")).thenReturn(branchesOwnedByOwner3Repo1Fork);

        updateController();
    }

    private void updateController() {
        controller = new Controller(gitHubRepoServiceMock);
    }

    @Test
    void getReposThatAreNotForks_givenValidInputAndContainingData_returnsNotNull() {
        Flux<GitHubRepoOfAccountResponse> reposThatAreNotForks = controller.getReposThatAreNotForks(owner1);

        Assertions.assertThat(reposThatAreNotForks).isNotNull();
    }

    @Test
    void getRepos_givenValidInputAndContainingData_returnsNotNull() {
        // Mock data
        String owner = "owner5";
        Flux<Repo> reposOwnedByOwner5 = Flux.just(
                new Repo("repo1", owner, false),
                new Repo("repo2", owner, false),
                new Repo("repo3", owner, true));

        // Stub behavior of the mock
        when(gitHubRepoServiceMock.getRepos(owner)).thenReturn(reposOwnedByOwner5);
        updateController();

        // Call the method under test
        Flux<Repo> repos = controller.getRepos(owner);

        // Verify
        Assertions.assertThat(repos.collectList().block()).isEqualTo(reposOwnedByOwner5.collectList().block());
    }

    //test for only forks
    @Test
    void getReposThatAreNotForks_givenValidInputAndContainingOnlyForks_returnsNull() {
        initOwner3();
        Flux<GitHubRepoOfAccountResponse> reposThatAreNotForks = controller.getReposThatAreNotForks("owner3");

        Assertions.assertThat(reposThatAreNotForks.blockFirst()).isNull();
    }

    // test for owner1 with 3 repos, 2 of them are not forks
    @Test
    void getReposThatAreNotForks_givenOwner1_sizeOfReturn() {
        initOwner1();
        Flux<GitHubRepoOfAccountResponse> reposThatAreNotForks = controller.getReposThatAreNotForks(owner1);

        Assertions.assertThat(reposThatAreNotForks).isNotNull();
        Assertions.assertThat(reposThatAreNotForks.collectList().block().size()).isEqualTo(2);

    }

}