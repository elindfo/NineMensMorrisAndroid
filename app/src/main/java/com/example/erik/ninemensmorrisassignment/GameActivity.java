package com.example.erik.ninemensmorrisassignment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;
import com.example.erik.ninemensmorrisassignment.model.SaveTool;
import com.example.erik.ninemensmorrisassignment.shape.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Button resetButton;
    private TextView currentPlayerText;

    private static final String FILE_MODEL = "model.ser";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.game_view);
        resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener((v) -> {
            gameView.reset();
        });

       /* if(savedInstanceState != null
                && savedInstanceState.getSerializable("model") != null){
            Log.d("LogAppTest", "savedInstanceState not null");
            gameView.setModel((NineMensMorrisGame)savedInstanceState.getSerializable("model"));
        }*/

        currentPlayerText = findViewById(R.id.current_player_textview);
    }

    @Override
    protected void onStart() {
        NineMensMorrisGame.getInstance().saveModel(SaveTool.getInstance(this).loadModel());
        super.onStart();
    }

    @Override
    protected void onStop() {
        SaveTool.getInstance(this).saveModel(NineMensMorrisGame.getInstance().getPlayfield());
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void updateText(String text){
        currentPlayerText.setText(text);
    }
}
