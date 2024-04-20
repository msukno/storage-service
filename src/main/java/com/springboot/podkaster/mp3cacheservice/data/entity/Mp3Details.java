package com.springboot.podkaster.mp3cacheservice.data.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "mp3_details")
public class Mp3Details {

    @Id
    @Column(name = "source_key", nullable = false, unique = true)
    private String sourceKey;


    @Column(name = "filename", nullable = false, unique = true)
    private String filename;

    @Column(name = "uri", nullable = false)
    private String uri; // user generated

    public Mp3Details() {
    }

    public Mp3Details(String sourceKey, String uri) {
        this.sourceKey = sourceKey;
        this.uri = uri;
        this.filename = UUID.randomUUID().toString();
    }


    public String getSourceKey() {
        return sourceKey;
    }

    public String getFilename() {
        return filename;
    }

    public String getUri() {
        return uri;
    }
    public String setUri(String uri) {
        return this.uri = uri;
    }

    @Override
    public String toString() {
        return "Mp3Details{" +
                "sourceKey=" + sourceKey +
                ", filename=" + filename +
                ", uri='" + uri + '\'' +
                '}';
    }
}
