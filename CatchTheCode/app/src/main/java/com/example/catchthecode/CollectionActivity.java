
package com.example.catchthecode;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
        ListView listView = findViewById(R.id.collection_rank);
        ListView listScoreView = findViewById(R.id.collection_score);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

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

        // Get the list of QR codes the user has collected from field qrLists and display them in a list view
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, qrNameList);
                            listView.setAdapter(adapter);
                            ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, qrScoreList);
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

        // set listener on the list view to show the QR code when the user clicks on it
        // listView.setOnItemClickListener((parent, view, position, id) -> {
        //     Intent intent = new Intent(CollectionActivity.this, QRCodeActivity.class);
        //     intent.putExtra("name", listView.getItemAtPosition(position).toString());
        //     startActivity(intent);
        // });




    }
}