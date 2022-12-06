package com.example.projectand.pages.Main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectand.R;
import com.example.projectand.pages.Auth.LoginActivity;
import com.example.projectand.pages.Auth.RegistrationActivity;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_proceed;
    CountryCodePicker cpp;
    EditText city_home;
    TextView log_in,reg;

    ActivityResultLauncher<Intent> launcherRegistration;
    private Geocoder geoCoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        btn_proceed = findViewById(R.id.btn_country);
        btn_proceed.setOnClickListener(this);
        cpp=findViewById(R.id.country_home);
        city_home= findViewById(R.id.city_home);
        log_in=findViewById(R.id.go_login);
        log_in.setOnClickListener(this);
        reg=findViewById(R.id.go_register);
        reg.setOnClickListener(this);
        geoCoder = new Geocoder(this);

        configureLauncher();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_country){
            String full_address = cpp.getSelectedCountryName();

            String city = city_home.getText().toString();
            if (!city.isEmpty()) {
                full_address += "," + city;
            }

            Intent data = new Intent(HomeActivity.this, MapsActivity.class);
            data.putExtra("country_name", full_address);
            startActivity(data);

        } else if (view.getId() == R.id.go_register){
            Intent intent = new Intent(HomeActivity.this, RegistrationActivity.class);
            launcherRegistration.launch(intent);

        } else if (view.getId() == R.id.go_login){
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }


    public void configureLauncher() {
        launcherRegistration = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                //
            }
        });
    }
}