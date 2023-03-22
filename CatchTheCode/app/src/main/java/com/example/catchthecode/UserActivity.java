package com.example.catchthecode;

// the activity that show the users' friends
// it follows the friend_page.xml

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
/**

 This class represents the user activity and corresponding activities.

 It shows the users' friends by following the friend_page.xml layout.
 */
public class UserActivity extends AppCompatActivity {
    /**

     Called when the activity is starting.

     It initializes the user's information and updates it when needed.

     @param savedInstanceState the data most recently supplied in onSaveInstanceState(Bundle)
     */
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
                        if (contactInfo == null || contactInfo.isEmpty() || name == null || name.isEmpty()){
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
                                            id.setText(name);
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
                        EditText Username = view.findViewById(R.id.enterUserName);
                        builder.setPositiveButton("Save", (dialog, which) -> {
                            String number = contactInfo.getText().toString();
                            Map<String, Object> user = new HashMap<>();
                            user.put("userid", androidId);
                            user.put("contactInfo", number);
                            user.put("username", Username.getText().toString());
                            db.collection("users").document(androidId).set(user)
                                    .addOnSuccessListener(Void -> {
                                        id.setText(Username.getText().toString());
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

        // This updates user information
        Button modifyButton = findViewById(R.id.modify_profile_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setTitle("Enter Contact Information");
                View view1 = getLayoutInflater().inflate(R.layout.enter_info_page, null);
                builder.setView(view1);
                EditText contactInfo = view1.findViewById(R.id.contactInfoText);
                EditText Username = view1.findViewById(R.id.enterUserName);
                builder.setPositiveButton("Save", (dialog, which) -> {
                    String number = contactInfo.getText().toString();
                    Map<String, Object> user = new HashMap<>();
                    user.put("userid", androidId);
                    user.put("contactInfo", number);
                    user.put("username", Username.getText().toString());
                    db.collection("users").document(androidId).set(user)
                            .addOnSuccessListener(Void -> {
                                id.setText(Username.getText().toString());
                                info.setText("Phone number is"+number);
                                Log.d(TAG, "User Updated successfully");
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
        });

        // Fill the last two columns with value
        TextView v1 = findViewById(R.id.hRankingNums);
        TextView v2 = findViewById(R.id.tCodesNums);

        // Get total number of codes scanned, and display it
        db.collection("users")
                .whereEqualTo("userid",androidId)
                .get()
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String numcodeString = document.getString("numCode");
                            v2.setText(numcodeString);
                        }
                    }
                    else{
                        Log.d(TAG, "Error ", task.getException());
                    }
                });
        // To get the current ranking of me as a player, we decide to first sort the collections
        // of players then check what is 'my' standing. We don't want to keep a copy of highest ranking
        // as a field because it would require updating each time a single player scans a code
        // which would be in-efficient
        db.collection("users")
                .orderBy("highest", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener( task -> {
                    int rankCounter = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(androidId)) {
                            // Found the user, so output their ranking
                            break;
                        } else {
                            // Increment the ranking for each user that has a higher highest value than the current user
                            rankCounter++;
                        }
                    }
                    v1.setText(String.valueOf(rankCounter));
                });


    }
}
