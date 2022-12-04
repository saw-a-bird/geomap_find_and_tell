package com.example.projectand.models;

import androidx.annotation.NonNull;

import com.example.projectand.database.ResultCodes;
import com.google.firebase.database.DataSnapshot;

public class User {
    private String uid;
    private String name;
    private String familyName;
    private String adresse;
    private String email;
    private String password;

    public User() {
    }

    public User(DataSnapshot data) {
        this.uid = (String) data.child("uid").getValue();
        this.name = (String) data.child("name").getValue();
        this.familyName = (String) data.child("familyName").getValue();
        this.adresse = (String) data.child("adresse").getValue();
        this.email = (String) data.child("email").getValue();
        this.password = (String) data.child("password").getValue();
    }

    public User(String name, String familyName, String adresse, String email, String password) {
        this.name = name;
        this.familyName = familyName;
        this.adresse = adresse;
        this.email = email;
        this.password = password;
    }

    public String getUId() {
        return uid;
    }

    public void setUId(String uid) {
        this.uid = uid;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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

    @NonNull
    @Override
    public String toString() {
        return "Name : " + this.name+
                "\n Family Name : "+ this.familyName+
                "\n Address : "+this.adresse+
                "\n Email : "+this.email+
                "\n Password : "+this.password+
                "\n ******";
    }
}
