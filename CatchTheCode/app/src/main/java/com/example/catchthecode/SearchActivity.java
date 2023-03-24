package com.example.catchthecode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**

 The SearchActivity class extends AppCompatActivity and overrides onCreate to create the activity

 and set the content view to the searching_page layout. It also initializes the views and connects

 the button listeners.
 */
public class SearchActivity extends AppCompatActivity {
    /**

     Overrides the onCreate method to create the activity and set the content view to the searching_page layout.
     It also calls the initViews method to initialize the views and connect the button listeners.
     @param savedInstanceState a Bundle object containing the activity's previously saved state, or null if there was none.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_page);
        initViews();
    }

    /**

     The initViews method initializes the views and connects the button listeners. It finds the Geo Search

     button and sets its OnClickListener to start the SearchGeoActivity. It also finds the Name Search

     button and sets its OnClickListener to start the SearchFriendsActivity.
     */
    private void initViews() {
        Button btnGeoSearch;

        btnGeoSearch = findViewById(R.id.geo_search_button);

        btnGeoSearch.setOnClickListener(new View.OnClickListener() {
            /**
             * Overrides the onClick method to start the SearchGeoActivity when the Geo Search button is clicked.
             * @param view the View that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, SearchGeoActivity.class);
                startActivity(intent);
            }
        });

        Button btnNameSearch;

        btnNameSearch = findViewById(R.id.name_search_button);

        btnNameSearch.setOnClickListener(new View.OnClickListener() {
            /**
             * Overrides the onClick method to start the SearchFriendsActivity when the Name Search button is clicked.
             * @param view the View that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, SearchFriendsActivity.class);
                startActivity(intent);
            }
        });

    }
}
