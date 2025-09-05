package com.example.projectand.pages;

import android.app.Application;

import com.example.projectand.database.FirebaseUserHandler;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class App extends Application {
    static FirebaseUserHandler firebaseUserHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyC4dYuV_o-yilHWY2BH26XT0y1zyACkJF4")
                .setApplicationId("project-and-74274")
                .setDatabaseUrl("https://project-and-74274-default-rtdb.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(this, options);
        firebaseUserHandler = new FirebaseUserHandler();
    }
}