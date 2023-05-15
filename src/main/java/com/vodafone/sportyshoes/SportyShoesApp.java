package com.vodafone.sportyshoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SportyShoesApp {

    public static void main(String[] args) {
        SpringApplication.run(SportyShoesApp.class, args);
    }

}
