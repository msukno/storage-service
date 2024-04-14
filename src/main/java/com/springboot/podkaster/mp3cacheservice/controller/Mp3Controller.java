package com.springboot.podkaster.mp3cacheservice.controller;

import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;
import com.springboot.podkaster.mp3cacheservice.service.Mp3DatabaseService;
import com.springboot.podkaster.mp3cacheservice.service.S3Service;
import com.springboot.podkaster.mp3cacheservice.controller.LoggingInputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    public List<Mp3Details> findSong(@PathVariable String sourceId){
        List<Mp3Details> results = mp3DatabaseService.getBySourceId(sourceId);
        return results;
    }

    @PostMapping("/mp3")
    public String uploadToBackblaze(@RequestBody UploadRequest request) throws IOException {
        String uri = backblazeService.uploadUrl(request.sourceId(), request.url());
        mp3DatabaseService.create(new Mp3Details(request.sourceId(), uri));
        System.out.println("entry created!!");
        return "sourceId: %s, url: %s".formatted(request.sourceId(), request.url());
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
            System.out.println("Some error occured!");
        }
    }


    /*@GetMapping(value = "/stream/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public StreamingResponseBody streamFile(@PathVariable String fileName) {
        return outputStream -> {
            int bytesRead;
            byte[] buffer = new byte[1024];
            InputStream inputStream = backblazeService.getDownloadStream(fileName);
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        };
    }*/
}

record UploadRequest(String sourceId, String url){};



