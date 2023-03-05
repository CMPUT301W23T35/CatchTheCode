package com.example.catchthecode;

// the activity that show the users' friends
// it follows the friend_page.xml

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);
    }
}
