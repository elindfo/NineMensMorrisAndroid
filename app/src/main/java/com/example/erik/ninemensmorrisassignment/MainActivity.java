package com.example.erik.ninemensmorrisassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newGameButton = findViewById(R.id.main_activity_new_game_button);
        newGameButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
    }
}
