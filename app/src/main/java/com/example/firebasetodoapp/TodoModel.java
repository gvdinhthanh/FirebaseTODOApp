package com.example.firebasetodoapp;

import java.util.HashMap;

public class TodoModel {
    private String id;
    private String title;
    private String content;
    public TodoModel(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public TodoModel(String title, String content) {
        this.id = null;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap<String, Object> toJson() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        return result;
    }
}
