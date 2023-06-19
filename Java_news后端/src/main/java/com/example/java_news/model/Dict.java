package com.example.java_news.model;

import java.sql.Timestamp;

public class Dict {
    private  int id;
    private String word;
    private Timestamp created_at;

    public Dict(int id, String word, Timestamp created_at) {
        this.id = id;
        this.word = word;
        this.created_at = created_at;
    }

    public Dict(String word, Timestamp created_at) {
        this.word = word;
        this.created_at = created_at;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }


}
