/**
 * This class defines the main page of the application and its button listeners.
 * */

package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

 import com.example.catchthecode.CollectionActivity;
 import com.example.catchthecode.MapsActivity;
 import com.example.catchthecode.ScannedBarcodeActivity;
 import com.example.catchthecode.ScoreBoardActivity;
 import com.example.catchthecode.SearchActivity;
 import com.example.catchthecode.UserActivity;
 import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import android.location.Location;

import java.util.HashMap;
import java.util.Map;

/**  This class defines all the controller on the main page.
 *
 */

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Creates the main page and sets up the buttons on the main page.
     * Sets up button listeners for each button.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        // getSupportActionBar().setTitle("CatchTheCode"); // setup the Title on the top left.

        // button with android:id="@+id/map" leads to MapsActivity
        Button mapButton = (Button) findViewById(R.id.map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        // button with android:id="@+id/scan" leads to ScannedBarcodeActivity
        Button scanButton = (Button) findViewById(R.id.scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScannedBarcodeActivity.class);
                startActivity(intent);
            }
        });

        // button with android:id="@+id/profile" leads to UserActivity
        Button friendButton = (Button) findViewById(R.id.profile);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        // button with android:id="@+id/collection" leads to CollectionActivity
        Button collectionButton = (Button) findViewById(R.id.collection);
        collectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                startActivity(intent);
            }
        });

        // button with android:id="@+id/Scoreboard" leads to ScoreBoardActivity
        Button ScoreBoardButton = (Button) findViewById(R.id.Scoreboard);
        ScoreBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScoreBoardActivity.class);
                startActivity(intent);
            }
        });

        // button with android:id="@+id/search_button_main" leads to SearchActivity
        Button SearchButton = (Button) findViewById(R.id.search_button_main);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Fetches the user information from the database.
     *
     * @param path The path to the user in the database.
     */
    public void fetchUser(String path) {
        db.document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // This way, you get the name of a user called james
                    // "First" is the key. Look at the
                }
            }
        });
    }
}