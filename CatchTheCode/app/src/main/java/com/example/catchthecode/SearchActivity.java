package com.example.catchthecode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// This class runs the searching category(geo/name) page(searching_page.xml)
public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searching_page);
        initViews();
    }

    private void initViews() {
        Button btnNameSearch;

        btnNameSearch = findViewById(R.id.name_search_button);

        btnNameSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, SearchFriendsActivity.class);
                startActivity(intent);
            }
        });
    }
}