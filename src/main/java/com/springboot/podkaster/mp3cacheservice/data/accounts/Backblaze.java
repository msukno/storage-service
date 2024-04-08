package com.springboot.podkaster.mp3cacheservice.data.accounts;

import software.amazon.awssdk.regions.Region;

import java.net.URI;

public record Backblaze(
        String bucketName,
        String accessKey,
        String secretAccessKey,
        Region region
){}
/*    String songUrl = "https://drive.google.com/file/d/1wszA47dG-G29pZGopgdlXWY5gn9AblJt/view?usp=sharing";
    String bucketName = "moja-kanta";
    String filename = "filename";
    String accessKey = "005b0e7311ef24c0000000002";
    String secretAccessKey = "K005eJ9f1ehy0GHCro7WKdDlGxlBUjw";
    URI uri = URI.create("https://s3.us-east-005.backblazeb2.com");
    Region region = Region.of("us-east-005");*/

