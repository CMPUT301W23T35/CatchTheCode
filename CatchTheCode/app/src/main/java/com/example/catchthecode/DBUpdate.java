package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DBUpdate extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String userId = document.getId();
                    Log.d("TAG", userId);
                    ArrayList<String> qrCodes = (ArrayList<String>) document.get("qrLists");

                    if (qrCodes != null && !qrCodes.isEmpty()) {
                        // Initialize scores to 0
                        final int[] highestScore = {0};
                        final int[] lowestScore = {Integer.MAX_VALUE};
                        final int[] totalScore = {0};

                        // Iterate over the QR codes of the user to find highest, lowest and total scores
                        for (String qrCode : qrCodes) {
                            db.collection("QRs").document(qrCode).get().addOnCompleteListener(qrTask -> {
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
                                    db.collection("users").document(userId).update(
                                            "highest", highestScore[0],
                                            "lowest", lowestScore[0],
                                            "score", totalScore[0]
                                    ).addOnSuccessListener(success -> {
                                        Log.d(TAG, "User " + userId + " scores updated successfully");
                                    }).addOnFailureListener(error -> {
                                        Log.e(TAG, "Error updating scores for user " + userId + ": " + error.getMessage());
                                    });
                                } else {
                                    Log.e(TAG, "Error getting QR document: ", qrTask.getException());
                                }
                            });
                        }
                    } else {
                        // If user has no QR codes, set all scores to 0
                        db.collection("users").document(userId).update(
                                "highest", 0,
                                "lowest", 0,
                                "score", 0
                        ).addOnSuccessListener(success -> {
                            Log.d(TAG, "User " + userId + " scores updated successfully");
                        }).addOnFailureListener(error -> {
                            Log.e(TAG, "Error updating scores for user " + userId + ": " + error.getMessage());
                        });
                    }
                }
            } else {
                Log.e(TAG, "Error getting user documents: ", task.getException());
            }
        });
        finish();


    }
}