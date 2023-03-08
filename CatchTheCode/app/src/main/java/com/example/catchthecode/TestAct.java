package com.example.catchthecode;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class TestAct extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_qr);
        ImageView qr = findViewById(R.id.qrimg);
        String content;
        content = "https://github.com";
        //String value = getIntent().getStringExtra("key");
        //content = getIntent().getStringExtra("key");
        //TextView tv = findViewById(R.id.test_tv);
        //tv.setText(content);

        QRcode test = new QRcode(content, qr);
        test.setImageview();


    }
}
