package com.springboot.podkaster.mp3cacheservice.service;

import java.io.IOException;

public interface S3Service {
    void uploadFile(String filePath, String fileName);
    String uploadUrl(int sourceId, String url) throws IOException;
    void download(String fileName, String filePath);
}
