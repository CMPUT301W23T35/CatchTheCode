package com.example.catchthecode;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**

 Represents an activity to display a success message after scanning a QR code.
 */
public class ScanSuccessMsg extends AppCompatActivity {

    /**
     * Creates the activity and sets its layout to display the success message.
     * @param savedInstanceState contains data that was saved if the activity is being re-initialized.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_success);
    }
}
