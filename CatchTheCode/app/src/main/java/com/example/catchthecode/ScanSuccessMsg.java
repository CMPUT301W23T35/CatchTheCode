package com.example.catchthecode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import android.widget.TextView;
import android.location.Location;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

// This class runs the success message after scanning.(scan_success.xml)
/**
 * represent the scan success message
 */
public class ScanSuccessMsg extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient client;
    LocationManager locationManager;
    String latitude;
    String longitude;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_success);
        Button saveLocationButton = (Button) findViewById(R.id.save_location_button);
        client = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();
        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ScanSuccessMsg.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    return;
                }
                LocationRequest mLocationRequest = LocationRequest.create();
                mLocationRequest.setInterval(60000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationCallback mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            return;
                        }
                        for (Location location : locationResult.getLocations()) {
                            if (location != null) {
                                //TODO: UI updates.
                            }
                        }
                    }
                };
                LocationServices.getFusedLocationProviderClient(getBaseContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                client.getLastLocation().addOnSuccessListener(ScanSuccessMsg.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = Double.toString(location.getLatitude());
                            longitude = Double.toString(location.getLongitude());
                            Log.d("latitude", latitude);
                            Log.d("longitude", latitude);
                            // can use this geolocation information


                        } else {
                            Log.d("failed", "0");
                        }
                    }

                });
            }
        });
//        ActivityCompat.requestPermissions( this,
//                new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//        saveLocationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("a", "hello");
//
//                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    OnGPS();
//                } else {
//                    getLocation();
//                }
//                // have latitude and longitude now
//                Log.d("location", latitude);
//                Log.d("location", longitude);
//            }
//        });

    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                ScanSuccessMsg.this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ScanSuccessMsg.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
