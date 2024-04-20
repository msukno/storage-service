package com.springboot.podkaster.mp3cacheservice;

import com.springboot.podkaster.mp3cacheservice.data.accounts.backblaze.BackblazeAccount;
import com.springboot.podkaster.mp3cacheservice.data.accounts.backblaze.BackblazeAccountDetails;
import com.springboot.podkaster.mp3cacheservice.data.dao.Mp3DetailsDao;
import com.springboot.podkaster.mp3cacheservice.service.BackblazeService;
import com.springboot.podkaster.mp3cacheservice.service.S3Service;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Mp3cacheserviceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

		SpringApplication.run(Mp3cacheserviceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(Mp3DetailsDao mp3DetailsDao) {
		return runner -> {
			System.out.println("***Hello from BackBlaze Cache Service!***");
		};
	}
}