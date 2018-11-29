package com.example.erik.ninemensmorrisassignment;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;
import com.example.erik.ninemensmorrisassignment.shape.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NineMensMorrisGame.getInstance().load();
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        NineMensMorrisGame.getInstance().save();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_refresh:
                gameView.reset();
                Toast.makeText(this, "New game started", Toast.LENGTH_LONG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
