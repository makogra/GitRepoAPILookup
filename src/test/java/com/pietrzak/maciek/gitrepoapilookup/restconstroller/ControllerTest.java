package com.pietrzak.maciek.gitrepoapilookup.restconstroller;

import com.pietrzak.maciek.gitrepoapilookup.service.GitHubRepoServiceMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Controller controller = new Controller(new GitHubRepoServiceMock(null));


    @Test
    void getReposThatAreNotForks() {
        controller.getReposThatAreNotForks("makogra");
        controller.getReposThatAreNotForks("spring-projects");
    }
}