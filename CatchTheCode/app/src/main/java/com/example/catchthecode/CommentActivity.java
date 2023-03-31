
package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
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


        ListView listView = findViewById(R.id.commentsList);
        Button addCommentButton = findViewById(R.id.addCommentButton);

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


    }
}