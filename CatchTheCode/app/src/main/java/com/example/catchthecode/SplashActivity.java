/**
 The SplashActivity class is responsible for showing a splash screen for 3 seconds before
 launching the CollectionActivity.
 */
package com.example.catchthecode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * The SplashActivity class is responsible for showing a splash screen for 3 seconds before
 * We DID NOT implement this due to technical difficulties.
 */
public class SplashActivity extends AppCompatActivity {
    private Handler handler;

    /**
     * This method is called when the SplashActivity is first created. It sets the layout for the
     * activity and initializes the handler to launch the CollectionActivity after 3 seconds.
     *
     * @param savedInstanceState The saved instance state bundle, which is null if the activity
     *                           is being launched for the first time.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            /**
             * This method is called after the delay specified in postDelayed() has elapsed. It
             * launches the CollectionActivity and finishes the SplashActivity.
             */
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000); // Delay for 1 seconds before launching CollectionActivity
    }
}