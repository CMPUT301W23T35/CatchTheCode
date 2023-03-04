package com.example.catchthecode;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.catchthecode.databinding.ActivityMapsBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
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

        LatLng ualberta = new LatLng(5.3521331248E01,- 1.13521331248E02);
        mMap.addMarker(new MarkerOptions().position(ualberta).title("Marker in University of Alberta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ualberta));

    }

    // add all QR codes stored in the database to the map
    public void addAllQRs(GoogleMap googleMap) {
        mMap = googleMap;
        db.collection("QRs").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Double lat = document.getDouble("latitude");
                    Double lon = document.getDouble("longitude");
                    Long score  = document.getLong("score");
                    LatLng qr = new LatLng(lat, lon);
                    mMap.addMarker(new MarkerOptions()
                            .position(qr)
                            .title(String.valueOf(score)));
                }
            }
        });
    }
}