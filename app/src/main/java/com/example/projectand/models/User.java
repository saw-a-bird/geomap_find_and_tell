package com.example.projectand.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String name;
    private String familyName;
    private String email;
    private String password;
    private LatLng lastLocation;
    private Integer role = 1; // 0 for admin // 1 for user

    public User() { }

    public User(DataSnapshot data) {
        fromMap(data);
    }

    public User(String name, String family_name, String email, String password) {
        this.name = name;
        this.familyName = family_name;
        this.email = email;
        this.password = password;
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

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public LatLng getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(LatLng lastLocation) {
        this.lastLocation = lastLocation;
    }

    // MAPPING
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", id);
        result.put("name", name);
        result.put("family_name", familyName);
        result.put("email", email);
        result.put("last_location", lastLocation);
        result.put("role", role);

        return result;
    }

    public void fromMap(DataSnapshot data) {
        this.id = (String) data.child("id").getValue();
        this.name = (String) data.child("name").getValue();
        this.familyName = (String) data.child("family_name").getValue();
        this.email = (String) data.child("email").getValue();

        if (data.child("last_location").exists()) {
            HashMap<String, Double> loc = (HashMap<String, Double>) data.child("last_location").getValue();
            this.lastLocation = new LatLng(loc.get("latitude"), loc.get("longitude"));
        }

        DataSnapshot roleSnapshot = data.child("role");
        if (roleSnapshot.exists() && roleSnapshot.getValue() != null) {
            Object roleObj = roleSnapshot.getValue();
            if (roleObj instanceof Long) {
                this.role = ((Long) roleObj).intValue();
            } else if (roleObj instanceof Integer) {
                this.role = (Integer) roleObj;
            } else {
                this.role = 0; // default fallback
            }
        } else {
            this.role = 0; // or whatever default makes sense
        }

    }


    // CACHING
    public static void localizeInstance(Context context, User user) {
        SharedPreferences sharedPreferences =  context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = gson.toJson(user);
        sharedPreferences.edit().putString("current_user", json).commit();
    }

    public static User getInstance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("current_user", "");

        if (json.isEmpty()) {
            return gson.fromJson(json, User.class); // no password
        } else {
            return null;
        }
    }
}
