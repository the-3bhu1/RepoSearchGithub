package com.example.githubreposearchapp2.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "repositories")
public class RepositoryEntity {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String url;

    public RepositoryEntity(int id, String name, String description, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}