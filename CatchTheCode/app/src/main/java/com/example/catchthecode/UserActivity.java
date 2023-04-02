package com.example.catchthecode;

// the activity that show the users' friends
// it follows the friend_page.xml

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        //Intent intent1 = new Intent(UserActivity.this, DBUpdate.class);
        //startActivity(intent1);
        updateDatabase().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                // do as normal
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
                                                    id.setText(name);
                                                    info.setText("Phone number is" + number);
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
                                    id.setText(name);
                                    info.setText("Phone number is" + contactInfo);

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
                                                info.setText("Phone number is" + number);
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
                    /**
                     * called when the button is clicked
                     * @param view
                     */
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                        builder.setTitle("Enter Contact Information");
                        View view1 = getLayoutInflater().inflate(R.layout.enter_info_page, null);
                        builder.setView(view1);
                        EditText contactInfo = view1.findViewById(R.id.contactInfoText);
                        EditText Username = view1.findViewById(R.id.enterUserName);
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
                                                Toast.makeText(UserActivity.this, "Duplicate username, please enter another name", Toast.LENGTH_SHORT).show();
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
                                                id.setText(userName);
                                                info.setText("Phone number is " + number);
                                            }
                                        } else {
                                            Log.e(TAG, "Error checking username: ", task.getException());
                                        }
                                    });
                        });
                    }
                });

                // Fill the last two columns with value
                TextView v1 = findViewById(R.id.hRankingNums);
                TextView v2 = findViewById(R.id.tCodesNums);

                // Get total number of codes scanned, and display it
                db.collection("users")
                        .whereEqualTo("userid", androidId)
                        .get()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                    ArrayList<String> qrCodes = (ArrayList<String>) document.get("qrLists");
                                    int qrListsLength = qrCodes != null ? qrCodes.size() : 0;
                                    Log.d(TAG, "Number of codes scanned: " + qrListsLength);
                                    v2.setText(String.valueOf(qrListsLength));
                                }
                            } else {
                                Log.d(TAG, "Error ", task1.getException());
                            }
                        });
                // To get the current ranking of me as a player, we decide to first sort the collections
                // of players then check what is 'my' standing. We don't want to keep a copy of highest ranking
                // as a field because it would require updating each time a single player scans a code
                // which would be in-efficient
                db.collection("users")
                        .orderBy("score", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(task2 -> {
                            int rankCounter = 1;
                            for (QueryDocumentSnapshot document : task2.getResult()) {
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

                // collection_button leads to the collection page
                Button collectionButton = findViewById(R.id.collection_button);
                collectionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserActivity.this, CollectionActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else{
                //handle error
            }
        });
    }
    /**
     * update the database with the lastest information
     * @return Task<Void></Void>
     */
    private Task<Void> updateDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Task<Void>> updateTasks = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String userId = document.getId();
                    Log.d("TAG", userId);
                    ArrayList<String> qrCodes = (ArrayList<String>) document.get("qrLists");
                    int qrListsLength = qrCodes != null ? qrCodes.size() : 0;

                    Task<Void> updateQrListsLengthTask = db.collection("users").document(userId).update("qrListsLength", qrListsLength);
                    updateTasks.add(updateQrListsLengthTask);

                    if (qrCodes != null && !qrCodes.isEmpty()) {
                        // Initialize scores to 0
                        final int[] highestScore = {0};
                        final int[] lowestScore = {Integer.MAX_VALUE};
                        final int[] totalScore = {0};

                        for (String qrCode : qrCodes) {
                            Task<DocumentSnapshot> getQrDocumentTask = db.collection("QRs").document(qrCode).get();
                            getQrDocumentTask.addOnCompleteListener(qrTask -> {
                                if (qrTask.isSuccessful()) {
                                    DocumentSnapshot qrDocument = qrTask.getResult();
                                    int score = qrDocument.getLong("score").intValue();
                                    totalScore[0] += score;

                                    if (score > highestScore[0]) {
                                        highestScore[0] = score;
                                    }

                                    if (score < lowestScore[0]) {
                                        lowestScore[0] = score;
                                    }

                                    // Update user's highest score, lowest score and total score
                                    Task<Void> updateScoresTask = db.collection("users").document(userId).update(
                                            "highest", highestScore[0],
                                            "lowest", lowestScore[0],
                                            "score", totalScore[0]
                                    );
                                    updateTasks.add(updateScoresTask);
                                } else {
                                    Log.e(TAG, "Error getting QR document: ", qrTask.getException());
                                }
                            });
                        }
                    } else {
                        // If user has no QR codes, set all scores to 0
                        Task<Void> updateScoresTask = db.collection("users").document(userId).update(
                                "highest", 0,
                                "lowest", 0,
                                "score", 0
                        );
                        updateTasks.add(updateScoresTask);
                    }
                }

                // Combine all tasks into a single Task<Void>
                Tasks.whenAll(updateTasks).addOnCompleteListener(allTasks -> {
                    if (allTasks.isSuccessful()) {
                        taskCompletionSource.setResult(null);
                    } else {
                        taskCompletionSource.setException(new Exception("Error updating user scores"));
                    }
                });

            } else {
                taskCompletionSource.setException(new Exception("Error getting user documents"));
                Log.e(TAG, "Error getting user documents: ", task.getException());
            }
        });
        return taskCompletionSource.getTask();
    }


}
