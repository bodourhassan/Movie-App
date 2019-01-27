package com.example.android.worldmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class detaildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);
        //Receive the sent Bundle
        Intent sentIntent = getIntent();
        Bundle sentBundle = sentIntent.getExtras();
        //Inflate Details Fragment & Send the Bundle to it
        detail_fragment mDetailsFragment = new detail_fragment();
        mDetailsFragment.setArguments(sentBundle);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail, mDetailsFragment)
                    .commit();
        }
    }

}