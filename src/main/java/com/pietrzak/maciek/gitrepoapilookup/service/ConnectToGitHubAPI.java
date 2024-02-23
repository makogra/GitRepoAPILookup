package com.pietrzak.maciek.gitrepoapilookup.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@PropertySource("classpath:application.properties")
public class ConnectToGitHubAPI {

    @Value("${github.api.url}")
    String urlAPI;
    @Bean
    public WebClient getGitHubAPI() {
        return WebClient.create(urlAPI);
    }
}
