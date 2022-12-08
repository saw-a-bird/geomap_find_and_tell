package com.example.projectand.models;

import com.google.firebase.database.DataSnapshot;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Category {
    private String id;
    private String name;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(DataSnapshot data) {
        this.id = (String) data.child("id").getValue();
        this.name = (String) data.child("name").getValue();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);

        return result;
    }
}
