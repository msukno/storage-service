package com.springboot.podkaster.mp3cacheservice.service;

import com.springboot.podkaster.mp3cacheservice.controller.LoggingInputStream;
import com.springboot.podkaster.mp3cacheservice.data.accounts.backblaze.BackblazeAccount;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;



@Component
public class BackblazeService implements S3Service {
    BackblazeAccount account;
    S3Client client;
    S3Presigner presigner;

    RedisService redisService;
    @Autowired
    public BackblazeService(BackblazeAccount account, RedisService redisService){
        this.account = account;
        this.client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        account.accountDetails.bucketAccessKey(),
                        account.accountDetails.bucketSecretAccessKey()
                )))
                .endpointOverride(URI.create(
                        account.accountDetails.bucketEndpoint()
                ))
                .region(Region.of(account.accountDetails.region()))
                .build();

        this.presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        account.accountDetails.bucketAccessKey(),
                        account.accountDetails.bucketSecretAccessKey()
                )))
                .endpointOverride(URI.create(
                        account.accountDetails.bucketEndpoint()
                ))
                .region(Region.of(account.accountDetails.region()))
                .build();

        this.redisService = redisService;
    }

    @Override
    public void uploadFile(String url, String fileName) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(account.accountDetails.bucketName())
                .key(fileName)
                .build();

        PutObjectResponse response = client.putObject(putObjectRequest,
                RequestBody.fromFile(Paths.get(url)));


        System.out.println("File upload successful: " + response);
    }

    @Override
    public String generatePresignedUrl(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(account.accountDetails.bucketName())
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

        // Extract the URL
        URL url = presignedRequest.url();

        String urlString = url.toExternalForm();

        System.out.println("Generated presign Url: " + urlString);

        return urlString;
    }



//    @Override
//    public String uploadUrl(String url, String filename) throws IOException {
//        URL urlLink = new URL(url);
//
//        HttpURLConnection connection = (HttpURLConnection) urlLink.openConnection();
//        connection.setRequestMethod("GET");
//
//        // Ensure we get the right content length and manage resources correctly
//        connection.connect();
//        long contentLength = connection.getContentLengthLong();
//        if (contentLength == -1) {
//            throw new IOException("Cannot get content length from the URL");
//        }
//
//        try (InputStream rawStream = connection.getInputStream()) {
//            LoggingInputStream stream = new LoggingInputStream(rawStream, contentLength,filename, redisService);
//            RequestBody requestBody = RequestBody.fromInputStream(stream, stream.available());
//
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(account.accountDetails.bucketName())
//                    .contentLength(contentLength)
//                    .key(filename)
//                    .build();
//
//            PutObjectResponse response = client.putObject(putObjectRequest, requestBody);
//
//            System.out.println("File upload successful: " + response);
//        } finally {
//            connection.disconnect();
//        }
//
//        return account.filepathFromFilename(filename);
//    }

    @Override
    public String uploadUrl(String url, String sourceKey, String filename) throws IOException {
        URL urlLink = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) urlLink.openConnection();
        connection.setRequestMethod("GET");

        // Ensure we get the right content length and manage resources correctly
        connection.connect();
        long contentLength = connection.getContentLengthLong();
        if (contentLength == -1) {
            throw new IOException("Cannot get content length from the URL");
        }




        try (InputStream inputStream = urlLink.openStream();
             LoggingInputStream stream = new LoggingInputStream(inputStream, contentLength, sourceKey, redisService)) {

            RequestBody requestBody = RequestBody.fromInputStream(stream, contentLength);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(account.accountDetails.bucketName())
                    .contentLength(contentLength)
                    .key(filename)
                    .build();

            PutObjectResponse response = client.putObject(putObjectRequest, requestBody);

            System.out.println("File upload successful: " + response);
        } finally {
            connection.disconnect();
        }

        return account.filepathFromFilename(filename);
    }



    @Override
    public void download(String fileName, String filePath) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(account.accountDetails.bucketName())
                .key(fileName)
                .build();

        GetObjectResponse response = client.getObject(getObjectRequest, Paths.get(filePath));
        System.out.println("File %s download successful: ".formatted(fileName) + response);
    }

    @Override
    public ResponseInputStream<GetObjectResponse> getDownloadStream(String fileName, HttpServletRequest request) {
        String range = request.getHeader("Range");
        String backblazeFileName = account.filenameFromId(fileName);
        GetObjectRequest.Builder getObjectRequestBuilder = GetObjectRequest.builder()
                .bucket(account.accountDetails.bucketName())
                .key(backblazeFileName);

        if (range != null) {
            getObjectRequestBuilder.range(range);
        }

        GetObjectRequest getObjectRequest = getObjectRequestBuilder.build();
        ResponseInputStream<GetObjectResponse> downloadStream = client.getObject(getObjectRequest);

        return downloadStream;
    }
}