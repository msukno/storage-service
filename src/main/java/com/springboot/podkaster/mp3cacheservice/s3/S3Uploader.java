package com.springboot.podkaster.mp3cacheservice.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.nio.file.StandardCopyOption;

public class S3Uploader {

    String imageUrl = "https://static.displate.com/280x392/displate/2023-01-26/598e8955611cb0d64afc80fab7242a79_b2e30fdd8340de85f15d8ae8ccb1da1b.jpg";

    String soundcloudSongUrl = "https://cf-media.sndcdn.com/PMRFQgKUwpB0.128.mp3?Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiKjovL2NmLW1lZGlhLnNuZGNkbi5jb20vUE1SRlFnS1V3cEIwLjEyOC5tcDMqIiwiQ29uZGl0aW9uIjp7IkRhdGVMZXNzVGhhbiI6eyJBV1M6RXBvY2hUaW1lIjoxNzEyNTk0MTk4fX19XX0_&Signature=AjTsk0oUenOjuk-tXmjD17IU~4UJFibZgz6T2YSgk6Nchn2cGx647a1JV-nLfcajUtxnyocv0iCcd404tuD3PTvXrN-~JLz90n8yk~zX4I2NveWi68cyVss63xggqOLDTk7CiZanJJaNVnFamde5q-rK7pS9f5nErlERP~RB2xx7pHmC8zFLp~qA9vb4Eu~iBEQOfgwju6uIs8Fn1zyu0gVnOfE-mFQU0M07X804zpFVRUK9jzsxrxeMm3Ety0j1aAMxeqNPnorfojZkI0UgLbo0JDUCNkcX3ntw4LRgGWV5drmLyi9t81emRXXJoM3grd59Vw4d0ziQRZj0iPmNYQ__&Key-Pair-Id=APKAI6TU7MMXM5DG6EPQ";
    String googleDriveVideoUrl = "https://drive.google.com/file/d/1wszA47dG-G29pZGopgdlXWY5gn9AblJt/view?usp=sharing";
    String bucketName = "moja-kanta";
    String filename = "filename";
    String accessKey = "005b0e7311ef24c0000000002";
    String secretAccessKey = "K005eJ9f1ehy0GHCro7WKdDlGxlBUjw";
    URI uri = URI.create("https://s3.us-east-005.backblazeb2.com");
    Region region = Region.of("us-east-005");
    private S3Client s3Client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretAccessKey)))
            .endpointOverride(uri) // Replace with your Backblaze endpoint
            .region(region) // Use a dummy region for S3-compatible services
            .build();

    public void uploadFile(String filePath) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest,
                RequestBody.fromFile(Paths.get(filePath)));


        System.out.println("File upload successful: " + response);
    }

    public void downloadFile(String filename, String filePath) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
             OutputStream outputStream = new FileOutputStream(filePath)) {

            byte[] buffer = new byte[1024];
            int readLen;
            while ((readLen = s3Object.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readLen);
            }
            System.out.println("File downloaded successfully: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Stream finished");
    }

    public void uploadFromUrlUsingTemp(String filename) {
        try {
            URL url = new URL(googleDriveVideoUrl);
            Path tempFile = Files.createTempFile("temp", ".mp4");
            try (InputStream stream = url.openStream()) {
                Files.copy(stream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    RequestBody.fromFile(tempFile));

            System.out.println("File upload successful: " + response);

            Files.delete(tempFile); // delete the temporary file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //17674 bytes

    public void uploadFromUrlUsingUrl() throws IOException {
        URL url = new URL(soundcloudSongUrl);
        //URL url = new URL(imageUrl);

        String fileName = "soundSong";

        //change this into a GET request to get teh content length
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        long contentLength = connection.getContentLengthLong();


        try (InputStream stream = url.openStream()){

            RequestBody requestBody = RequestBody.fromInputStream(
                    stream,
                    contentLength
            );

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    requestBody
            );

            System.out.println("File upload successful: " + response);
        } catch (IOException e) {
            System.out.println("File upload failed");
            e.printStackTrace();
        }
    }
}
