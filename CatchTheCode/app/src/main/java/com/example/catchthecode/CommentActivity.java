
package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrRef = db.collection("QRs");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_comment_member);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");
        String allowComment = intent.getStringExtra("allowComment");
        ListView listView = findViewById(R.id.commentsList);
        Button addCommentButton = findViewById(R.id.addCommentButton);
        EditText editTextComment = findViewById(R.id.enter_comment_box);



        qrRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
            FirebaseFirestoreException error) {
                List<String> commentList = new ArrayList<String>();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    if (((String) doc.getData().get("readable_name")).equals(name)) {
                        if (doc.getData().get("comments") == null) {
                            commentList = new ArrayList<String>();
//                            qrRef.document(doc.getId()).update(
//                                            "qrLists", FieldValue.arrayUnion(name))
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void unused) {
//                                            Log.e(TAG, "qrList field created successfully");;
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.e(TAG, "comments field failed to create");
//                                        }
//                                    });
                        } else {

                            commentList = (List<String>) doc.getData().get("comments");
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, commentList);
                listView.setAdapter(adapter);
            }
        });


//        qrRef.document(name).get(Source.SERVER).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                List<String> commentList = (List<String>) task.getResult().get("comments");
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, commentList);
//                listView.setAdapter(adapter);
//            } else {
//                Log.d("CollectionActivity", "Error getting documents: ", task.getException());
//            }
//        });

        if (allowComment.equals("0")) {
            addCommentButton.setVisibility(View.INVISIBLE);
            addCommentButton.setClickable(false);
            editTextComment.setVisibility(View.INVISIBLE);
            editTextComment.setClickable(false);
        } else {
            addCommentButton.setVisibility(View.VISIBLE);
            addCommentButton.setClickable(true);
            editTextComment.setVisibility(View.VISIBLE);
            editTextComment.setClickable(true);
        }
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentComment = editTextComment.getText().toString();
                editTextComment.getText().clear();
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                addToUserCommentCollection(qrRef, id, currentComment);
//                qrRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
////                        List<String> commentList = new ArrayList<String>();
//                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                        {
////                            if (((String) doc.getData().get("readable_name")).equals(name)) {
//                                if (doc.getData().get("comments") == null) {
//                                    qrRef.document(doc.getId()).update(
//                                                    "comments", FieldValue.arrayUnion(currentComment))
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void unused) {
//                                                    Log.e(TAG, "qrList field created successfully");;
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.e(TAG, "comments field failed to create");
//                                                }
//                                            });
////                                } else {
//
////                                }
//                            }
//                        }
//                    }
//                });

            }
        });

    }
    private void addToUserCommentCollection(CollectionReference qrRef, String id, String comment) {
        // check if name is already in the current user's array
        qrRef.document(id).get(Source.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //check whether qrLists field is empty
                if (task.getResult().get("comments") == null) {
                    qrRef.document(id).update(
                                    "comments", FieldValue.arrayUnion(comment))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.e(TAG, "comments added to list");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "comments failed to add to user list");
                                }
                            });
                } else {
                    qrRef.document(id).update("comments", FieldValue.arrayUnion(comment))
//                    List<String> qrList = (List<String>) task.getResult().get("qrLists");
//                    boolean duplicate = false;
//                    for (int i = 0; i < qrList.size(); i++) {
//                        if (name == qrList.get(i)) duplicate = true;
//                    }
//                    if (!duplicate) {
//                        userRef.document(androidId).update(
//                                        "qrLists", FieldValue.arrayUnion(name))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.e(TAG, "comment added to list");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "comment added to list");
                                    }
                                });
                    }
                }
//            } else {
//                Log.d("CollectionActivity", "Error getting documents: ", task.getException());
//            }
        });
    }
}