package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public class QRCodeActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrRef = db.collection("QRs");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_comment);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        //get the textView
        TextView nameText = findViewById(R.id.textViewName);
        ImageView qrImage = findViewById(R.id.imageViewQRImage);
        TextView spaceImage = findViewById(R.id.space);
        Button commentButton = findViewById(R.id.comment_button);



//        DocumentReference docRef = qrRef.document(name);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        String qrName = (String) document.getData().get("readable_name");
//                        nameText.setText(qrName);
//                        String url = (String) document.getData().get("url");
//                        QRcode current;
//                        try {
//                            current = new QRcode(url, qrImage);
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                        current.setImageview();
//                        spaceImage.setText(current.getQrVR());
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });


        qrRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
            FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    //FIXME: should we search based on readable_name or id?
                    if (((String) doc.getData().get("readable_name")).equals(name)) {
                        String qrName = (String) doc.getData().get("readable_name");
                        nameText.setText(qrName);
                        String url = (String) doc.getData().get("url");
                        QRcode current;
                        try {
                            current = new QRcode(url, qrImage);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        current.setImageview();
                        spaceImage.setText(current.getQrVR());
                    }
                }
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRCodeActivity.this, CommentActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });



    }
}