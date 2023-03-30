package com.example.catchthecode;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        // Get the user ID from the intent
        String userId = getIntent().getStringExtra("userid");
        // Make
        TextView id = findViewById(R.id.playerID);
        TextView info = findViewById(R.id.info);
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
                    info.setText("Phone number is"+contactInfo);
                });
    }
}


