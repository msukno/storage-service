package com.springboot.podkaster.mp3cacheservice.data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mp3_details")
public class Mp3Details {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    int id;
    @Column(name="source_id")
    int sourceId;

    @Column(name="uri")
    String uri;

    public Mp3Details() {}

    public Mp3Details(int sourceId, String uri) {
        this.sourceId = sourceId;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Mp3Details{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", uri='" + uri + '\'' +
                '}';
    }
}
