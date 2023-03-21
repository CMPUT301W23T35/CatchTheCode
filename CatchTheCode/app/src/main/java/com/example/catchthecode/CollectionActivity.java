
package com.example.catchthecode;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class defines the collection page of the app.
 * It shows the user the current QR code this user has collected.
 */

public class CollectionActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_page);
    }
}
