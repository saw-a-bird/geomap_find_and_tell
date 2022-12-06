package com.example.projectand.pages.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.projectand.R;
import com.example.projectand.database.FirebaseCategoryHandler;
import com.example.projectand.database.FirebaseMapMarkerHandler;
import com.example.projectand.database.FirebaseUserHandler;
import com.example.projectand.models.User;
import com.example.projectand.pages.modals.CountryDialogFragment;
import com.example.projectand.utils.AddressGetter;
import com.example.projectand.utils.BetterActivityResult;
import com.example.projectand.utils.InternetConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnMapLongClickListener, OnCameraMoveListener, CountryDialogFragment.DialogListener, AddressGetter.OnAddressGetterFinished {

    DialogFragment dialog;
    private FirebaseUserHandler firebaseUserHandler;
    private FirebaseMapMarkerHandler firebaseMapMarkerHandler;

    private Geocoder geoCoder;
    private GoogleMap googleMap;
    private Boolean setMode = false;

    private Boolean isAuthenticated = false;
    private User currentUser;

    private boolean waitingForPermission = false;
    private LatLng currentLocation;

    private final String DEFAULT_COUNTRY ="Tunisia, Sousse";
    private Boolean appearSearch = false;
    private PowerSpinnerView searchBar;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_maps);
        } catch (Exception e) {
            Toast.makeText(this, "Sorry, an exception occurred. Please clean your cache.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(MapsActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        firebaseUserHandler = new FirebaseUserHandler();
        firebaseMapMarkerHandler = new FirebaseMapMarkerHandler();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        searchBar = (PowerSpinnerView) findViewById(R.id.search_text_bar);
        loadSearch();

        fab = findViewById(R.id.markerBtn);
        fab.setOnClickListener(view -> {
            if (isAuthenticated) {
                setMode = !setMode;
                if (setMode) {
                    fab.setBackgroundTintList(ContextCompat.getColorStateList(MapsActivity.this, R.color.flag_enabled));
                    Toast.makeText(this, "Select a place on the map.", Toast.LENGTH_SHORT).show();
                } else {
                    fab.setBackgroundTintList(ContextCompat.getColorStateList(MapsActivity.this, R.color.flag));
                }
                fab.refreshDrawableState();

            } else {
                Toast.makeText(this, "Please authenticate first!", Toast.LENGTH_SHORT).show();
            }
        });

        isAuthenticated = firebaseUserHandler.isAuthenticated();
        if (isAuthenticated) {
            fab.setBackgroundTintList(ContextCompat.getColorStateList(MapsActivity.this, R.color.flag));
            currentUser = User.getInstance(this);
            if (currentUser == null) {
                firebaseUserHandler.getCurrentUser().addOnSuccessListener(result -> {
                    currentUser = new User(result);
                    User.localizeInstance(this, new User(result));
                    loadMap(); // ok 2
                });
            } else {
                loadMap(); // ok
            }
        } else {
            loadMap(); // not ok
            fab.setBackgroundTintList(ContextCompat.getColorStateList(MapsActivity.this, R.color.flag_disabled));
            fab.refreshDrawableState();
        }
    }

    /*
        ACTION MENU
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.action_search:
                triggerSearchBar();
                break;
            case R.id.action_goto:
//                Log.e("location: ", locationAddress);
                dialog = new CountryDialogFragment(this, geoCoder);
                dialog.show(getSupportFragmentManager(), "country");
                break;
            case R.id.action_cloc:
                if (currentLocation != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                else
                    Toast.makeText(this, "Please accept the location permission first!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_logout:
                firebaseUserHandler.signOut();
                intent = new Intent(MapsActivity.this, HomeActivity.class);
                startActivity(intent);
                break;

        }

        return true;
    }

    /*
        LOADING MAP
     */
    public void loadMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        geoCoder = new Geocoder(this);
        googleMap = gMap;
        googleMap.setOnMapLongClickListener(this);
        if (currentUser != null) {
            googleMap.setOnCameraMoveListener(this);
        }

        // GETTING CURRENT LOCATION AND SETTING MAIN MARKER

        LatLng location = null;

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            } else {
                location = setCurrentLocation(locationManager);
            }
        }


        // MOVING CAMERA
        if (currentUser != null) {
            if (currentUser.getLastLocation() != null) {
                // move map to user location
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser.getLastLocation(), 12));
            } else {
                // wait for response
                if (location == null) {
                    waitingForPermission = true;
                } else {
                    // move to location
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
                    // save position
                    firebaseUserHandler.saveLocation(location);
                }
            }
        } else {
            Bundle extras = this.getIntent().getExtras();
            String country_name;
            if (extras != null && extras.containsKey("country_name")) {
                country_name = extras.getString("country_name");
            } else {
                country_name = DEFAULT_COUNTRY;
            }

            new AddressGetter(geoCoder, this).execute(country_name);
        }


        // SETTING OTHER MARKERS
        if (currentUser != null) {
            getAllMarkers(currentUser.getFavouriteCategory());
        } else {
            getAllMarkers(1);
        }
    }


    // CURRENT LOCATION
    public LatLng setCurrentLocation(LocationManager locationManager) {
        Location cLocation = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (cLocation != null) {
            currentLocation = new LatLng(cLocation.getLatitude(), cLocation.getLongitude());
            Log.e("Location :", "current location");

            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current location"));
            if (waitingForPermission) {
                // move to location
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            }

            return currentLocation;
        }

        return null;
    }

    // SET COUNTRY
    public void setCountry(String location, Address address) {
        Toast.makeText(this, location, Toast.LENGTH_SHORT).show();
        LatLng lng = new LatLng(address.getLatitude(), address.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lng, 12));
    }

    // GET ALL MARKERS
    public void getAllMarkers(int categoryId) {
        firebaseMapMarkerHandler.getAll(categoryId);

//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                Post post = dataSnapshot.getValue(Post.class);
//                // ..
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        mPostReference.addValueEventListener(postListener);

//        LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
//            googleMap.addMarker(new MarkerOptions().position(location).title(locationAddress));
    }

    /*

     */
    @Override
    public void onMapLongClick(@NonNull LatLng place) {
        Log.e("location", "clicked");

        if (setMode) {
            setMode = false;
            fab.setBackgroundTintList(ContextCompat.getColorStateList(MapsActivity.this, R.color.flag));
            // go to details activity with result
        }
    }

    private void triggerSearchBar() {
        Toast.makeText(this, "Triggered.", Toast.LENGTH_SHORT).show();

        float translationY = !appearSearch ? 130f : 0;
        appearSearch = !appearSearch;

        searchBar.animate()
                .translationY(translationY)
                .setDuration(1000)
                .start();
    }

    private void loadSearch() {
        // TODO: get categories from firebase
        FirebaseCategoryHandler firebaseCategoryHandler = new FirebaseCategoryHandler();
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<IconSpinnerItem> itemArray = firebaseCategoryHandler.getAllTemporarily();

        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(searchBar);
        searchBar.setSpinnerAdapter(iconSpinnerAdapter);
        searchBar.setItems(itemArray);
        searchBar.selectItemByIndex(0);
        searchBar.setLifecycleOwner(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.e("Location", String.valueOf(requestCode));
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
                setCurrentLocation(locationManager);
            } else {
                Toast.makeText(getBaseContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDialogValidated(DialogFragment dialog, String locationAddress, Address address) {
        setCountry(locationAddress, address);
        Log.e("Location", "set to new");
    }

    @Override
    public void onAddressGetterFinished(String result, Address address) {
        if (address != null) {
            setCountry(result, address);
            System.out.println(result+": "+address.getLatitude()+", "+address.getLongitude());
        } else {
            // default address
        }
    }

    @Override
    public void onCameraMove() {
        LatLng newLocation = new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
        firebaseUserHandler.saveLocation(newLocation);
        Log.e("Location : ","moved");
    }
}