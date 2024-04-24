package com.springboot.podkaster.mp3cacheservice.service;

import jakarta.servlet.http.HttpServletRequest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface S3Service {
    void uploadFile(String filePath, String fileName);

    String uploadUrl(String url, String sourceId, String fileName) throws IOException;

    void download(String fileName, String filePath);

    String generatePresignedUrl(String fileName);

    ResponseInputStream<GetObjectResponse> getDownloadStream(String fileName, HttpServletRequest request);

}
