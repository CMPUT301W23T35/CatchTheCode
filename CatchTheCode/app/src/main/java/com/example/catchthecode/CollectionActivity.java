
package com.example.catchthecode;

import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 The CollectionActivity class displays the user's current QR code collection and rank.
 The class extends the AppCompatActivity class.
 The class includes methods to update the user's QR code collection and rank.
 */

public class CollectionActivity extends AppCompatActivity {
    /**
     * The REQUEST constant is used to identify the intent that started the activity.
     */
    final static int REQUEST = 999;
    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     * @see AppCompatActivity#onCreate(Bundle)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_page);

        /**
         * Asynchronously updates the user's QR code collection and rank in the database.
         * @return a Task object representing the completion of the update process
         */
        // call DBUpdate to update the database
        //DBUpdate dbUpdate = new DBUpdate();
        updateDatabase().addOnCompleteListener(taskNew -> {
            if (taskNew.isSuccessful()) {
                TextView rankingNum = findViewById(R.id.current_ranking_nums);
                ListView listView = findViewById(R.id.collection_rank);
                ListView listScoreView = findViewById(R.id.collection_score);
                TextView QRCodeCount = findViewById(R.id.QRCodeCount);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // get the intent extra from the previous activity, if "userid" exists, then it is from the search friends page
                Intent intent = getIntent();
                String userid = intent.getStringExtra("userid");
                String androidId;
                if(userid != null){
                    androidId = userid;
                }else{
                    androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                }

                // Get the current ranking of the user
                db.collection("users").orderBy("score", Query.Direction.DESCENDING).get(Source.SERVER).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int rank = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            rank++;
                            if (document.getId().equals(androidId)) {
                                rankingNum.setText(String.valueOf(rank));
                                break;
                            }
                        }
                    } else {
                        Log.d("CollectionActivity", "Error getting documents: ", task.getException());
                    }
                });

                // Get the number of QR codes the user has collected
                db.collection("users").document(androidId).get(Source.SERVER).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> qrList = (List<String>) task.getResult().get("qrLists");
                        if (qrList == null) {
                            qrList = new ArrayList<>();
                        }
                        QRCodeCount.setText(String.valueOf(qrList.size()));
                    } else {
                        Log.d("CollectionActivity", "Error getting documents: ", task.getException());
                    }
                });


                /**
                 This method sets up a spinner to sort the QR codes by highest or lowest score
                 */
                Spinner spinner = findViewById(R.id.collection_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new String[]{"Highest first", "Lowest first"});
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    /**
                     * Called when an QR code is selected
                     */
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        if(pos == 0){
                            // sort from highest to lowest
                            db.collection("users").document(androidId).get(Source.SERVER).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<String> qrList = (List<String>) task.getResult().get("qrLists");
                                    if (qrList == null) {
                                        qrList = new ArrayList<>();
                                    }
                                    // convert it to human readable name with getqrName() from QRs collection in the database
                                    List<String> qrNameList = new ArrayList<>();
                                    List<Integer> qrScoreList = new ArrayList<>();
                                    for (String qr : qrList) {
                                        db.collection("QRs").document(qr).get(Source.SERVER).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                //Log.d("CollectionActivity", task1.getResult().get("readable_name").toString());
                                                qrNameList.add(task1.getResult().get("readable_name").toString());
                                                qrScoreList.add(Integer.parseInt(task1.getResult().get("score").toString()));
                                                Log.d("CollectionActivity", qrNameList.toString());
                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CollectionActivity.this, android.R.layout.simple_list_item_1, qrNameList);
                                                listView.setAdapter(adapter);
                                                ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(CollectionActivity.this, android.R.layout.simple_list_item_1, qrScoreList);
                                                listScoreView.setAdapter(adapter1);
                                                // sort the list by score and sort the name list accordingly
                                                for (int i = 0; i < qrScoreList.size(); i++) {
                                                    for (int j = i + 1; j < qrScoreList.size(); j++) {
                                                        if (qrScoreList.get(i) < qrScoreList.get(j)) {
                                                            int temp = qrScoreList.get(i);
                                                            qrScoreList.set(i, qrScoreList.get(j));
                                                            qrScoreList.set(j, temp);
                                                            String temp1 = qrNameList.get(i);
                                                            qrNameList.set(i, qrNameList.get(j));
                                                            qrNameList.set(j, temp1);
                                                        }
                                                    }
                                                }
                                            } else {
                                                Log.d("CollectionActivity", "Error getting documents: ", task1.getException());
                                            }
                                        });
                                    }

                                } else {
                                    Log.d("CollectionActivity", "Error getting documents: ", task.getException());
                                }
                            });
                        }
                        else if(pos == 1){
                            // do the same thing but sort from lowest to highest
                            db.collection("users").document(androidId).get(Source.SERVER).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    List<String> qrList = (List<String>) task.getResult().get("qrLists");
                                    if (qrList == null) {
                                        qrList = new ArrayList<>();
                                    }
                                    // convert it to human readable name with getqrName() from QRs collection in the database
                                    List<String> qrNameList = new ArrayList<>();
                                    List<Integer> qrScoreList = new ArrayList<>();
                                    for (String qr : qrList) {
                                        db.collection("QRs").document(qr).get(Source.SERVER).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                //Log.d("CollectionActivity", task1.getResult().get("readable_name").toString());
                                                qrNameList.add(task1.getResult().get("readable_name").toString());
                                                qrScoreList.add(Integer.parseInt(task1.getResult().get("score").toString()));
                                                Log.d("CollectionActivity", qrNameList.toString());
                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(CollectionActivity.this, android.R.layout.simple_list_item_1, qrNameList);
                                                listView.setAdapter(adapter);
                                                ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(CollectionActivity.this, android.R.layout.simple_list_item_1, qrScoreList);
                                                listScoreView.setAdapter(adapter1);
                                                // sort the list by score and sort the name list accordingly
                                                for (int i = 0; i < qrScoreList.size(); i++) {
                                                    for (int j = i + 1; j < qrScoreList.size(); j++) {
                                                        if (qrScoreList.get(i) > qrScoreList.get(j)) {
                                                            int temp = qrScoreList.get(i);
                                                            qrScoreList.set(i, qrScoreList.get(j));
                                                            qrScoreList.set(j, temp);
                                                            String temp1 = qrNameList.get(i);
                                                            qrNameList.set(i, qrNameList.get(j));
                                                            qrNameList.set(j, temp1);
                                                        }
                                                    }
                                                }
                                            } else {
                                                Log.d("CollectionActivity", "Error getting documents: ", task1.getException());
                                            }
                                        });
                                    }

                                } else {
                                    Log.d("CollectionActivity", "Error getting documents: ", task.getException());
                                }
                            });
                        }
                    }
                    /**
                     * Called when nothing is selected
                     */
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // do nothing when nothing is selected
                    }
                });
                // set listener on the list view to show the QR code when the user clicks on it
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    Intent newintent = new Intent(CollectionActivity.this, QRCodeActivity.class);
                    newintent.putExtra("name", listView.getItemAtPosition(position).toString());
                    // if the current android id is not the same as the one in the intent, then it is from the search friends page
                    if(userid != null){
                        newintent.putExtra("userid", userid);
                    }
                    startActivityForResult(newintent, REQUEST);

                });
            } else {
            }
        });


    }

    /**
     Called when an activity you launched exits, giving you the requestCode you started it with, the resultCode it returned, and any additional data from it.
     @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     @param resultCode The integer result code returned by the child activity through its setResult().
     @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     If the requestCode and resultCode match the expected values, update the database with the new data obtained from the Intent, and recreate the activity to reflect the changes.
     If the update is successful, recreate the activity to show the updated information. Otherwise, do nothing.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            // Get the updated data from the Intent
            String updatedData = data.getStringExtra("updatedData");
            updateDatabase().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    recreate();
                } else {
                }
            });

            // Refresh the activity
            // ...
        }
    }

    /**
     Updates the user scores in the Firestore database based on their QR code data.
     Retrieves all user documents in the "users" collection, and for each user, retrieves their
     associated QR codes and updates their highest score, lowest score, and total score in the
     "users" collection.
     If a user has no QR codes, their highest score, lowest score, and total score are all set to 0.
     @Returns a Task<Void> that completes when all updates have been made.
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
