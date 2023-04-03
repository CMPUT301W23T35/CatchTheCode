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


/**
 The QRCodeActivity class displays the details of a specific QR code, including its name, image,
 comments, and the users who have scanned it.
 */
public class QRCodeActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrRef = db.collection("QRs");
    CollectionReference userRef = db.collection("users");
    StorageReference sr = FirebaseStorage.getInstance().getReference();
    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     * @see AppCompatActivity#onCreate(Bundle)
     */
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

        final String[] SHACode = new String[1];

        qrRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * execute the query
             * @param queryDocumentSnapshots
             * @param error
             */
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

                // to find the user who contains the code
                ArrayList<String> userList = new ArrayList<String>();
                /**
                 * execute the query
                 * @param queryDocumentSnapshots
                 * @param error
                 */
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
                    /**
                     * when the button is clicked, change to comment activity
                     * @param view
                     */
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(QRCodeActivity.this, CommentActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("id", SHACode[0]);
                        if (userid != null) {
                            intent.putExtra("allowComment", "0");
                        } else {
                            intent.putExtra("allowComment", "1");
                        }
                        startActivity(intent);

                    }
                });
//                ImageView commentImage = findViewById(R.id.imageView3);
                if (userid != null) {
                    // other user's qr code
                    deleteButton.setClickable(false);
                    commentButton.setText("VIEW COMMENTS");
                    deleteButton.setVisibility(View.INVISIBLE);
                } else {
                    deleteButton.setClickable(true);
                    deleteButton.setVisibility(View.VISIBLE);
                    commentButton.setClickable(true);
                    commentButton.setVisibility(View.VISIBLE);
                }

                // Delete button in the collection_comment.xml.
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    /**
                     * when the button is clicked, delete the code
                     * @param v
                     */
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

                // add a listener for button comment_location_button, pass the current longitude and latitude to the MapActivity
                Button commentLocationButton = findViewById(R.id.comment_location_button);
                commentLocationButton.setOnClickListener(new View.OnClickListener() {
                    /**
                     * called when the button is clicked
                     * @param view
                     */
                    @Override
                    public void onClick(View view) {
                        // get the longitude and latitude from firebase for the QR code has the name "name"
                        // pass the longitude and latitude to the MapActivity
                        db.collection("QRs").whereEqualTo("readable_name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        String longitude = (String) document.getData().get("longitude");
                                        String latitude = (String) document.getData().get("latitude");
                                        Log.e(TAG, "longitude: " + longitude);
                                        Log.e(TAG, "latitude: " + latitude);
                                        try{
                                            double lon = Double.parseDouble(longitude);
                                            double lat = Double.parseDouble(latitude);
                                            Intent intent = new Intent(QRCodeActivity.this, MapsActivity.class);
                                            intent.putExtra("lon", lon);
                                            intent.putExtra("lat", lat);
                                            startActivity(intent);
                                        }
                                        catch (Exception e){
                                            // Log the exception
                                            Log.e ("Exception", e.toString());
                                            Toast.makeText(QRCodeActivity.this, "No location information", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}