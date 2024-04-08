package com.springboot.podkaster.mp3cacheservice;

import com.springboot.podkaster.mp3cacheservice.data.dao.Mp3DetailsDao;
import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;
import com.springboot.podkaster.mp3cacheservice.s3.S3Uploader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootApplication
public class Mp3cacheserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mp3cacheserviceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(Mp3DetailsDao mp3DetailsDao) {
		return runner -> {
/*			Mp3Details songDetails = new Mp3Details(22, "http://song_22");
			mp3DetailsDao.create(songDetails);
			System.out.println("Created: %s".formatted(songDetails));*/

			//System.out.println("Database content: %s".formatted(mp3DetailsDao.getAll()));

			S3Uploader uploader = new S3Uploader();
			//uploader.uploadFile("/home/msukno/spring-boot-course/mp3cacheservice/src/main/resources/static/test-backblaze.txt");
			uploader.downloadFile("soundSong", "./soundSongLocal.mp3");
			//uploader.downloadFile("filename", "./mojTekst.txt");
			//uploader.uploadFromUrlUsingTemp("blabla");
			//uploader.uploadFromUrlUsingUrl();
		};
	}
}