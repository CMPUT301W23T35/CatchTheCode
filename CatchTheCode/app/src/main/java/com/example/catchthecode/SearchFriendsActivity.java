package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 This class runs the search friends list page. (search_friends.xml)
 */
public class SearchFriendsActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_friends);
        //initViews();
        Button button = findViewById(R.id.searchButton);
        TextView text = findViewById(R.id.searchEditText);
        ListView listOfUsers = findViewById(R.id. resultsList);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
                String name = text.getText().toString();
                // Search for friend in DB;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // db.collection("users")
                //         .whereEqualTo("username", name)
                //         .get()
                //         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                //             @Override
                //             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //                 if (task.isSuccessful()) {
                //                     // Get the list of users that match the search criteria
                //                     ArrayList<String> userList = new ArrayList<>();
                //                     for (QueryDocumentSnapshot document : task.getResult()) {
                //                         String userId = document.getString("userid");
                //                         userList.add("User id: "+userId);
                //                     }
                //                     // Now update the page so that it gets displayed a list of users
                //                     ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchFriendsActivity.this, android.R.layout.simple_list_item_1, userList);
                //                     listOfUsers.setAdapter(adapter);
                //                     if (userList.isEmpty()){
                //                         Toast.makeText(SearchFriendsActivity.this, "User " + name + " not found", Toast.LENGTH_SHORT).show();
                //                     }

                //                     // Now, make the listview be responsive to clicks
                //                     listOfUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //                         @Override
                //                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //                             // Get the clicked user ID from the adapter
                //                             String clickedUserId = (String) parent.getItemAtPosition(position);
                //                             // Start the new activity to display the clicked user's page
                //                             Intent intent = new Intent(SearchFriendsActivity.this, FriendPageActivity.class);
                //                             // Assuming the user ID is after "User id: " in the string
                //                             intent.putExtra("userid", clickedUserId.substring(9));
                //                             startActivity(intent);
                //                         }
                //                     });

                //                 } else {
                //                     Log.d(TAG, "Error getting documents: ", task.getException());
                //                 }
                //             }
                //         });

                // get all username and userid from the database
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // Get the list of users that match the search criteria
                                    ArrayList<String> userList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String userId = document.getString("userid");
                                        String username = document.getString("username");
                                        // if username contains name, add to list
                                        if (username.contains(name)) {
                                            userList.add("UserName: " + username);
                                        }
                                    }
                                    // Now update the page so that it gets displayed a list of users
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SearchFriendsActivity.this, android.R.layout.simple_list_item_1, userList);
                                    listOfUsers.setAdapter(adapter);
                                    if (userList.isEmpty()) {
                                        Toast.makeText(SearchFriendsActivity.this, "User " + name + " not found", Toast.LENGTH_SHORT).show();
                                    }

                                    // Now, make the listview be responsive to clicks
                                    listOfUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        /**
                                         * called when an item is clicked
                                         * @param parent
                                         * @param view
                                         * @param position
                                         * @param id
                                         */
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // Get the clicked user ID from the adapter
                                            String clickedUserName = (String) parent.getItemAtPosition(position);
                                            String clickedUserId = "";
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String userId = document.getString("userid");
                                                String username = document.getString("username");
                                                if (username.equals(clickedUserName.substring(10))) {
                                                    clickedUserId = userId;
                                                }
                                            }
                                            Log.d("clickedUserId", clickedUserId);
                                            // Start the new activity to display the clicked user's page
                                            Intent intent = new Intent(SearchFriendsActivity.this, FriendPageActivity.class);
                                            // Assuming the user ID is after "User id: " in the string
                                            intent.putExtra("userid", clickedUserId);
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });
    }
}