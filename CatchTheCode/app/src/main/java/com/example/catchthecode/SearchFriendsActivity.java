package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**

 This class runs the search friends list page. (search_friends.xml)
 */
public class SearchFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_friends);
        getSupportActionBar().setTitle("Friend Search"); // setup the Title on the top left.
        //initViews();
        Button button = findViewById(R.id.searchButton);
        TextView text = findViewById(R.id.searchEditText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                String ID = text.getText().toString();
                // Search for friend in DB;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users")
                        .whereEqualTo("userid", ID)
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            // user exists
                            if (!querySnapshot.isEmpty()) {
                                // The user exists in Cloud Firestore
                                // Display friend page
                                Intent intent = new Intent(SearchFriendsActivity.this, FriendPageActivity.class);
                                intent.putExtra("userId", ID);
                                startActivity(intent);

                            } else {
                                // The user does not exist in Cloud Firestore
                                Toast.makeText(getApplicationContext(), "User " + ID + " doesn't exist. Please input again.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(error -> {
                            // Handle errors here
                            Toast.makeText(getApplicationContext(), "Error checking user " + ID + " in Cloud Firestore: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            }
        });
    }
}
