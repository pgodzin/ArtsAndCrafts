package com.example.artsandcrafts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SearchActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");
        setTitle(query);
    }

}