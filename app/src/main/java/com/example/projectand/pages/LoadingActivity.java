package com.example.projectand.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.projectand.R;
import com.example.projectand.models.User;
import com.example.projectand.pages.Main.HomeActivity;
import com.example.projectand.pages.Main.MapsActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase immediately
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_loading_page);


        Handler handler = new Handler();
        handler.postDelayed(() -> {

            if (App.firebaseUserHandler.isAuthenticated()) {
                Task<DataSnapshot> task = App.firebaseUserHandler.getCurrentUser();

                task.addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        try {
                            System.out.println(task2.getResult());
                            User.localizeInstance(this, new User(task2.getResult()));
                            finishedWithIntent(new Intent(LoadingActivity.this, MapsActivity.class));
                        } catch (Exception e) {
                            App.firebaseUserHandler.signOut();
                            finishedDefault();
                        }
                    } else {
                        App.firebaseUserHandler.signOut();
                        finishedDefault();
                    }
                });
            } else {
                finishedDefault();
            }
        }, 2000);
    }

    public void finishedDefault() {
        Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void finishedWithIntent(Intent intent) {
        startActivity(intent);
        finish();
    }
}