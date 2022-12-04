package com.example.projectand.pages.Main;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.example.projectand.R;
import com.example.projectand.database.FirebaseUserHandler;
import com.example.projectand.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.projectand.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ActivityMapsBinding binding;
    private FirebaseUserHandler firebaseUserHandler;

    private Geocoder geoCoder;
    private GoogleMap googleMap;

    String country_name;
    User user;
    List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        firebaseUserHandler = new FirebaseUserHandler();
        Task<DataSnapshot> task = firebaseUserHandler.getCurrentUser();
        if (task != null) {
            task.addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                    user = new User(task2.getResult());
                    country_name = user.getAdresse();
                    mapFragment.getMapAsync(this);
                }
            });

        } else {
            if (this.getIntent().getExtras() != null) {
                country_name = this.getIntent().getExtras().getString("country_name");
            } else {
                country_name = "Tunisia";
            }

            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap gMap) {
        geoCoder = new Geocoder(this);
        googleMap = gMap;
        LatLng country = null;

        try {
            addressList = geoCoder.getFromLocationName(country_name,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i =0; i<addressList.size();i++){
            Address userAddress = addressList.get(i);
            country = new LatLng(userAddress.getLatitude(),userAddress.getLongitude());
        }
        // Add a marker in Sydney and move the camera
        Toast.makeText(this, country_name, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(country), Toast.LENGTH_SHORT).show();
        googleMap.addMarker(new MarkerOptions().position(country).title(country_name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(country,12));

        if (this.getIntent().hasExtra("user")){
            Toast.makeText(this, this.getIntent().getExtras().getString("user"), Toast.LENGTH_SHORT).show();
        }
    }
}