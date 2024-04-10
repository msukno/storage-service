package com.springboot.podkaster.mp3cacheservice.data.accounts.backblaze;

public record BackblazeAccountDetails(
        String bucketName,
        String bucketEndpoint,
        String bucketAccessKey,
        String bucketSecretAccessKey,
        String region
){}