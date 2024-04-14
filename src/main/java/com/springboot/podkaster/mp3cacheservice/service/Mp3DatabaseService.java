package com.springboot.podkaster.mp3cacheservice.service;

import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;

import java.util.List;

public interface Mp3DatabaseService {
    void create(Mp3Details mp3Details);
    List<Mp3Details> getBySourceId(String sourceId);
    List<Mp3Details> getAll();
    void update(Mp3Details mp3Details);
    void delete(String sourceId);
    int deleteAll();
}
