package com.springboot.podkaster.mp3cacheservice;

import com.springboot.podkaster.mp3cacheservice.data.accounts.backblaze.BackblazeAccount;
import com.springboot.podkaster.mp3cacheservice.data.accounts.backblaze.BackblazeAccountDetails;
import com.springboot.podkaster.mp3cacheservice.data.dao.Mp3DetailsDao;
import com.springboot.podkaster.mp3cacheservice.service.BackblazeService;
import com.springboot.podkaster.mp3cacheservice.service.S3Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Mp3cacheserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mp3cacheserviceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(Mp3DetailsDao mp3DetailsDao) {
		return runner -> {





			//System.out.println("Database content: %s".formatted(mp3DetailsDao.getAll()));

			//S3Uploader uploader = new S3Uploader();
			//uploader.uploadFile("/home/msukno/spring-boot-course/mp3cacheservice/src/main/resources/static/test-backblaze.txt");
			//uploader.downloadFile("soundSong", "./soundSongLocal.mp3");
			//uploader.downloadFile("filename", "./mojTekst.txt");
			//uploader.uploadFromUrlUsingTemp("blabla");
			//uploader.uploadFromUrlUsingUrl();

			/*String imageUrl = "https://static.displate.com/280x392/displate/2023-01-26/598e8955611cb0d64afc80fab7242a79_b2e30fdd8340de85f15d8ae8ccb1da1b.jpg";

			String soundcloudSongUrl = "https://cf-media.sndcdn.com/PMRFQgKUwpB0.128.mp3?Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiKjovL2NmLW1lZGlhLnNuZGNkbi5jb20vUE1SRlFnS1V3cEIwLjEyOC5tcDMqIiwiQ29uZGl0aW9uIjp7IkRhdGVMZXNzVGhhbiI6eyJBV1M6RXBvY2hUaW1lIjoxNzEyNTk0MTk4fX19XX0_&Signature=AjTsk0oUenOjuk-tXmjD17IU~4UJFibZgz6T2YSgk6Nchn2cGx647a1JV-nLfcajUtxnyocv0iCcd404tuD3PTvXrN-~JLz90n8yk~zX4I2NveWi68cyVss63xggqOLDTk7CiZanJJaNVnFamde5q-rK7pS9f5nErlERP~RB2xx7pHmC8zFLp~qA9vb4Eu~iBEQOfgwju6uIs8Fn1zyu0gVnOfE-mFQU0M07X804zpFVRUK9jzsxrxeMm3Ety0j1aAMxeqNPnorfojZkI0UgLbo0JDUCNkcX3ntw4LRgGWV5drmLyi9t81emRXXJoM3grd59Vw4d0ziQRZj0iPmNYQ__&Key-Pair-Id=APKAI6TU7MMXM5DG6EPQ";
			String googleDriveVideoUrl = "https://drive.google.com/file/d/1wszA47dG-G29pZGopgdlXWY5gn9AblJt/view?usp=sharing";

			BackblazeAccount account1 = new BackblazeAccount(
					"moja-kanta",
					"https://s3.us-east-005.backblazeb2.com",
					"005b0e7311ef24c0000000002",
					"K005eJ9f1ehy0GHCro7WKdDlGxlBUjw",
					"us-east-005"
			);
			S3Service service = new BackblazeService(account1);*/
			/*service.uploadFile(
					"/home/msukno/spring-boot-course/mp3cacheservice/src/main/resources/static/test-backblaze.txt",
					"test-backblazeService");
			service.uploadUrl(googleDriveVideoUrl, "test-backblazeService-gdrive");*/
			//service.uploadUrl(googleDriveVideoUrl, "gdrive.mp4");
			//service.download("test-backblazeService-gdrive", "./gdrive.mp4");
			//service.download("bckblzFile-abc", "./bckblz-abc");
		};
	}
}