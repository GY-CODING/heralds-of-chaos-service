package org.gycoding.heraldsofchaos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@ComponentScan(basePackages = "org.gycoding")
public class HeraldsOfChaosApplication {
	public static void main(String[] args) {
        SpringApplication.run(HeraldsOfChaosApplication.class, args);
	}
}
