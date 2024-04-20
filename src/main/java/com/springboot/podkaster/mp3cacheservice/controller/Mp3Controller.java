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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class Mp3Controller {
    private Mp3DatabaseService mp3DatabaseService;
    private S3Service backblazeService;
    @Autowired
    public Mp3Controller(Mp3DatabaseService mp3DatabaseService, S3Service backblazeService){
        this.mp3DatabaseService = mp3DatabaseService;
        this.backblazeService = backblazeService;
    }

    @GetMapping("/mp3/{sourceId}")
    public Mp3Details findSong(@PathVariable String sourceId){
        Mp3Details results = mp3DatabaseService.getByKey(sourceId);
        return results;
    }

    @PostMapping("/mp3")
    public String uploadToBackblaze(@RequestBody UploadRequest request) {
        Mp3Details entry = mp3DatabaseService.getByKey(request.sourceKey());
        if(entry != null){
            return "Entry already exists";
        }
        else{
            try {
                entry = new Mp3Details(request.sourceKey(), "www.example.com");
                mp3DatabaseService.create(entry);
                String uri = backblazeService.uploadUrl(entry.getFilename(), request.url());
                entry.setUri(uri);
                mp3DatabaseService.update(entry);
                System.out.println("entry created!!");
                return "entry created: " + entry;
            }catch (Exception e){
                mp3DatabaseService.delete(entry.getSourceKey());
                return "Error while uploading URL: ";
            }
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
        String presignedUrl = backblazeService.generatePresignedUrl(result.getFilename());
        return presignedUrl;
    }
}

record UploadRequest(String sourceKey, String url){};



