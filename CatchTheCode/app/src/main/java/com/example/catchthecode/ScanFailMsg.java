package com.example.catchthecode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 This class represents the Scan Fail Message that is displayed after a failed QR code scan. It extends the AppCompatActivity class.
 */
public class ScanFailMsg extends AppCompatActivity {

    /**
     This method is called when the activity is created. It sets the content view to scan_fail.xml and initializes the views.
     @param savedInstanceState the saved instance state of the activity
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_fail);
        initViews();
    }

    Button btnRetake;

    /**
     This method initializes the view and sets up a click listener for the "Retake" button, which restarts the QR code scanner.
     */
    private void initViews() {

        btnRetake = findViewById(R.id.btnRetake);

        btnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
