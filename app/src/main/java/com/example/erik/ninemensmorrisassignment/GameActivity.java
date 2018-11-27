package com.example.erik.ninemensmorrisassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.erik.ninemensmorrisassignment.shape.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);
    }
}
