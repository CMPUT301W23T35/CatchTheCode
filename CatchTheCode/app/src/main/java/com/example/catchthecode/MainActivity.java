package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.units.qual.K;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        // button with android:id="@+id/map" leads to MapsActivity
        Button mapButton = (Button) findViewById(R.id.map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        
        // button with android:id="@+id/friend" leads to FriendActivity
        Button friendButton = (Button) findViewById(R.id.friend);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendActivity.class);
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

        HashMap<String,Object> code = new HashMap<>();
    }

    // call this function when you want to fetch
    // path is a String, containing path to a specific user
    public void fetchUser(String path) {
        db.document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){ // check for existance
                    // This way, you get the name of a user called james
                    // "First" is the key. Look at the firebase in the following link for visualization
                    // https://console.firebase.google.com/project/ctcdb-1d60c/firestore/data/~2Fusers~2FJame1
                    String first = documentSnapshot.getString("First");
                    Log.d("myTag","The string is "+first);

                    //This method gets you all of the data about user jame1
                    Map<String,Object> allData = documentSnapshot.getData();

                    // If you want to reconstruct the user object using the data from DB, do following
                    // Type name = documentSnapshot.toObject(Type.class);
                }
            }
        });
    }

    //This method add to DB with arbitrary ID
    public void simpleAd(Map user) {
        // Adding User to DB
        // This way, the user is added with arbitrary ID.

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    // This functions handles uploading/updateing a document.
    // Just pass in the object, all the info will be stored in the DB and can be fetched by function above

    public void AddWithID(Map user){
            db.collection("users").document(String.valueOf(user.get("username")))
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }


    // if you want to update without overwritting entire document, use this method
    public void update(String key, String value, Map user){
        db.collection("users").document(String.valueOf(user.get("username")))
                .update(
                        key, value
                );
    }


    // If you want to delete a user
    public void deleteUser(Map user){
        db.collection("users").document(String.valueOf(user.get("username")))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }


    public void deleteField(Map user, String toRemove){
        Map<String,Object> updates = new HashMap<>();
        updates.put(toRemove, FieldValue.delete());
        db.collection("users").document(String.valueOf(user.get("username")))
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Deletion failed");
                    }
                });
    }

    // Call this function to add a QR code to DB(adds under the QR part)
    public void addQrCode(Map code){
        db.collection("QRs").document(String.valueOf(code.get("id")))
                .set(code)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }










}