package com.springboot.podkaster.mp3cacheservice.controller;

import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;
import com.springboot.podkaster.mp3cacheservice.service.Mp3DatabaseService;
import com.springboot.podkaster.mp3cacheservice.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public List<Mp3Details> findSong(@PathVariable int sourceId){
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
}

record UploadRequest(int sourceId, String url){};
