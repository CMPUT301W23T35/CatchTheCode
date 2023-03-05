package com.example.catchthecode;

// the activity that show the users' friends
// it follows the friend_page.xml

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        // To achieve logging without asking the user to type in username, we must do the following
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        TextView id = findViewById(R.id.playerID);
        TextView info = findViewById(R.id.info);

        // Check if user is already in the firestore. Create one if not.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", androidId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // User exists in database, retrieve user information
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        String name = documentSnapshot.getString("username");
                        String contactInfo = documentSnapshot.getString("contactInfo");

                        // Sometimes user can have their id stored but not for the contact info, so we do the following
                        if (contactInfo == null || contactInfo.isEmpty()){
                            Log.d(TAG, "Reached but not prompted");
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Enter Contact Information");
                            View view = getLayoutInflater().inflate(R.layout.enter_info_page, null);
                            builder.setView(view);
                            EditText Info = view.findViewById(R.id.contactInfoText);
                            builder.setPositiveButton("Save", (dialog, which) -> {
                                String number = Info.getText().toString();
                                Map<String, Object> user = new HashMap<>();
                                user.put("username", androidId);
                                user.put("contactInfo", number);
                                db.collection("users").document(androidId).set(user)
                                        .addOnSuccessListener(Void -> {
                                            Log.d(TAG, "User added successfully");
                                            id.setText(androidId);
                                            info.setText("Phone number is"+number);
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
                        }
                        else{
                            id.setText(name);
                            info.setText("Phone number is"+contactInfo);

                        }

                    } else {
                        // Use alertDialog to prompt the user to input contact info
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Enter Contact Information");
                        View view = getLayoutInflater().inflate(R.layout.enter_info_page, null);
                        builder.setView(view);
                        EditText contactInfo = view.findViewById(R.id.contactInfoText);
                        builder.setPositiveButton("Save", (dialog, which) -> {
                            String number = contactInfo.getText().toString();
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", androidId);
                            user.put("contactInfo", number);
                            db.collection("users").document(androidId).set(user)
                                    .addOnSuccessListener(Void -> {
                                        id.setText(androidId);
                                        info.setText("Phone number is"+number);
                                        Log.d(TAG, "User added successfully");
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
                    }
                })
                .addOnFailureListener(error -> {
                    //Do nothing
                });


    }
}
