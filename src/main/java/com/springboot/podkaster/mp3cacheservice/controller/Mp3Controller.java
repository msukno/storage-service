package com.springboot.podkaster.mp3cacheservice.controller;

import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;
import com.springboot.podkaster.mp3cacheservice.service.Mp3DatabaseService;
import com.springboot.podkaster.mp3cacheservice.service.S3Service;
import com.springboot.podkaster.mp3cacheservice.controller.LoggingInputStream;

import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@RestController
@RequestMapping("/api")
public class Mp3Controller {

    private Executor taskExecutor;
    private Mp3DatabaseService mp3DatabaseService;
    private S3Service backblazeService;
    @Autowired
    public Mp3Controller(Mp3DatabaseService mp3DatabaseService, S3Service backblazeService, @Qualifier("taskExecutor") Executor taskExecutor){
        this.mp3DatabaseService = mp3DatabaseService;
        this.backblazeService = backblazeService;
        this.taskExecutor = taskExecutor;
    }
    @GetMapping("/mp3/{sourceId}")
    public Mp3Details findSong(@PathVariable String sourceId){
        Mp3Details results = mp3DatabaseService.getByKey(sourceId);
        return results;
    }

    @PostMapping("/mp3")
    public ResponseEntity<String> uploadToBackblaze(@RequestBody UploadRequest request) {
        Mp3Details entry = mp3DatabaseService.getByKey(request.sourceKey());
        if (entry != null) {
            return ResponseEntity.badRequest().body("Entry already exists");
        }
            try {
                CompletableFuture.runAsync(() -> {
                    startUpload(request.url(), request.sourceKey());
                }, taskExecutor);
                return ResponseEntity.ok("Upload started for: " + request.sourceKey());
            } catch (Exception e) {
                System.out.println("Error while starting uploading URL: " + e.getMessage());
                return ResponseEntity.ok("Error while starting uploading URL: " + request.sourceKey());
            }
        }

    private void startUpload(String url, String sourceKey) {
        try {
            Mp3Details newEntry = new Mp3Details(sourceKey, url);
            String fileName =  newEntry.getFilename();
            String uploadedUrl = backblazeService.uploadUrl(url, sourceKey, fileName);
            newEntry.setUri(uploadedUrl);
            mp3DatabaseService.create(newEntry);
        } catch (Exception e) {
            System.out.println("Error while uploading URL: " + e.getMessage());
        }
    }

    @GetMapping("/listen/{sourceId}")
    public void listenToSong(@PathVariable String sourceId, HttpServletResponse response, HttpServletRequest request){
        try (ResponseInputStream<GetObjectResponse> downloadStream = backblazeService.getDownloadStream(sourceId, request)) {
            GetObjectResponse getObjectResponse = downloadStream.response();
            long contentLength = getObjectResponse.contentLength();
            String contentRange = getObjectResponse.contentRange();

            response.setContentType(getObjectResponse.contentType());
            response.setHeader("Content-Length", String.valueOf(contentLength));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Range", contentRange);

            downloadStream.transferTo(response.getOutputStream());
        } catch (ClientAbortException e){
            System.out.println("Client has disconnected!");
        } catch (IOException e) {
            System.out.println("listenToSong method, Some error occured!");
        }
    }

    @GetMapping("/presigned/{sourceKey}")
    public String getPresignedUrl(@PathVariable String sourceKey){
        Mp3Details result = mp3DatabaseService.getByKey(sourceKey);

        if(result == null){
            return null;
        }

        String presignedUrl = backblazeService.generatePresignedUrl(result.getFilename());
        return presignedUrl;
    }
}

record UploadRequest(String sourceKey, String url){};



