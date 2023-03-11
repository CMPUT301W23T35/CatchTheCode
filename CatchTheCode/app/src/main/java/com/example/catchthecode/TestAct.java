package com.example.catchthecode;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.security.NoSuchAlgorithmException;

//TODO: refactor the name to "QRInfo"

/**
 * This class represents the QR code information.
 */
public class TestAct extends AppCompatActivity {
    /**
     * Called when the activity is starting. Initializes the view and generates the QR code image.
     * @param savedInstanceState the saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_success1);
        ImageView qr = findViewById(R.id.qrimg);
        String content = getIntent().getStringExtra("key");
        //TextView tv = findViewById(R.id.test_tv);
        //tv.setText(content);

        QRcode test = null;
        try {
            test = new QRcode(content, qr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        test.setImageview();


    }
}