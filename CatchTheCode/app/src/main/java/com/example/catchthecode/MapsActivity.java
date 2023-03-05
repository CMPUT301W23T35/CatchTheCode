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


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    FirebaseFirestore db;

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // try to get the location permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // add a marker to the current location
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }



        View locationButton = ((View) this.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        // click on the button every 10 seconds
        CountDownTimer mTimer = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Do nothing
            }

            public void onFinish() {
                locationButton.performClick();
                this.start();  // Restart
            }
        }.start();


        // add all QR codes stored in the database to the map
        addAllQRs(mMap);
    }

    // add all QR codes stored in the database to the map
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
                    Log.d("QR", document.getId() + " => " + document.getData());
                    if (document.get("latitude") == null || document.get("longitude") == null || document.get("score") == null) {
                        continue;
                    }
                    // get the latitude, longitude and score of the QR code, all types are String
                    String latString = document.get("latitude").toString();
                    String lonString = document.get("longitude").toString();
                    String scoreString = document.get("score").toString();
                    // convert the String types to double and int
                    double lat = Double.parseDouble(latString);
                    double lon = Double.parseDouble(lonString);
                    int score = Integer.parseInt(scoreString);
                    // add a marker to the map

                    LatLng qr = new LatLng(lat, lon);
                    addMarkerOnMap(mMap, qr, score);
                }
            }
        });
    }

    public void addMarkerOnMap(GoogleMap googleMap, LatLng qr, int score) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(qr)
                .title(String.valueOf(score)));
        Log.d("QR", "QR added");
    }

}