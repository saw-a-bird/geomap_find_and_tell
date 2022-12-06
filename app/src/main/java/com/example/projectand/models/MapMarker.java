package com.example.projectand.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapMarker {
    private String id;
    private LatLng location;
    private Timestamp timeCreation;
    private int categoryId;
    private String creatorId;
    private String remark;

    public MapMarker() {
    }

    public MapMarker(DataSnapshot data) {
        this.location = (LatLng) data.child("location").getValue();
        this.timeCreation = (Timestamp) data.child("timeCreation").getValue();
        this.remark = (String) data.child("remark").getValue();
        this.creatorId = (String) data.child("creatorId").getValue();
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Timestamp getTimeCreation() {
        return timeCreation;
    }

    public void setTimeCreation(Timestamp timeCreation) {
        this.timeCreation = timeCreation;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("location", location);
        result.put("timeCreation", timeCreation);
        result.put("creatorId", creatorId);
        result.put("remark", remark);

        return result;
    }
}
