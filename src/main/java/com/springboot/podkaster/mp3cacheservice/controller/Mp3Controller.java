package com.springboot.podkaster.mp3cacheservice.controller;

import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;
import com.springboot.podkaster.mp3cacheservice.service.Mp3DetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Mp3Controller {
    private Mp3DetailsService mp3DetailsService;

    public Mp3Controller(Mp3DetailsService mp3DetailsService){
        this.mp3DetailsService = mp3DetailsService;
    }

    @GetMapping("/mp3/{sourceId}")
    public List<Mp3Details> findSong(@PathVariable int sourceId){
        System.out.println(sourceId);
        List<Mp3Details> results = mp3DetailsService.getBySourceId(sourceId);
        System.out.println(results);
        return results;
    }
}
