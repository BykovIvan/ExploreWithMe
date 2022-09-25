package ru.bykov.explore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ExploreWithMeServer {
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeServer.class, args);
    }
}
