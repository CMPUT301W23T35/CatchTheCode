package com.example.catchthecode;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// This class runs the success message after scanning.(scan_success.xml)
/**
 * represent the scan success message
 */
public class ScanSuccessMsg extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_success);
    }
}
