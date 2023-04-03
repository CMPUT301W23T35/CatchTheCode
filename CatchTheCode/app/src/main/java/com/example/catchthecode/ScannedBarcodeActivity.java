package com.example.catchthecode;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
/**
 * represent a qr code scanner that can automatically scan the qr code
 */
public class ScannedBarcodeActivity extends AppCompatActivity {

    private static final int IMAGE_CAPTURE_CODE = 202;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //Button btnAction;
    Button btnScan;
    String intentData = "";
    boolean isEmail = false;

    Uri image_uri;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}.
     * @see AppCompatActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initViews();
    }
    /**
     Initialize the views and set up button listeners.
     */
    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        //btnAction = findViewById(R.id.btnAction);
        btnScan = findViewById(R.id.btnScan);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScannedBarcodeActivity.this, ScanFailMsg.class);
                startActivity(intent);
            }
        });

        /*btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            *//*public void onClick(View v) {
                if (intentData.length() > 0) {
                    if (isEmail) {
                    }
                       //tartActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
               else {
                   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
               }
           }*//*


                // TODO: need to store it somewhere
                //  intentdata has the string representation of the scanned qr code

            public void onClick(View v){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){

                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, REQUEST_CAMERA_PERMISSION);
                }
                else {
                    // permission already granted
                        openCamera();
                }
            }
            else {
                // system os is less then marshmallow
            }

        }
    });*/
}
    /**

     Open the device's camera and set up a BarcodeDetector and CameraSource to scan QR codes.

     If a QR code is scanned successfully, the scanned data is stored in intentData.
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

    /**
     * request the permission
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**

     This method is called when an activity you launched exits, giving you the requestCode you started it with,
     the resultCode it returned, and any additional data from it. In this case, it is used to handle the result of
     an image capture activity and display the captured image in an image view.
     @param requestCode an integer request code originally supplied to startActivityForResult(),
     which allows to identify who this result came from.
     @param resultCode an integer result code returned by the child activity through its setResult().
     @param data an Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_CODE) {
                // set image captured to image view
                //imgView.setImageURI(image_uri);
                Log.d("image_uri", image_uri.toString());
            }
        }
    }


    /**

     This method initializes the barcode detector and camera source, starts the camera preview,

     and detects barcodes in real-time using the camera source. It displays a toast message to inform

     the user that the barcode scanner has started.
     */
    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        /**

         This method is called when the SurfaceView is created.
         It checks if the app has the required CAMERA permission. If the permission is granted, the cameraSource starts to stream the video from the camera to the surfaceView.
         If the permission is not granted, a request is sent to the user for the CAMERA permission.
         @param holder the SurfaceHolder of the SurfaceView
         */
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            /**

             Called when the surface's layout or format has been changed.
             @param holder The SurfaceHolder whose surface has changed.
             @param format The new PixelFormat of the surface.
             @param width The new width of the surface.
             @param height The new height of the surface.
             */
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            /**

             Called when the surface is being destroyed. Stops the camera source.
             @param holder The SurfaceHolder being destroyed.
             */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        /**

         Sets a new detector processor for the barcode detector.
         The processor's release() method displays a toast to notify the user
         that the barcode scanner has been stopped to prevent memory leaks.
         The receiveDetections() method receives a collection of detected items
         and displays their value on the txtBarcodeValue TextView.
         If the barcode is an email, it will display the score of the email.
         The method also sets a boolean value to determine if the scanned barcode is an email or not.
         @param detections A collection of detected items returned from the barcode detector
         */
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            /**

             This method is called when barcode detections are received by the detector.
             It sets the detected barcode value to the TextView and launches an activity when the scan button is clicked.
             @param detections the detected items by the detector.
             */
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {

                        /**
                         * This method is called when the Runnable is executed to set the barcode value to the TextView.
                         */
                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                try {
                                    intentData = "Score: " + getScore(barcodes.valueAt(0).displayValue);
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                txtBarcodeValue.setText(intentData);
                                isEmail = true;

                                // redundant
                            } else {
                                isEmail = false;
                                try {
                                    intentData = "Score: " + Integer.toString(getScore(barcodes.valueAt(0).displayValue));
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                txtBarcodeValue.setText(intentData);
                            }
                            /*
                            btnScan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(ScannedBarcodeActivity.this, ScanSuccessMsg.class);
                                    startActivity(intent);

                                }
                            });
                            */
                            // inflation testing

                            Button enter = findViewById(R.id.btnScan);
                            enter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ScannedBarcodeActivity.this, TestAct.class);

                                    String value = barcodes.valueAt(0).displayValue;
                                    intent.putExtra("key", value);
                                    startActivity(intent);
                                }
                            });
                        }

                    });

                }
                else{
                    txtBarcodeValue.setText("No barcode detected");
                    btnScan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ScannedBarcodeActivity.this, ScanFailMsg.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //txtBarcodeValue.setText("No barcode detected");
        initialiseDetectorsAndSources();

    }
    /**

     Calculates the score of a given QR code.
     @param code the string representation of the QR code to be scored
     @return the score of the QR code
     @throws NoSuchAlgorithmException if the hashing algorithm used by the QRcode class is not available
     */
    protected int getScore(String code) throws NoSuchAlgorithmException {

        /*// maybe an email address, maybe an url
        // TODO: hash function needed
        Log.d("myTag", code);

        int hash = 7;
        for (int i = 0; i < code.length(); i++) {
            hash = hash * 31 + code.charAt(i);
            hash = hash % 100;
        }
        return hash;*/

        QRcode input = new QRcode(code);
        return input.getqrScore();
    }


}
