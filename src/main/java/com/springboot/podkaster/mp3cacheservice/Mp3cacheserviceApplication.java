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
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class Mp3cacheserviceApplication {

	public static void main(String[] args) {
		// Only load .env file if it exists and the application is not running in production
		if (!isProductionEnvironment()) {
			Dotenv dotenv = loadDotenv();
			dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
		}

		SpringApplication.run(Mp3cacheserviceApplication.class, args);
	}
	private static boolean isProductionEnvironment() {
		// Check if the "prod" profile is active
		String profile = System.getProperty("spring.profiles.active", "default");
		return "prod".equalsIgnoreCase(profile);
	}

	private static Dotenv loadDotenv() {
		// Safely attempt to load the .env file, providing fallback to prevent crashes
		try {
			return Dotenv.load();
		} catch (Exception e) {
			System.out.println("Warning: .env file not found, continuing without it.");
			return Dotenv.configure().ignoreIfMissing().load();
		}
	}

	@Bean
	public CommandLineRunner commandLineRunner(Mp3DetailsDao mp3DetailsDao) {
		return runner -> {
			System.out.println("***Hello from BackBlaze Cache Service!***");
		};
	}
}