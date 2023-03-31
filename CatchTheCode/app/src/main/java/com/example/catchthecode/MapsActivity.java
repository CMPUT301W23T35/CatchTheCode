
package com.example.catchthecode;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.catchthecode.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 This class is used to display a map and add markers to it.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private GoogleMap gMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    FirebaseFirestore db;

    /**
     * This method is called when the activity is created, creating the map.
     *
     * @param savedInstanceState a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * This method is called when the map is ready to be used.
     *
     * @param googleMap the map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gMap = googleMap;
        // try to get the location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // add a marker to the current location
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }



        View locationButton = ((View) this.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

        // get Intent extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // get the latitude and longitude from the intent
            Double lat = extras.getDouble("lat");
            Double lon = extras.getDouble("lon");
            // log the lat and lon
            Log.d("QR", "lat: " + lat + " lon: " + lon);
            // search the map for nearby QR codes
            searchGeo(lon, lat);
        }
        // click the location button after 5 seconds
        else{
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                locationButton.performClick();
            }
            }, 5000);
        }

        // add all QR codes stored in the database to the map
        addAllQRs(mMap);
    }

    /**
     * This method is used to add all QR codes stored in the database to the map.
     *
     * @param googleMap the map
     */
    public void addAllQRs(GoogleMap googleMap) {
        mMap = googleMap;
        db.collection("QRs").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // throw exception if the document does not contain the required fields
//                    if (!document.contains("latitude") || !document.contains("longitude") || !document.contains("score")) {
//                        continue;
//                    }
                    // if the object is null, continue
                    // Log.d("QR", document.getId() + " => " + document.getData());
                    if (document.get("latitude") == null || document.get("longitude") == null || document.get("score") == null) {
                        continue;
                    }
                    // get the latitude, longitude and score of the QR code, all types are String
                    String latString = document.get("latitude").toString();
                    String lonString = document.get("longitude").toString();
                    String scoreString = document.get("score").toString();
                    // if there is no location stored in the database, noLat and noLon
                    if(latString.equals("noLat") || lonString.equals("noLon")) {
                        continue;
                    }
                    // convert the String types to double and int
                    double lat = Double.parseDouble(latString);
                    double lon = Double.parseDouble(lonString);
                    int score = Integer.parseInt(scoreString);
                    // log the lat, lon and score
                    Log.d("QR", "lat: " + lat + " lon: " + lon + " score: " + score);
                    // add a marker to the map
                    LatLng qr = new LatLng(lat, lon);
                    addMarkerOnMap(mMap, qr, score);
                }
            }
        });
    }

    /** This method is used to add a marker to the map
     * @param googleMap the map
     * @param qr the location of the QR code
     * @param score the score of the QR code
     */
    public void addMarkerOnMap(GoogleMap googleMap, LatLng qr, int score) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(qr)
                .title(String.valueOf(score)));
        Log.d("QR", "QR added");
    }

    // based on the location, show the map for nearby QR codes
    // used the existing gMap object, if it is not initialized, initialize it
    public void searchGeo(double lon, double lat){
        // initialize the gMap object with save
        LatLng currentLocation = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }

}