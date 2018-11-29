package com.example.erik.ninemensmorrisassignment.model;

import java.io.Serializable;

public class GameData implements Serializable {

    private NineMensMorrisGame.PlayfieldPosition[] playfield;
    private int blueMarker, redMarker;
    private int redMarkersPlaced, blueMarkersPlaced;
    private NineMensMorrisGame.Player currentPlayer; // player in currentPlayer
    private NineMensMorrisGame.GameState gameState;

    public GameData(NineMensMorrisGame.PlayfieldPosition[] playfield, int blueMarker, int redMarker, int redMarkersPlaced, int blueMarkersPlaced, NineMensMorrisGame.Player currentPlayer, NineMensMorrisGame.GameState gameState) {
        this.playfield = playfield;
        this.blueMarker = blueMarker;
        this.redMarker = redMarker;
        this.redMarkersPlaced = redMarkersPlaced;
        this.blueMarkersPlaced = blueMarkersPlaced;
        this.currentPlayer = currentPlayer;
        this.gameState = gameState;
    }

    public NineMensMorrisGame.PlayfieldPosition[] getPlayfield() {
        return playfield;
    }

    public int getBlueMarker() {
        return blueMarker;
    }

    public int getRedMarker() {
        return redMarker;
    }

    public int getRedMarkersPlaced() {
        return redMarkersPlaced;
    }

    public int getBlueMarkersPlaced() {
        return blueMarkersPlaced;
    }

    public NineMensMorrisGame.Player getCurrentPlayer() {
        return currentPlayer;
    }

    public NineMensMorrisGame.GameState getGameState() {
        return gameState;
    }
}
