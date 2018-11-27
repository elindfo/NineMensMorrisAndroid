package com.example.erik.ninemensmorrisassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;
import com.example.erik.ninemensmorrisassignment.shape.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);
        resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener((v) -> {
            NineMensMorrisGame.getInstance().reset();
            gameView.invalidate();
        });
    }
}
