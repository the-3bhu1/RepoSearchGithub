package com.example.githubreposearchapp2.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Repository implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("html_url")
    private String url;

    @SerializedName("owner")
    private Owner owner;

    // Constructor (Optional, if needed elsewhere)
    public Repository(int id, String name, String description, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.owner = owner;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Owner getOwner() {
        return owner;
    }

    // Nested Owner Class
    public static class Owner implements Serializable {
        @SerializedName("avatar_url")
        private String avatarUrl;

        @SerializedName("login")
        private String login; // Add the login field

        // Getters
        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getLogin() {
            return login;
        }

        // Setters (optional, if needed)
        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }
}