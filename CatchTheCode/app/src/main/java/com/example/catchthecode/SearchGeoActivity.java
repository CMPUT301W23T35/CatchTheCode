package com.example.catchthecode;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class runs the search geolocation list page. (search_geo.xml)
 */
public class SearchGeoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_geo);
        getSupportActionBar().setTitle("Geolocation Search"); // setup the Title on the top left.
        //initViews();
    }
}
