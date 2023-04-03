package com.example.catchthecode;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//TODO: refactor the name to "QRInfo"

/**
 * This class represents the QR code information.
 */
public class TestAct extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_REQUEST_CODE = 1122;
    int SELECT_PICTURE = 715;
    private static final int IMAGE_CAPTURE_CODE = 202;
    Uri image_uri;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef = db.collection("users");
    CollectionReference qrRef = db.collection("QRs");
    StorageReference sr = FirebaseStorage.getInstance().getReference("QRs");
    final boolean[] wPic = {false};
    final boolean[] wLoc = {false};
    QRcode test = null;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_success1);
        ImageView qr = findViewById(R.id.qrimg);
        String content = getIntent().getStringExtra("key");

        //TextView tv = findViewById(R.id.test_tv);
        //tv.setText(content);

        try {
            test = new QRcode(content, qr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        test.setImageview();

        Switch location = findViewById(R.id.location_switch);
        Switch picture = findViewById(R.id.picture_switch);

        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wLoc[0] = b;
                if (wLoc[0]){
                    Log.e(TAG, "wLoc");
                }
            }
        });

        picture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                wPic[0] = b;
                if (wPic[0]){
                    Log.e(TAG, "wPic");
                }
            }
        });

        //Button wLocation = findViewById(R.id.withLoc);
        //Button woLocation = findViewById(R.id.withoutLoc);
        Button confirm_button = findViewById(R.id.confirm_button);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        QRcode finalTest = test;

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // the user want to save the picture with location
                // get the current location
                if (wLoc[0]){
                    fillLastLocation(finalTest);
                }
                // TODO: somehow the message order is wrong, but it works perfectly
                Log.d(TAG, "3 "+finalTest.getLongitude());
                // fill the image
                if (wPic[0]) {
                    //chooseImage();

                    // take a picture and then auto upload it
                    openCamera();
                }

                String name = String.valueOf(finalTest.getSHA256());
                qrRef.document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(),"Success!", Toast.LENGTH_SHORT).show();
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Document exists!");
                                addToUserCollection(userRef, name, finalTest);
                                // update the document with new location information
                                // only if the document in database has "nolon" for longitude and "nolat" for latitude
                                if (document.getString("longitude").equals("noLon") && document.getString("latitude").equals("noLat")){
                                    // update the document
                                    qrRef.document(name).update("longitude", finalTest.getLongitude());
                                    qrRef.document(name).update("latitude", finalTest.getLatitude());
                                }
                            } else {
                                Log.d(TAG, "Document does not exist!");
                                // if the document does not exist
                                // create the document
                                addToQRCollection(qrRef, name, finalTest);
                                addToUserCollection(userRef, name, finalTest);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Ops! Something went wrong...", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                        if (!wPic[0]){
                            finish();
                        }
                    }
                });

            }
        });
        /*woLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // the user want to save the picture only
                // fill the image
                chooseImage();
            }
        });*/
    }



    private void chooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private void fillLastLocation(QRcode QR) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.e(TAG, "yeahaAA");
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null){
                                Geocoder geocoder = new Geocoder(TestAct.this, Locale.getDefault());
                                List<Address> addresses = null;

                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String latitude = String.valueOf(addresses.get(0).getLatitude());
                                String longitude = String.valueOf(addresses.get(0).getLongitude());
                                Log.e(TAG, "1 lat: "+latitude+"\tlon: "+longitude);
                                
                                // fill the location info
                                QR.setLocation(latitude, longitude);
                                Log.e(TAG, "2 see "+QR.getLongitude());
                                return;
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "fk up");
                        }
                    });
        }
        else{
            Log.e(TAG, "No permission to location access.");
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK){
            if (requestCode == SELECT_PICTURE) {
                Uri selectedUri = data.getData();
                Bitmap img = null;
                // turn the uri into bitmap form, and fill the QRcode object with it
                try {
                    img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                test.setImage(img);
                //ImageView qrimg = findViewById(R.id.qrimg);
                //qrimg.setImageBitmap(test.getImage());

                // the object has been filled with all necessary attributes, time to upload them
                uploadQR(test, selectedUri);
                Log.d(TAG, "after upload");
            }
        }*/

        // upload the photo right after taking it
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_CODE) {
                // set image captured to image view
                //imgView.setImageURI(image_uri);
                Log.d("image_uri", image_uri.toString());
                Bitmap img = null;
                // turn the uri into bitmap form, and fill the QRcode object with it
                try {
                    img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                test.setImage(img);
                //ImageView qrimg = findViewById(R.id.qrimg);
                //qrimg.setImageBitmap(test.getImage());

                // the object has been filled with all necessary attributes, time to upload them
                uploadPhoto(test, image_uri);
                Log.d(TAG, "after upload");
                finish();
            }
        }
    }
    /**
     * upload the photo to the firebase
     * @param input the QR code to be stored
     * @param uri the uri of the code
     */
    private void uploadPhoto(QRcode input, Uri uri) {

        String name = String.valueOf(input.getSHA256());
//        String name = String.valueOf(input.getqrName());
        StorageReference storeFile = sr.child(name + "." +getExtension(uri));

        // add to the QRS database

        // check if qr exist in the qrRef
        qrRef.document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    input.getImage().compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = storeFile.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.e(TAG, "BMP fail");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            Log.e(TAG, "BMP fine");
                        }
                    });
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });


    }

    /***
     * get image file extension
     * @param uri the uri of the target image file
     * @return the image extension
     */
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    /**
     * upload the code to the user collection in firebase
     * @param userRef the user reference in firebase
     * @param name the id of the QR code
     * @param input the object of the current QR code
     */
    private void addToUserCollection(CollectionReference userRef, String name, QRcode input) {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // add to the user database
        userRef.document(androidId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        // if the document does not exist
                        userRef.document(androidId).set(new HashMap<String, Object>());
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
        // check if name is already in the current user's array
        userRef.document(androidId).get(Source.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //check whether qrLists field is empty
                if (task.getResult().get("qrLists") == null) {
                    userRef.document(androidId).update(
                                    "qrLists", FieldValue.arrayUnion(name))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.e(TAG, input.getSHA256());
//                                    Log.e(TAG, "qr added to user list");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "qr failed to add to user list");
                                }
                            });
                }
                else{
                    List<String> qrList = (List<String>) task.getResult().get("qrLists");
                    boolean duplicate = false;
                    for (int i = 0; i < qrList.size(); i++) {
                        if (name == qrList.get(i)) duplicate = true;
                    }
                    if (!duplicate) {
                        userRef.document(androidId).update(
                                        "qrLists", FieldValue.arrayUnion(name))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.e(TAG, input.getSHA256());
                                        Log.e(TAG, "qr added to user list");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "qr failed to add to user list");
                                    }
                                });
                    } else {
                        Toast.makeText(TestAct.this, "You have already scanned this code.", Toast.LENGTH_SHORT);
                    }
                }
            } else {
                Log.d("CollectionActivity", "Error getting documents: ", task.getException());
            }
        });
    }
    /**
     * upload the code to the QR code collection in firebase
     * @param qrRef the QR code reference in firebase
     * @param name the id of the QR code
     * @param input the object of the current QR code
     */
    private void addToQRCollection(CollectionReference qrRef, String name, QRcode input) {
        Log.e(TAG, "try to upload data");
        qrRef.document(name).set(input.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e(TAG, "data fine");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "data fail");
                    }
                });
    }

    /**
     * open the camera on the phone
     */
    private void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

}

