package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class QRCodeActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrRef = db.collection("QRs");
    CollectionReference userRef = db.collection("users");
    StorageReference sr = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_comment);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String userid = intent.getStringExtra("userid");
        //get the textView
        TextView nameText = findViewById(R.id.textViewName);
        ImageView qrImage = findViewById(R.id.imageViewQRImage);
        TextView spaceImage = findViewById(R.id.textQRString);
        Button commentButton = findViewById(R.id.comment_button);
        Button deleteButton = findViewById(R.id.buttonDelete);
        ListView playerListView = findViewById(R.id.scan_players);

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
        final String[] SHACode = new String[1];

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
                            current = new QRcode(url);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
//                        current.setImageview();
                        SHACode[0] = current.getSHA256();
                        spaceImage.setText(current.getQrVR());
                    }
                }

                StorageReference sref = sr
                        .child( "QRs/" + SHACode[0]+ ".jpg");
                try {
                    final File localFile = File.createTempFile(SHACode[0],"jpg");
                    sref.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(QRCodeActivity.this, "Picture Retrieved", Toast.LENGTH_SHORT);
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    qrImage.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(QRCodeActivity.this, "Error Occurred", Toast.LENGTH_SHORT);
                                }
                            });
                } catch (Exception e) {

                }

//        sref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downUri = task.getResult();
//                    String imageUrl = downUri.toString();
//
//                    Toast.makeText(QRCodeActivity.this, imageUrl , Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(QRCodeActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
                // to find the user who contains the code
                ArrayList<String> userList = new ArrayList<String>();
                userRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                        userList.clear();
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            String id = doc.getId();
                            if (doc.getData().get("qrLists") == null) {

                            } else {
                                if (((ArrayList<String>) doc.getData().get("qrLists")).contains(SHACode[0])) {
                                    userList.add((String) doc.getData().get("username"));
                                }
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, userList);
                        playerListView.setAdapter(adapter);
                    }
                });



                commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(QRCodeActivity.this, CommentActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("id", SHACode[0]);
                        startActivity(intent);

                    }
                });
//                ImageView commentImage = findViewById(R.id.imageView3);
                if (userid != null) {
                    // other user's qr code
                    deleteButton.setClickable(false);
                    commentButton.setClickable(false);
                    deleteButton.setVisibility(View.INVISIBLE);
                    commentButton.setVisibility(View.INVISIBLE);
//                    commentImage.setVisibility(View.INVISIBLE);
                } else {
                    deleteButton.setClickable(true);
                    deleteButton.setVisibility(View.VISIBLE);
                    commentButton.setClickable(true);
                    commentButton.setVisibility(View.VISIBLE);
//                    commentImage.setVisibility(View.VISIBLE);
                }
//                Button deleteButton = findViewById(R.id.buttonDelete);
                // Delete button in the collection_comment.xml.
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        DocumentReference docRef = db.collection("users").document(androidId);

                        // Update the document to remove the "field_name" field
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("qrLists", FieldValue.arrayRemove(SHACode[0]));

                        docRef.update(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Field deleted successfully!");
                                        Intent intent = new Intent();
                                        String newData = "wtf";
                                        intent.putExtra("updatedData", newData);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "Error deleting field: " + e.getMessage());
                                    }
                                });



                    }
                });

            }
        });
    }
}