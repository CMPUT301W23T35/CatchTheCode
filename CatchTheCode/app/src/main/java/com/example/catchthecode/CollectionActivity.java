
package com.example.catchthecode;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * This class defines the collection page of the app.
 * It shows the user the current QR code this user has collected.
 */

public class CollectionActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_page);

        TextView rankingNum = findViewById(R.id.current_ranking_nums);
        TextView highest = findViewById(R.id.highest_button_num);
        TextView lowest = findViewById(R.id.lowest_button_num);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Set ranking
        db.collection("users")
                .orderBy("highest", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener( task -> {
                    int rankCounter = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(androidId)) {
                            // Found the user, so output their ranking
                            highest.setText(String.valueOf(document.getLong("highest")));
                            lowest.setText(String.valueOf(document.getLong("lowest")));
                            break;
                        } else {
                            // Increment the ranking for each user that has a higher highest value than the current user
                            rankCounter++;
                        }
                    }
                    rankingNum.setText(String.valueOf(rankCounter));
                });

    }
}
