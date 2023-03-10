package com.example.catchthecode;

import static android.R.layout.simple_list_item_1;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * represent a scoreboard activity and its functionalities
 */
public class ScoreBoardActivity extends AppCompatActivity {
    List<String> listUsers = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_board);
        Intent intent = getIntent();

        getSupportActionBar().setTitle("Unique Ranking"); // sets the title of the ranking type.

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userRef = db.collection("users");


        final TextView text1 = findViewById(R.id.text1);
        final TextView text2 = findViewById(R.id.text2);
        final TextView text3 = findViewById(R.id.text3);

        TextView[] playerViews = new TextView[] {text1,text2,text3};




        Query query = userRef.orderBy("score", Query.Direction.DESCENDING);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (i < 3) {
                            String playerId = document.getId();
                            long playerScore = document.getLong("score");
                            //users.put(counter,playerId+":"+String.valueOf(playerScore));
                            playerViews[i].setText("Player " + playerId + " has " + String.valueOf(playerScore) + " points");
                            i++;
                        }
                        else {
                            Log.d(TAG, "Reached", task.getException());
                            String playerId = document.getId();
                            long playerScore = document.getLong("score");
                            listUsers.add("Player " + playerId + " has " + String.valueOf(playerScore) + " points");
                            i++;
                        }
                    }
                    final ListView playerList = findViewById(R.id.userList);
                    //Log.d(TAG, listUsers.get(1));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScoreBoardActivity.this, simple_list_item_1, listUsers);
                    playerList.setAdapter(adapter);

                } else {
                    Log.d(TAG, "Error getting players: ", task.getException());
                }
            }
        });




    }
}
