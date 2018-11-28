package com.example.erik.ninemensmorrisassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;
import com.example.erik.ninemensmorrisassignment.shape.GameView;

import static com.example.erik.ninemensmorrisassignment.R.menu.menu;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Button resetButton;
    private TextView currentPlayerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameView = findViewById(R.id.game_view);
        currentPlayerText = findViewById(R.id.current_player_textview);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                gameView.reset();
                gameView.invalidate();
                Toast.makeText(this, "New game started", Toast.LENGTH_LONG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void updateText(String text){
        currentPlayerText.setText(text);
    }
}
