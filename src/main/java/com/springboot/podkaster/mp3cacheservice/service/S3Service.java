package com.springboot.podkaster.mp3cacheservice.service;

public interface S3Service {
    void uploadFromFile(String filepath);
}

class S3ServiceBackblaze implements S3Service {

    @Override
    public void uploadFromFile(String filepath) {
    }
}
