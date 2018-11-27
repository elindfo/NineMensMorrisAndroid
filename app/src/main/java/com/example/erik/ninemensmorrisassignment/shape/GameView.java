package com.example.erik.ninemensmorrisassignment.shape;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.ninemensmorrisassignment.R;
import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {

    private static final String TAG = "GameView";

    private Drawable background;
    private NineMensMorrisGame model;

    private int width;
    private int height;

    private int from = -1;

    private int circleDiameter;

    private List<Piece> pieces;

    private Context context;

    private Piece selectedPiece = null;

    public GameView(Context context) {
        super(context);
        model = NineMensMorrisGame.getInstance();
        pieces = new ArrayList<>();
        background = getResources().getDrawable(R.drawable.morrisplayfield);
        model.reset();
        updatePieces();
        this.context = context;
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        model = NineMensMorrisGame.getInstance();
        pieces = new ArrayList<>();
        background = getResources().getDrawable(R.drawable.morrisplayfield);
        model.reset();
        updatePieces();
        this.context = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        circleDiameter = width / 7 < height / 7 ? width / 7 : height / 7;
    }

    @Override
    protected void onDraw(Canvas canvas){
        background.setBounds(0, 0, width, height);
        background.draw(canvas);

        for(Piece p : pieces){
            p.getDrawable().draw(canvas);
        }

        ((TextView)((Activity) context).findViewById(R.id.current_player_textview)).setText("Current Player: " + model.getCurrentPlayer());
        ((TextView)((Activity) context).findViewById(R.id.game_state_textview)).setText("State: " + model.getGameState());
        ((TextView)((Activity) context).findViewById(R.id.game_status_textview)).setText("Status: " + (model.getGameState() == NineMensMorrisGame.GameState.REMOVE_PIECE ? "Remove Piece" : "Move Piece"));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getAction()){
            case MotionEvent.ACTION_MOVE:{
                if(model.getGameState() == NineMensMorrisGame.GameState.POST_INITIAL){
                    int x = (int)event.getX();
                    int y = (int)event.getY();
                    if(selectedPiece != null){
                        selectedPiece.getDrawable().setBounds(x - circleDiameter / 2, y - circleDiameter / 2, x + circleDiameter / 2, y + circleDiameter / 2);
                        invalidate();
                    }
                }
                //Log.d(TAG, "ACTION_MOVE - x: " + x + ", y: " + y);
                break;
            }
            case MotionEvent.ACTION_DOWN:{
                int x = (int)event.getX();
                int y = (int)event.getY();
                for(Piece p : pieces){
                    if(p.getPlayer() == model.getCurrentPlayer() && p.cellIndex == getCellIndex(getRowCol(x, y))){
                        selectedPiece = p;
                    }
                }

                from = getCellIndex(getRowCol(x, y));
                break;
            }
            case MotionEvent.ACTION_UP:{
                int x = (int)event.getX();
                int y = (int)event.getY();
                int to = getCellIndex(getRowCol(x, y));
                Log.d(TAG, "ACTION_UP - gamestate: " + model.getGameState());
                if(model.getGameState() == NineMensMorrisGame.GameState.INITIAL){
                    if(to != -1){
                        boolean legalMove = model.makeInitialMove(to);
                        if(legalMove){
                            if(model.isPartOfThreeInARow(to)){
                                Log.d(TAG, "ACTION_UP - partOfThreeInARow");
                                model.setGameState(NineMensMorrisGame.GameState.REMOVE_PIECE);
                            }
                            else{
                                model.nextPlayer();
                            }
                        }
                    }
                }
                else if(model.getGameState() == NineMensMorrisGame.GameState.REMOVE_PIECE){
                    if(to != -1){
                        if(model.remove(to)){
                            model.restoreGameState();
                            if(model.isWinner()){
                                Toast.makeText(context, "Player: " + model.getCurrentPlayer() + " is the WINNER", Toast.LENGTH_SHORT).show();
                                model.setGameState(NineMensMorrisGame.GameState.FINISHED);
                            }
                            else{
                                model.nextPlayer();
                            }
                        }
                    }
                }
                else{
                    if(to != -1 && from != -1){
                        boolean legalMove = model.makeMove(to, from);
                        if(legalMove){
                            if(model.isPartOfThreeInARow(to)){
                                Log.d(TAG, "ACTION_UP - partOfThreeInARow");
                                model.setGameState(NineMensMorrisGame.GameState.REMOVE_PIECE);
                            }
                            else{
                                model.nextPlayer();
                            }
                        }
                    }
                }
                updatePieces();
            }
            invalidate();
        }
        return true;
    }

    private int[] getRowCol(int x, int y){
        int row = - 1, col = - 1;
        if(x < (width * (7.0 / 7))){
            col = 7;
        }
        if(x < (width * (6.0 / 7))){
            col = 6;
        }
        if(x < (width * (5.0 / 7))){
            col = 5;
        }
        if(x < (width * (4.0 / 7))){
            col = 4;
        }
        if(x < (width * (3.0 / 7))){
            col = 3;
        }
        if(x < (width * (2.0 / 7))){
            col = 2;
        }
        if(x < (width * (1.0 / 7))){
            col = 1;
        }
        if(y < (height * (7.0 / 7))){
            row = 7;
        }
        if(y < (height * (6.0 / 7))){
            row = 6;
        }
        if(y < (height * (5.0 / 7))){
            row = 5;
        }
        if(y < (height * (4.0 / 7))){
            row = 4;
        }
        if(y < (height * (3.0 / 7))){
            row = 3;
        }
        if(y < (height * (2.0 / 7))){
            row = 2;
        }
        if(y < (height * (1.0 / 7))){
            row = 1;
        }
        return new int[]{row, col};
    }

    private int getCellIndex(int[] coords){
        int row = coords[0];
        int col = coords[1];
        if(row == 3 && col == 3){
            return 1;
        }
        if(row == 2 && col == 2){
            return 2;
        }
        if(row == 1 && col == 1){
            return 3;
        }
        if(row == 3 && col == 4){
            return 4;
        }
        if(row == 2 && col == 4){
            return 5;
        }
        if(row == 1 && col == 4){
            return 6;
        }
        if(row == 3 && col == 5){
            return 7;
        }
        if(row == 2 && col == 6){
            return 8;
        }
        if(row == 1 && col == 7){
            return 9;
        }
        if(row == 4 && col == 5){
            return 10;
        }
        if(row == 4 && col == 6){
            return 11;
        }
        if(row == 4 && col == 7){
            return 12;
        }
        if(row == 5 && col == 5){
            return 13;
        }
        if(row == 6 && col == 6){
            return 14;
        }
        if(row == 7 && col == 7){
            return 15;
        }
        if(row == 5 && col == 4){
            return 16;
        }
        if(row == 6 && col == 4){
            return 17;
        }
        if(row == 7 && col == 4){
            return 18;
        }
        if(row == 5 && col == 3){
            return 19;
        }
        if(row == 6 && col == 2){
            return 20;
        }
        if(row == 7 && col == 1){
            return 21;
        }
        if(row == 4 && col == 3){
            return 22;
        }
        if(row == 4 && col == 2){
            return 23;
        }
        if(row == 4 && col == 1){
            return 24;
        }
        return -1;
    }

    private int[] getRowColFromCellIndex(int cellIndex){
        if(cellIndex == 1) return new int[]{3, 3};
        if(cellIndex == 2) return new int[]{2, 2};
        if(cellIndex == 3) return new int[]{1, 1};
        if(cellIndex == 4) return new int[]{3, 4};
        if(cellIndex == 5) return new int[]{2, 4};
        if(cellIndex == 6) return new int[]{1, 4};
        if(cellIndex == 7) return new int[]{3, 5};
        if(cellIndex == 8) return new int[]{2, 6};
        if(cellIndex == 9) return new int[]{1, 7};
        if(cellIndex == 10) return new int[]{4, 5};
        if(cellIndex == 11) return new int[]{4, 6};
        if(cellIndex == 12) return new int[]{4, 7};
        if(cellIndex == 13) return new int[]{5, 5};
        if(cellIndex == 14) return new int[]{6, 6};
        if(cellIndex == 15) return new int[]{7, 7};
        if(cellIndex == 16) return new int[]{5, 4};
        if(cellIndex == 17) return new int[]{6, 4};
        if(cellIndex == 18) return new int[]{7, 4};
        if(cellIndex == 19) return new int[]{5, 3};
        if(cellIndex == 20) return new int[]{6, 2};
        if(cellIndex == 21) return new int[]{7, 1};
        if(cellIndex == 22) return new int[]{4, 3};
        if(cellIndex == 23) return new int[]{4, 2};
        if(cellIndex == 24) return new int[]{4, 1};
        return new int[]{-1, -1};
    }

    private int[] getPiecePlacementCoordsFromRowCol(int[] rowCol){
        int row = rowCol[0];
        int col = rowCol[1];
        if(row == 1 && col == 1) return new int[]{0, 0};
        if(row == 1 && col == 4) return new int[]{circleDiameter * 3, 0};
        if(row == 1 && col == 7) return new int[]{circleDiameter * 6, 0};
        if(row == 2 && col == 2) return new int[]{circleDiameter * 1, circleDiameter};
        if(row == 2 && col == 4) return new int[]{circleDiameter * 3, circleDiameter};
        if(row == 2 && col == 6) return new int[]{circleDiameter * 5, circleDiameter};
        if(row == 3 && col == 3) return new int[]{circleDiameter * 2, circleDiameter * 2};
        if(row == 3 && col == 4) return new int[]{circleDiameter * 3, circleDiameter * 2};
        if(row == 3 && col == 5) return new int[]{circleDiameter * 4, circleDiameter * 2};
        if(row == 4 && col == 1) return new int[]{0, circleDiameter * 3};
        if(row == 4 && col == 2) return new int[]{circleDiameter * 1, circleDiameter * 3};
        if(row == 4 && col == 3) return new int[]{circleDiameter * 2, circleDiameter * 3};
        if(row == 4 && col == 5) return new int[]{circleDiameter * 4, circleDiameter * 3};
        if(row == 4 && col == 6) return new int[]{circleDiameter * 5, circleDiameter * 3};
        if(row == 4 && col == 7) return new int[]{circleDiameter * 6, circleDiameter * 3};
        if(row == 5 && col == 3) return new int[]{circleDiameter * 2, circleDiameter * 4};
        if(row == 5 && col == 4) return new int[]{circleDiameter * 3, circleDiameter * 4};
        if(row == 5 && col == 5) return new int[]{circleDiameter * 4, circleDiameter * 4};
        if(row == 6 && col == 2) return new int[]{circleDiameter * 1, circleDiameter * 5};
        if(row == 6 && col == 4) return new int[]{circleDiameter * 3, circleDiameter * 5};
        if(row == 6 && col == 6) return new int[]{circleDiameter * 5, circleDiameter * 5};
        if(row == 7 && col == 1) return new int[]{0, circleDiameter * 6};
        if(row == 7 && col == 4) return new int[]{circleDiameter * 3, circleDiameter * 6};
        if(row == 7 && col == 7) return new int[]{circleDiameter * 6, circleDiameter * 6};
        return new int[]{10, 10};
    }

    private class Piece{
        private Drawable drawable;
        private int cellIndex;
        private NineMensMorrisGame.Player player;

        public Piece(Drawable drawable, int cellIndex, NineMensMorrisGame.Player player) {
            this.drawable = drawable;
            this.cellIndex = cellIndex;
            this.player = player;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public int getCellIndex() {
            return cellIndex;
        }

        public void setCellIndex(int cellIndex) {
            this.cellIndex = cellIndex;
        }

        public NineMensMorrisGame.Player getPlayer() {
            return player;
        }

        public void setPlayer(NineMensMorrisGame.Player player) {
            this.player = player;
        }
    }

    public void reset(){
        model.reset();
        pieces = new ArrayList<>();
        invalidate();
    }

    private void updatePieces(){
        List<Piece> newList = new ArrayList<>();
        for(int i = 1; i < model.getPlayfield().length; i++){
            int[] coords = getPiecePlacementCoordsFromRowCol(getRowColFromCellIndex(i));
            if(model.getPlayfield()[i] == NineMensMorrisGame.PlayfieldPosition.BLUE){
                Drawable d = getResources().getDrawable(R.drawable.blue_circle);
                d.setBounds(coords[0], coords[1], coords[0] + circleDiameter, coords[1] + circleDiameter);
                newList.add(new Piece(d, i, NineMensMorrisGame.Player.BLUE));
            }
            if(model.getPlayfield()[i] == NineMensMorrisGame.PlayfieldPosition.RED){
                Drawable d = getResources().getDrawable(R.drawable.red_circle);
                d.setBounds(coords[0], coords[1], coords[0] + circleDiameter, coords[1] + circleDiameter);
                newList.add(new Piece(d, i, NineMensMorrisGame.Player.RED));
            }
        }
        pieces = newList;
    }
}
