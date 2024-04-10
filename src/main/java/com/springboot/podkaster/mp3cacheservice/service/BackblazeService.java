package com.springboot.podkaster.mp3cacheservice.service;

import com.springboot.podkaster.mp3cacheservice.data.accounts.backblaze.BackblazeAccount;
import com.springboot.podkaster.mp3cacheservice.data.dao.Mp3DetailsDao;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;

@Component
public class BackblazeService implements S3Service {
    BackblazeAccount account;
    S3Client client;
    @Autowired
    public BackblazeService(BackblazeAccount account){
        this.account = account;
        client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        account.accountDetails.bucketAccessKey(),
                        account.accountDetails.bucketSecretAccessKey()
                )))
                .endpointOverride(URI.create(
                        account.accountDetails.bucketEndpoint()
                ))
                .region(Region.of(account.accountDetails.region()))
                .build();
    }
    @Override
    public void uploadFile(String filePath, String fileName) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(account.accountDetails.bucketName())
                .key(fileName)
                .build();

        PutObjectResponse response = client.putObject(putObjectRequest,
                RequestBody.fromFile(Paths.get(filePath)));


        System.out.println("File upload successful: " + response);
    }

    @Override
    public String uploadUrl(int sourceId, String url) throws IOException {
        String fileName = account.filenameFromId(sourceId);
        URL urlLink = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) urlLink.openConnection();
        connection.setRequestMethod("HEAD");
        long contentLength = connection.getContentLengthLong();

        try (InputStream stream = urlLink.openStream()){

            RequestBody requestBody = RequestBody.fromInputStream(
                    stream,
                    contentLength
            );

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(account.accountDetails.bucketName())
                    .key(fileName)
                    .build();

            PutObjectResponse response = client.putObject(
                    putObjectRequest,
                    requestBody
            );

            System.out.println("File upload successful: " + response);

        } catch (IOException e) {
            System.out.println("File upload failed");
            e.printStackTrace();
        }
        return account.filepathFromId(sourceId);
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
}