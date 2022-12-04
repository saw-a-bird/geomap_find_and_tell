package com.example.projectand.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.projectand.R;
import com.example.projectand.database.FirebaseUserHandler;
import com.example.projectand.pages.Main.HomeActivity;
import com.example.projectand.pages.Main.MapsActivity;

public class LoadingActivity extends AppCompatActivity {

    FirebaseUserHandler firebaseUserHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            firebaseUserHandler = new FirebaseUserHandler();
            Intent intent;

            if (firebaseUserHandler.check()) {
                intent = new Intent(LoadingActivity.this, MapsActivity.class);
                Toast.makeText(this, "Welcome back user.", Toast.LENGTH_SHORT).show();
            } else {
                intent = new Intent(LoadingActivity.this, HomeActivity.class);
            }

            startActivity(intent);
            finish();
        }, 3000);
    }
}