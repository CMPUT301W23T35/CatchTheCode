package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FriendPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page_without_modify);

        // Get the user ID from the intent
        String userId = getIntent().getStringExtra("userid");
        // Make
        TextView id = findViewById(R.id.playerID);
        TextView info = findViewById(R.id.info);

        Intent intent1 = new Intent(FriendPageActivity.this, DBUpdate.class);
        startActivity(intent1);
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("userid", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // User must exist in database. Because
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    String name = documentSnapshot.getString("username");
                    String contactInfo = documentSnapshot.getString("contactInfo");
                    id.setText(name);
                    info.setText("Phone number is "+ contactInfo);
                });

        // Fill the last two columns with value
        TextView v1 = findViewById(R.id.hRankingNums);
        TextView v2 = findViewById(R.id.tCodesNums);

        // Get total number of codes scanned, and display it
        db.collection("users")
                .whereEqualTo("userid",userId)
                .get()
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ArrayList<String> qrCodes = (ArrayList<String>) document.get("qrLists");
                            int qrListsLength = qrCodes != null ? qrCodes.size() : 0;
                            Log.d(TAG, "Number of codes scanned: " + qrListsLength);
                            v2.setText(String.valueOf(qrListsLength));
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
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener( task -> {
                    int rankCounter = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(userId)) {
                            // Found the user, so output their ranking
                            break;
                        } else {
                            // Increment the ranking for each user that has a higher highest value than the current user
                            rankCounter++;
                        }
                    }
                    v1.setText(String.valueOf(rankCounter));
                });


        // collection_button leads to the collection page for the current user with userId
        Button collectionButton = findViewById(R.id.collection_button);
        collectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendPageActivity.this, CollectionActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });
    }
}


