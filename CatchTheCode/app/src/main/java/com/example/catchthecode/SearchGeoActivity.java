package com.example.catchthecode;

import android.os.Bundle;

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
        getSupportActionBar().setTitle("Geolocation Search"); // setup the Title on the top left.
        //initViews();
    }
}
