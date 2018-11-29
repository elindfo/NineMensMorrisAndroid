package com.example.erik.ninemensmorrisassignment.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.ninemensmorrisassignment.R;
import com.example.erik.ninemensmorrisassignment.model.NineMensMorrisGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Visual representation of the game Nine Men's Morris.
 * This class will visualize the playingfield and recognize touch events.
 * At runtime gameview will draw pieces on the playing field and convert a piece's coordinates to
 * a place number that the model can use in it's logic.
 */
public class GameView extends View {

    private Drawable background;
    private NineMensMorrisGame model;
    private int width;
    private int height;
    private int from = -1;
    private int circleDiameter;
    private List<Piece> pieces;
    private Context context;
    private Piece selectedPiece = null;

    /**
     * Construct and initialize an instance of GameView
     * @param context
     * @param attrs
     */
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        model = NineMensMorrisGame.getInstance();
        background = getResources().getDrawable(R.drawable.morrisplayfield);
        this.context = context;
    }

    /**
     * This method is called to compute the size of current the current view.
     * It sets width and height to correct values and updates the current placed
     * pieces.
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        circleDiameter = width / 7 < height / 7 ? width / 7 : height / 7;
        background.setBounds(0, 0, width, height);
        updatePieces();
    }

    /**
     * Method is called to update view. Alternates color on current player text and draws or redraws
     * everything currently on gameview.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas){

        background.draw(canvas);

        ((TextView)((Activity) context).findViewById(R.id.current_player_textview)).setText("Current Player: " + model.getCurrentPlayer());
        if(model.getCurrentPlayer() == NineMensMorrisGame.Player.BLUE){
            ((TextView)((Activity) context).findViewById(R.id.current_player_textview)).setTextColor(context.getColor(R.color.blue));
        } else {
            ((TextView)((Activity) context).findViewById(R.id.current_player_textview)).setTextColor(context.getColor(R.color.red));
        }
        ((TextView)((Activity) context).findViewById(R.id.game_state_textview)).setText("State: " + model.getGameState());
        ((TextView)((Activity) context).findViewById(R.id.game_status_textview)).setText("Status: " + (model.getGameState() == NineMensMorrisGame.GameState.REMOVE_PIECE ? "Remove Piece" : "Move Piece"));

        for(int i = 0; i < pieces.size(); i++){
            pieces.get(i).getDrawable().draw(canvas);
        }
    }

    /**
     * This method responds to touch events made on devices.
     * Checks what event happened and what state the game is in and acts accordingly to the rules
     * of nine men's morris.
     * Updates and invalidates view.
     * @param event
     * @return
     */
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
                if(model.getGameState() == NineMensMorrisGame.GameState.INITIAL){
                    if(to != -1){
                        boolean legalMove = model.makeInitialMove(to);
                        if(legalMove){
                            if(model.isPartOfThreeInARow(to)){
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

    /**
     * Method for getting row col int values, when we have width and height values from view.
     * playing field is divided in 7 columns and 7 rows.
     * @param x
     * @param y
     * @return
     */
    private int[] getRowCol(int x, int y){
        int row = - 1, col = - 1;
        if(x < (width * (7.0 / 7))) col = 7;
        if(x < (width * (6.0 / 7))) col = 6;
        if(x < (width * (5.0 / 7))) col = 5;
        if(x < (width * (4.0 / 7))) col = 4;
        if(x < (width * (3.0 / 7))) col = 3;
        if(x < (width * (2.0 / 7))) col = 2;
        if(x < (width * (1.0 / 7))) col = 1;
        if(y < (height * (7.0 / 7))) row = 7;
        if(y < (height * (6.0 / 7))) row = 6;
        if(y < (height * (5.0 / 7))) row = 5;
        if(y < (height * (4.0 / 7))) row = 4;
        if(y < (height * (3.0 / 7))) row = 3;
        if(y < (height * (2.0 / 7))) row = 2;
        if(y < (height * (1.0 / 7))) row = 1;
        return new int[]{row, col};
    }

    /**
     * Method returning a game point if row and col corresponds to a valid point.
     * not all coords (row, col) will return a valid point due to the playing field of NMM.
     *
     * @param coords
     * @return
     */
    private int getCellIndex(int[] coords){
        int row = coords[0];
        int col = coords[1];
        if(row == 3 && col == 3) return 1;
        if(row == 2 && col == 2) return 2;
        if(row == 1 && col == 1) return 3;
        if(row == 3 && col == 4) return 4;
        if(row == 2 && col == 4) return 5;
        if(row == 1 && col == 4) return 6;
        if(row == 3 && col == 5) return 7;
        if(row == 2 && col == 6) return 8;
        if(row == 1 && col == 7) return 9;
        if(row == 4 && col == 5) return 10;
        if(row == 4 && col == 6) return 11;
        if(row == 4 && col == 7) return 12;
        if(row == 5 && col == 5) return 13;
        if(row == 6 && col == 6) return 14;
        if(row == 7 && col == 7) return 15;
        if(row == 5 && col == 4) return 16;
        if(row == 6 && col == 4) return 17;
        if(row == 7 && col == 4) return 18;
        if(row == 5 && col == 3) return 19;
        if(row == 6 && col == 2) return 20;
        if(row == 7 && col == 1) return 21;
        if(row == 4 && col == 3) return 22;
        if(row == 4 && col == 2) return 23;
        if(row == 4 && col == 1) return 24;
        return -1;
    }

    /**
     * Method returning row and col from a playing field point.
     * @param cellIndex
     * @return
     */
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

    /**
     * Method returning the correct place to render a game piece at on the canvas, this
     * according to row and col.
     * @param rowCol
     * @return
     */
    private int[] getPiecePlacementCoordsFromRowCol(int[] rowCol){
        int row = rowCol[0];
        int col = rowCol[1];
        if(row == 1 && col == 1) return new int[]{0, 0};
        if(row == 1 && col == 4) return new int[]{0, (width / 7 + 1) * 3};
        if(row == 1 && col == 7) return new int[]{0, (width / 7) * 6};
        if(row == 2 && col == 2) return new int[]{(height / 7), (width / 7 + 1) * 1};
        if(row == 2 && col == 4) return new int[]{(height / 7), (width / 7 + 1) * 3};
        if(row == 2 && col == 6) return new int[]{(height / 7), (width / 7 + 1) * 5};
        if(row == 3 && col == 3) return new int[]{(height / 7) * 2, (width / 7 + 1) * 2};
        if(row == 3 && col == 4) return new int[]{(height / 7) * 2, (width / 7 + 1) * 3};
        if(row == 3 && col == 5) return new int[]{(height / 7) * 2, (width / 7 + 1) * 4};
        if(row == 4 && col == 1) return new int[]{(height / 7) * 3, 0};
        if(row == 4 && col == 2) return new int[]{(height / 7) * 3, (width / 7 + 1) * 1};
        if(row == 4 && col == 3) return new int[]{(height / 7) * 3, (width / 7 + 1) * 2};
        if(row == 4 && col == 5) return new int[]{(height / 7) * 3, (width / 7 + 1) * 4};
        if(row == 4 && col == 6) return new int[]{(height / 7) * 3, (width / 7 + 1) * 5};
        if(row == 4 && col == 7) return new int[]{(height / 7) * 3, (width / 7 + 1) * 6};
        if(row == 5 && col == 3) return new int[]{(height / 7) * 4, (width / 7 + 1) * 2};
        if(row == 5 && col == 4) return new int[]{(height / 7) * 4, (width / 7 + 1) * 3};
        if(row == 5 && col == 5) return new int[]{(height / 7) * 4, (width / 7 + 1) * 4};
        if(row == 6 && col == 2) return new int[]{(height / 7) * 5, (width / 7 + 1) * 1};
        if(row == 6 && col == 4) return new int[]{(height / 7) * 5, (width / 7 + 1) * 3};
        if(row == 6 && col == 6) return new int[]{(height / 7) * 5, (width / 7 + 1) * 5};
        if(row == 7 && col == 1) return new int[]{(height / 7) * 6, 0};
        if(row == 7 && col == 4) return new int[]{(height / 7) * 6, (width / 7 + 1) * 3};
        if(row == 7 && col == 7) return new int[]{(height / 7) * 6, (width / 7 + 1) * 6};
        return new int[]{10, 10};
    }

    /**
     * Method used to draw pieces on playing field according to the model playing field status.
     */
    private void updatePieces(){
        List<Piece> newList = new ArrayList<>();
        for(int i = 1; i < model.getPlayfield().length; i++){
            int[] coords = getPiecePlacementCoordsFromRowCol(getRowColFromCellIndex(i));
            if(model.getPlayfield()[i] == NineMensMorrisGame.PlayfieldPosition.BLUE){
                Drawable d = getResources().getDrawable(R.drawable.blue_circle);
                d.setBounds(coords[1], coords[0], coords[1] + circleDiameter, coords[0] + circleDiameter);
                newList.add(new Piece(d, i, NineMensMorrisGame.Player.BLUE));
            }
            if(model.getPlayfield()[i] == NineMensMorrisGame.PlayfieldPosition.RED){
                Drawable d = getResources().getDrawable(R.drawable.red_circle);
                d.setBounds(coords[1], coords[0], coords[1] + circleDiameter, coords[0] + circleDiameter);
                newList.add(new Piece(d, i, NineMensMorrisGame.Player.RED));
            }
        }
        pieces = newList;
    }

    /**
     * Method to reset model at new game.
     */
    public void reset(){
        model.reset();
        updatePieces();
        invalidate();
    }

    /**
     * Class representing the game pieces used on the playing field.
     */
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
}
