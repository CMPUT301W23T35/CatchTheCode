package com.example.catchthecode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// This class runs the scan failed message after scanning. (scan_fail.xml)
/**
 * represent the scan fail message
 */
public class ScanFailMsg extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_fail);
        initViews();
    }

    Button btnRetake;

    /**
     * initialize the view and connect the button listeners
     */
    private void initViews() {

        btnRetake = findViewById(R.id.btnRetake);

        btnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanFailMsg.this, ScannedBarcodeActivity.class);
                startActivity(intent);
            }
        });
    }

}
