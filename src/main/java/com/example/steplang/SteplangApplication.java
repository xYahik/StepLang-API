package com.example.steplang;

import com.example.steplang.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(AppConfig.class)
public class SteplangApplication {

	public static void main(String[] args) {
		SpringApplication.run(SteplangApplication.class, args);
	}

}
