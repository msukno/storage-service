package com.springboot.podkaster.mp3cacheservice.data.dao;

import com.springboot.podkaster.mp3cacheservice.data.entity.Mp3Details;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

public interface Mp3DetailsDao {
    void create(Mp3Details mp3Details);
    Mp3Details getByKey(String key);
    List<Mp3Details> getAll();
    void update(Mp3Details mp3Details);
    void delete(String key);
    int deleteAll();
}

@Repository
class Mp3DetailsDaoImpl implements Mp3DetailsDao{

    private EntityManager entityManager;

    @Autowired
    public Mp3DetailsDaoImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void create(Mp3Details mp3Details) { entityManager.persist(mp3Details); }

    @Override
    public Mp3Details getByKey(String key) {
        return entityManager.find(Mp3Details.class, key);
    }

    @Override
    public List<Mp3Details> getAll() {
        TypedQuery<Mp3Details> theQuery = entityManager.createQuery("FROM Mp3Details", Mp3Details.class);
        return theQuery.getResultList();
    }

    @Override
    public void update(Mp3Details mp3Details) { entityManager.merge(mp3Details); }

    @Override
    public void delete(String key) {
        Mp3Details mp3Details = entityManager.find(Mp3Details.class, key);
        entityManager.remove(mp3Details);
    }

    @Override
    public int deleteAll() {
        int numRowsDeleted = entityManager.createQuery("DELETE FROM Mp3Details").executeUpdate();
        return numRowsDeleted;
    }
}
