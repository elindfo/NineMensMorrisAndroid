package com.example.erik.ninemensmorrisassignment.controller;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.erik.ninemensmorrisassignment.R;

/**
 * Activity containing welcome screen with name of app and a menu button for resuming / new game.
 * This activity is the starting activity.
 */
public class MainActivity extends AppCompatActivity {

    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.main_contraint_layout);
        layout.setOnClickListener((v) -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
    }
}
