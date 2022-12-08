package com.example.projectand.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapMarker {
    private String id;

    private String country;
    private LatLng location;
    private Instant timeCreation;
    private String categoryId;
    private String creatorId;
    private String remark;

    private Marker marker;


    public MapMarker() {
    }

    public MapMarker(DataSnapshot data) {
        this.id = (String) data.child("id").getValue();
        this.country = (String) data.child("country").getValue();
        this.timeCreation = Instant.parse((String) data.child("timeCreation").getValue());
        this.remark = (String) data.child("remark").getValue();
        this.creatorId = (String) data.child("creatorId").getValue();
        this.categoryId = ((String) data.child("categoryId").getValue());

        if (data.child("location").exists()) {
            HashMap<String, Double> loc = (HashMap<String, Double>) data.child("location").getValue();
            this.location = new LatLng(loc.get("latitude"), loc.get("longitude"));
        }
    }

    public MapMarker(String creatorId, Instant timeCreation, LatLng location, String country, String categoryId, String remark) {
        this.location = location;
        this.country = country;
        this.timeCreation = timeCreation;
        this.categoryId = categoryId;
        this.creatorId = creatorId;
        this.remark = remark;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Instant getTimeCreation() {
        return timeCreation;
    }

    public void setTimeCreation(Instant timeCreation) {
        this.timeCreation = timeCreation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("creatorId", creatorId);
        result.put("country", country);
        result.put("location", location);

        timeCreation = Instant.now();
        result.put("timeCreation", timeCreation.toString());
        result.put("categoryId", categoryId);
        result.put("remark", remark);

        return result;
    }
}
