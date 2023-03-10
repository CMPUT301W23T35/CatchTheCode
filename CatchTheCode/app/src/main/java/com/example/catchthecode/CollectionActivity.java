package com.example.catchthecode;

/* This class defines the collection page of the app.
 * It shows the user the current QR code this user has collected.
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CollectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_page);


    }
}
