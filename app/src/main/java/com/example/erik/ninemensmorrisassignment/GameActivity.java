package com.example.erik.ninemensmorrisassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;
import com.example.erik.ninemensmorrisassignment.shape.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Button resetButton;
    private TextView currentPlayerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.game_view);
        resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener((v) -> {
            gameView.reset();
        });

        currentPlayerText = findViewById(R.id.current_player_textview);
    }

    public void updateText(String text){
        currentPlayerText.setText(text);
    }
}
