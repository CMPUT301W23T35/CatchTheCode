package com.example.catchthecode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class runs the search geolocation list page. (search_geo.xml)
 */


public class SearchGeoActivity extends AppCompatActivity {
    /**

     Called when the activity is starting. It initializes the activity and sets up the UI.
     @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_geo);
        //getSupportActionBar().setTitle("Geolocation Search"); // setup the Title on the top left.
        //android:id="@+id/searchLatitude" represents the latitude search bar.
        //android:id="@+id/searchLongitude" represents the longitude search bar.

        // add a button listener to the search button "@+id/searchButton"
        // pass the latitude and longitude to the MapsActivity
        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the value from those two search bars
                EditText searchLatitude = findViewById(R.id.searchLatitude);
                EditText searchLongitude = findViewById(R.id.searchLongitude);
                String stringLatitude = searchLatitude.getText().toString();
                String stringLongitude = searchLongitude.getText().toString();
                //Log.d("QR", "lat: " + stringLatitude + " lon: " + stringLongitude);
                // if those two strings are not in the correct format, throw exception
                double latitude = 0;
                double longitude = 0;
                try{
                    // convert them into double
                    latitude = Double.parseDouble(stringLatitude);
                    longitude = Double.parseDouble(stringLongitude);
                }
                catch (NumberFormatException e){
                    throw new IllegalArgumentException("Latitude and Longitude must be numbers");
                }
                Log.d("QR", "lat: " + latitude + " lon: " + longitude);
                // start the MapsActivity
                Intent intent = new Intent(SearchGeoActivity.this, MapsActivity.class);
                intent.putExtra("lat", latitude);
                intent.putExtra("lon", longitude);
                startActivity(intent);
            }
        });


        // pass them to the MapsActivity
        // MapsActivity mapsActivity = new MapsActivity();
        //mapsActivity.searchGeo(latitude, longitude);

    }
}
