package com.springboot.podkaster.mp3cacheservice.data.accounts.backblaze;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BackblazeAccount {
    public BackblazeAccountDetails accountDetails;

    @Autowired
    public BackblazeAccount(
            @Value("${backblaze.bucketName}") String bucketName,
            @Value("${backblaze.bucketEndpoint}") String bucketEndpoint,
            @Value("${backblaze.bucketAccessKey}") String bucketAccessKey,
            @Value("${backblaze.bucketSecretAccessKey}") String bucketSecretAccessKey,
            @Value("${backblaze.region}") String region
    ){
        this.accountDetails = new BackblazeAccountDetails(
                bucketName,
                bucketEndpoint,
                bucketAccessKey,
                bucketSecretAccessKey,
                region
        );
    }

    public String filenameFromId(int id){
        return "bckblzFile-%s".formatted(id);
    }

    public String filepathFromId(int id) {
        String baseUri = accountDetails.bucketEndpoint()
                .replace("https://", "https://"+accountDetails.bucketName()+".");
        return baseUri + "/" + filenameFromId(id);
    }

}
