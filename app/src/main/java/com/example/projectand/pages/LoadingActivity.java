package com.example.projectand.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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
        setContentView(R.layout.activity_loading_page);
        Log.e("LOAD", "Ok loading...");
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (App.firebaseUserHandler.isAuthenticated()) {
                Log.e("LOAD", "Authenticated");
                Task<DataSnapshot> task = App.firebaseUserHandler.getCurrentUser();
//
//// Timeout handler (e.g., 10 seconds)
                Runnable timeoutRunnable = () -> {
                    Log.e("LOAD", "Fetching user time out...");
                };

                handler.postDelayed(timeoutRunnable, 10000); // 10 seconds timeout
                task.addOnCompleteListener(task2 -> {
                    // Cancel the timeout if task completes
                    handler.removeCallbacks(timeoutRunnable);

                    if (task2.isSuccessful()) {
                        Log.e("LOAD", "Got user");
                        try {
                            User.localizeInstance(this, new User(task2.getResult()));
                            finishedWithIntent(new Intent(LoadingActivity.this, MapsActivity.class));
                        } catch (Exception e) {
                            Log.e("LOAD", "Error localizing user", e);
                            App.firebaseUserHandler.signOut();
                            finishedDefault();
                        }
                    } else {
                        Log.e("LOAD", "Failed to get user", task2.getException());
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