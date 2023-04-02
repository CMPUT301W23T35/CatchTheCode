/**
 * This class defines the main page of the application and its button listeners.
 * */

package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import android.widget.EditText;
import android.widget.Toast;

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


        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Prompt for entering information
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("userid", androidId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // user exists
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // User exists in database, retrieve user information
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String name = documentSnapshot.getString("username");
                        String contactInfo = documentSnapshot.getString("contactInfo");

                        // Sometimes user can have their id stored but not for the contact info, so we do the following
                        if (contactInfo == null || contactInfo.isEmpty() || name == null || name.isEmpty()) {
                            Log.d(TAG, "Reached but not prompted");
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Enter Contact Information");
                            View view = getLayoutInflater().inflate(R.layout.enter_info_page, null);
                            builder.setView(view);
                            EditText Info = view.findViewById(R.id.contactInfoText);
                            EditText Username = view.findViewById(R.id.enterUserName);
                            builder.setPositiveButton("Save", (dialog, which) -> {
                                String number = Info.getText().toString();
                                Map<String, Object> user = new HashMap<>();
                                user.put("username", Username.getText().toString());
                                user.put("contactInfo", number);
                                user.put("userid", androidId);
                                db.collection("users").document(androidId).set(user)
                                        .addOnSuccessListener(Void -> {
                                            Log.d(TAG, "User added successfully");
                                            //id.setText(name);
                                            //info.setText("Phone number is"+number);
                                        })
                                        .addOnFailureListener(error -> {
                                            Log.d(TAG, "Failed to add user");
                                        });

                            });
                            builder.setNegativeButton("Cancel", (dialog, which) -> {
                                // Do nothing, simply ignore it.
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            // do nothing
                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Enter Contact Information");
                        View view = getLayoutInflater().inflate(R.layout.enter_info_page, null);
                        builder.setView(view);
                        EditText contactInfo = view.findViewById(R.id.contactInfoText);
                        EditText Username = view.findViewById(R.id.enterUserName);
                        builder.setPositiveButton("Save", null); // Set a null click listener to keep the dialog open
                        builder.setNegativeButton("Cancel", (dialog, which) -> {
                            // Do nothing, simply ignore it.
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        // Override the positive button click listener after showing the dialog
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                            String number = contactInfo.getText().toString();
                            String userName = Username.getText().toString();
                            // Check if the entered username already exists in the database
                            db.collection("users")
                                    .whereEqualTo("username", userName)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            if (!task.getResult().isEmpty()) {
                                                // Username already exists, display a toast message and prompt for another name
                                                Toast.makeText(this, "Duplicate username, please enter another name", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Username is unique, add user to the database and dismiss the dialog
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("userid", androidId);
                                                user.put("contactInfo", number);
                                                user.put("username", userName);
                                                db.collection("users").document(androidId).set(user)
                                                        .addOnSuccessListener(Void -> {
                                                            //id.setText(userName);
                                                            //info.setText("Phone number is"+number);`
                                                            Log.d(TAG, "User added successfully");
                                                            alertDialog.dismiss();
                                                        })
                                                        .addOnFailureListener(error -> {
                                                            Log.d(TAG, "Failed to add user");
                                                        });
                                            }
                                        } else {
                                            Log.e(TAG, "Error checking username: ", task.getException());
                                        }
                                    });
                        });

                    }


                })
                .addOnFailureListener(error -> {
                    //Do nothing
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