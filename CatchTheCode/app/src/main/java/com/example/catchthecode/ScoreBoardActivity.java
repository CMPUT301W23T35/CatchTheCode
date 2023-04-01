package com.example.catchthecode;

import static android.R.layout.simple_list_item_1;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

 Represents a scoreboard activity and its functionalities
 */
public class ScoreBoardActivity extends AppCompatActivity {


    /**

     Called when the activity is starting. This is where most initialization should go.

     @param savedInstanceState a Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_board);


        //Intent intent1 = new Intent(ScoreBoardActivity.this, DBUpdate.class);
        //startActivity(intent1);
        Intent intent = getIntent();




        updateDatabase().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //getSupportActionBar().setTitle("Ranking Board"); // sets the title of the ranking type.
                // Set the ActionBar
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle("Scoreboard");
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference userRef = db.collection("users");


                final TextView text1 = findViewById(R.id.text1);
                final TextView text2 = findViewById(R.id.text2);
                final TextView text3 = findViewById(R.id.text3);

                TextView[] playerViews = new TextView[] {text1,text2,text3};

                Spinner spinner = findViewById(R.id.my_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new String[]{"Total ranking", "highest ranking"});
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 1){
                            List<String> listUsers2 = new ArrayList<String>();
                            Query highestQuery = userRef.orderBy("highest", Query.Direction.DESCENDING);
                            highestQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int i = 0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (i < 3) {
                                                String playerId = document.getString("username");
                                                long playerScore = document.getLong("highest");
                                                //users.put(counter,playerId+":"+String.valueOf(playerScore));
                                                playerViews[i].setText("Player " + playerId + " has a code of " + String.valueOf(playerScore) + " points");
                                                i++;
                                            } else {
                                                Log.d(TAG, "Reached", task.getException());
                                                String playerId = document.getString("username");
                                                long playerScore = document.getLong("highest");
                                                listUsers2.add("Player " + playerId + " has a code of " + String.valueOf(playerScore) + " points");
                                                i++;
                                            }
                                        }
                                        final ListView playerList = findViewById(R.id.userList);
                                        //Log.d(TAG, listUsers.get(1));
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScoreBoardActivity.this, simple_list_item_1, listUsers2);
                                        playerList.setAdapter(adapter);

                                    } else {
                                        Log.d(TAG, "Error getting players: ", task.getException());
                                    }
                                }
                            });
                        }

                        else if (i == 0) {

                            Query query = userRef.orderBy("score", Query.Direction.DESCENDING);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                /**

                                 This method is called when a Firestore query is complete, and it updates the UI based on the results.
                                 If the query is successful, it iterates through the query results and populates the top 3 player scores into
                                 the text views, and the rest of the players scores into the list view. It then sets the adapter for the list view.
                                 If the query fails, it logs an error message.
                                 @param task a Task containing the result of the Firestore query
                                 */
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<String> listUsers = new ArrayList<String>();
                                    if (task.isSuccessful()) {
                                        int i = 0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (i < 3) {
                                                String playerId = document.getString("username");
                                                long playerScore = document.getLong("score");
                                                //users.put(counter,playerId+":"+String.valueOf(playerScore));
                                                playerViews[i].setText("Player " + playerId + " has " + String.valueOf(playerScore) + " total points");
                                                i++;
                                            }
                                            else {
                                                Log.d(TAG, "Reached", task.getException());
                                                String playerId = document.getString("username");
                                                long playerScore = document.getLong("score");
                                                listUsers.add("Player " + playerId + " has " + String.valueOf(playerScore) + " total points");
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

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // just do nothing if nothing is selected
                    }
                });

                Query query = userRef.orderBy("score", Query.Direction.DESCENDING);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**

                     This method is called when a Firestore query is complete, and it updates the UI based on the results.
                     If the query is successful, it iterates through the query results and populates the top 3 player scores into
                     the text views, and the rest of the players scores into the list view. It then sets the adapter for the list view.
                     If the query fails, it logs an error message.
                     @param task a Task containing the result of the Firestore query
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> listUsers = new ArrayList<String>();
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (i < 3) {
                                    String playerId = document.getString("username");
                                    long playerScore = document.getLong("score");
                                    //users.put(counter,playerId+":"+String.valueOf(playerScore));
                                    playerViews[i].setText("Player " + playerId + " has " + String.valueOf(playerScore) + " total points");
                                    i++;
                                }
                                else {
                                    Log.d(TAG, "Reached", task.getException());
                                    String playerId = document.getString("username");
                                    long playerScore = document.getLong("score");
                                    listUsers.add("Player " + playerId + " has " + String.valueOf(playerScore) + " total points");
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
            } else {
                // handle error
            }
        });









    }



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
