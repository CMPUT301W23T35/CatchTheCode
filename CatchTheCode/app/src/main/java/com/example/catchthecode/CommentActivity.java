
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

/**
 This activity displays the comments associated with a particular QR code.
 It allows the user to add a new comment and view existing comments.
 The comments are stored in the Firestore database.
 */
public class CommentActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrRef = db.collection("QRs");

    /**
     Called when the activity is starting.
     @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     @see AppCompatActivity#onCreate(Bundle)
     */
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
                        } else {

                            commentList = (List<String>) doc.getData().get("comments");
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, commentList);
                listView.setAdapter(adapter);
            }
        });


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
            }
        });
    }

    /**
     Add the comment to the comments collection for a given user ID.
     @param qrRef A reference to the QRs collection in Firestore.
     @param id The ID of the user to add the comment to.
     @param comment The comment to add to the user's comments collection.
     */

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
        });
    }
}