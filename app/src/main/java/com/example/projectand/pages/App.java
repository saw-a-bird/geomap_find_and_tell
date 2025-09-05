package com.example.projectand.pages;

import android.app.Application;

import com.example.projectand.BuildConfig;
import com.example.projectand.database.FirebaseUserHandler;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class App extends Application {
    static FirebaseUserHandler firebaseUserHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey(BuildConfig.MAPS_API_KEY)
                .setApplicationId(BuildConfig.MAPS_APP_ID)
                .setDatabaseUrl(BuildConfig.MAPS_APP_URL)
                .build();

        FirebaseApp.initializeApp(this, options);
        firebaseUserHandler = new FirebaseUserHandler();
    }
}