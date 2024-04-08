package com.springboot.podkaster.mp3cacheservice.service;

import com.springboot.podkaster.mp3cacheservice.data.dao.Mp3DetailsDao;
import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Mp3DetailsService {
    void create(Mp3Details mp3Details);
    List<Mp3Details> getBySourceId(int sourceId);
    List<Mp3Details> getAll();
    void update(Mp3Details mp3Details);
    void delete(int sourceId);
    int deleteAll();
}

@Component
class Mp3DetailsServiceImpl implements Mp3DetailsService{
    private Mp3DetailsDao mp3DetailsDao;

    @Autowired
    public Mp3DetailsServiceImpl(Mp3DetailsDao mp3DetailsDao){
        this.mp3DetailsDao = mp3DetailsDao;
    }

    @Override
    @Transactional
    public void create(Mp3Details mp3Details) {
        mp3DetailsDao.create(mp3Details);
    }

    @Override
    public List<Mp3Details> getBySourceId(int sourceId) {
        return mp3DetailsDao.getBySourceId(sourceId);
    }

    @Override
    public List<Mp3Details> getAll() {
        return mp3DetailsDao.getAll();
    }

    @Override
    @Transactional
    public void update(Mp3Details mp3Details) {
        mp3DetailsDao.update(mp3Details);
    }

    @Override
    @Transactional
    public void delete(int sourceId) {
        mp3DetailsDao.delete(sourceId);
    }

    @Override
    @Transactional
    public int deleteAll() {
        return mp3DetailsDao.deleteAll();
    }
}
