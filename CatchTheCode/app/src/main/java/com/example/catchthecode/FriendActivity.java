package com.example.catchthecode;

// the activity that show the users' friends
// it follows the friend_page.xml

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FriendActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_page);
    }
}
