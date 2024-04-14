package com.springboot.podkaster.mp3cacheservice.service;

import jakarta.servlet.http.HttpServletRequest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

public interface S3Service {
    void uploadFile(String filePath, String fileName);
    String uploadUrl(String sourceId, String url) throws IOException;
    void download(String fileName, String filePath);

    ResponseInputStream<GetObjectResponse> getDownloadStream(String fileName, HttpServletRequest request);

}
