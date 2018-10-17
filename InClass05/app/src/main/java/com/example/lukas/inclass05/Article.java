package com.example.lukas.inclass05;

public class Article {
    String description, title, pub, url;

    public Article(){

    }

    @Override
    public String toString() {
        return "Article{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", pub='" + pub + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
