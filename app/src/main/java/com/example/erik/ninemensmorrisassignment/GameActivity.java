package com.example.erik.ninemensmorrisassignment;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;
import com.example.erik.ninemensmorrisassignment.shape.GameView;

/**
 * Game activity holds the view that the game is played on.
 */
public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    /**
     * Will try to load saved state only first time game is started after it has been destroyed.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NineMensMorrisGame.getInstance().load();
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);
    }

    /**
     * Saves model state at activity destroyed.
     */
    @Override
    protected void onDestroy(){
        NineMensMorrisGame.getInstance().save();
        super.onDestroy();
    }

    /**
     * Inflates the menu with a reset button.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Method to respond to menu actions.
     * @param item
     * @return
     */
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
