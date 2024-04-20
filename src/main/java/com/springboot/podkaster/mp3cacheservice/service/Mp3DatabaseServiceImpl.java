package com.springboot.podkaster.mp3cacheservice.service;

import com.springboot.podkaster.mp3cacheservice.data.dao.Mp3DetailsDao;
import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class Mp3DatabaseServiceImpl implements Mp3DatabaseService {
    private Mp3DetailsDao mp3DetailsDao;

    @Autowired
    public Mp3DatabaseServiceImpl(Mp3DetailsDao mp3DetailsDao){
        this.mp3DetailsDao = mp3DetailsDao;
    }

    @Override
    @Transactional
    public void create(Mp3Details mp3Details) {
        mp3DetailsDao.create(mp3Details);
    }


    @Override
    public Mp3Details getByKey(String key) {
        return mp3DetailsDao.getByKey(key);
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
    public void delete(String sourceKey) {
        mp3DetailsDao.delete(sourceKey);
    }

    @Override
    @Transactional
    public int deleteAll() {
        return mp3DetailsDao.deleteAll();
    }
}
