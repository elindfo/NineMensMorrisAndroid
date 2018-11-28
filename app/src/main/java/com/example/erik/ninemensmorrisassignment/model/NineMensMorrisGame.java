package com.example.erik.ninemensmorrisassignment.model;

import java.io.Serializable;

/**
 * @author Jonas W�hsl�n, jwi@kth.se. 
 * Revised by Anders Lindstr�m, anderslm@kth.se
 */

/*
 * The game board positions
 *
 * 03           06           09
 *     02       05       08
 *         01   04   07
 * 24  23  22        10  11  12
 *         19   16   13
 *     20       17       14
 * 21           18           15
 * 
 */

public class NineMensMorrisGame  {

	public static NineMensMorrisGame instance;

	public enum Player {
		RED, BLUE
	}

	public enum PlayfieldPosition implements Serializable{
		RED, BLUE, NONE
	}

	public enum GameState implements Serializable{
		INITIAL, POST_INITIAL, REMOVE_PIECE, FINISHED
	}

	private SaveTool saveTool;
	private PlayfieldPosition[] playfield;
	private int blueMarker, redMarker;
	private int redMarkersPlaced, blueMarkersPlaced;
	private Player currentPlayer; // player in currentPlayer
	private GameState gameState;

	private NineMensMorrisGame() {
		reset();
	}

	public static NineMensMorrisGame getInstance(){
		if(instance == null){
			instance = new NineMensMorrisGame();
		}
		return instance;
	}

	public PlayfieldPosition[] getPlayfield(){
		return playfield;
	}

	public void reset(){
		playfield = new PlayfieldPosition[25]; // zeroes
		for(int i = 0; i < playfield.length; i++){
			playfield[i] = PlayfieldPosition.NONE;
		}
		redMarker = blueMarker = 9;
		redMarkersPlaced = blueMarkersPlaced = 0;
		gameState = GameState.INITIAL;
		currentPlayer = getRandomPlayer();
	}

	public GameState getGameState(){
		return gameState;
	}

	public boolean makeInitialMove(int to) throws IllegalArgumentException{
		if(gameState == GameState.INITIAL) {
			if (to < 0 || to >= playfield.length) {
				throw new IllegalArgumentException();
			}
			if (currentPlayer == Player.RED) {
				if (redMarkersPlaced < 9) {
					if (playfield[to] == PlayfieldPosition.NONE) {
						playfield[to] = PlayfieldPosition.RED;
						redMarkersPlaced++;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (blueMarkersPlaced < 9) {
					if (playfield[to] == PlayfieldPosition.NONE) {
						playfield[to] = PlayfieldPosition.BLUE;
						blueMarkersPlaced++;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
			if(redMarkersPlaced >= 9 && blueMarkersPlaced >= 9){
				gameState = GameState.POST_INITIAL;
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns true if a move is successful
	 */
	public boolean makeMove(int to, int from) throws IllegalArgumentException{
		if(gameState == GameState.POST_INITIAL){
			if(to < 0 || to >= playfield.length || from < 0 || from >= playfield.length){
				throw new IllegalArgumentException();
			}

			if (currentPlayer == Player.RED) {
				if (playfield[to] == PlayfieldPosition.NONE && playfield[from] == PlayfieldPosition.RED) {
					if (isValidMove(to, from)) {
						playfield[to] = PlayfieldPosition.RED;
						playfield[from] = PlayfieldPosition.NONE;
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (playfield[to] == PlayfieldPosition.NONE && playfield[from] == PlayfieldPosition.BLUE) {
					boolean valid = isValidMove(to, from);
					if (valid == true) {
						playfield[to] = PlayfieldPosition.BLUE;
						playfield[from] = PlayfieldPosition.NONE;
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if position "to" is part of three in a row.
	 */
	public boolean isPartOfThreeInARow(int to) throws IllegalArgumentException{
		if(to < 0 || to >= playfield.length){
			throw new IllegalArgumentException();
		}

		if ((to == 1 || to == 4 || to == 7) && playfield[1] == playfield[4]
				&& playfield[4] == playfield[7]) {
			return true;
		} else if ((to == 2 || to == 5 || to == 8)
				&& playfield[2] == playfield[5] && playfield[5] == playfield[8]) {
			return true;
		} else if ((to == 3 || to == 6 || to == 9)
				&& playfield[3] == playfield[6] && playfield[6] == playfield[9]) {
			return true;
		} else if ((to == 7 || to == 10 || to == 13)
				&& playfield[7] == playfield[10] && playfield[10] == playfield[13]) {
			return true;
		} else if ((to == 8 || to == 11 || to == 14)
				&& playfield[8] == playfield[11] && playfield[11] == playfield[14]) {
			return true;
		} else if ((to == 9 || to == 12 || to == 15) 
				&& playfield[9] == playfield[12] && playfield[12] == playfield[15]) {
			return true;
		} else if ((to == 13 || to == 16 || to == 19)
				&& playfield[13] == playfield[16] && playfield[16] == playfield[19]) {
			return true;
		} else if ((to == 14 || to == 17 || to == 20)
				&& playfield[14] == playfield[17] && playfield[17] == playfield[20]) {
			return true;
		} else if ((to == 15 || to == 18 || to == 21)
				&& playfield[15] == playfield[18] && playfield[18] == playfield[21]) {
			return true;
		} else if ((to == 1 || to == 22 || to == 19)
				&& playfield[1] == playfield[22] && playfield[22] == playfield[19]) {
			return true;
		} else if ((to == 2 || to == 23 || to == 20)
				&& playfield[2] == playfield[23] && playfield[23] == playfield[20]) {
			return true;
		} else if ((to == 3 || to == 24 || to == 21)
				&& playfield[3] == playfield[24] && playfield[24] == playfield[21]) {
			return true;
		} else if ((to == 22 || to == 23 || to == 24)
				&& playfield[22] == playfield[23] && playfield[23] == playfield[24]) {
			return true;
		} else if ((to == 4 || to == 5 || to == 6)
				&& playfield[4] == playfield[5] && playfield[5] == playfield[6]) {
			return true;
		} else if ((to == 10 || to == 11 || to == 12)
				&& playfield[10] == playfield[11] && playfield[11] == playfield[12]) {
			return true;
		} else if ((to == 16 || to == 17 || to == 18)
				&& playfield[16] == playfield[17] && playfield[17] == playfield[18]) {
			return true;
		}
		return false;
	}

	public void nextPlayer(){
		if(currentPlayer == Player.RED){
			currentPlayer = Player.BLUE;
		}
		else{
			currentPlayer = Player.RED;
		}
	}

	/**
	 * Request to remove a marker for the selected player.
	 * Returns true if the marker where successfully removed
	 */
	public boolean remove(int from) throws IllegalArgumentException{
		if(from < 0 || from >= playfield.length){
			throw new IllegalArgumentException();
		}
		if(currentPlayer == Player.RED){
			if(playfield[from] == PlayfieldPosition.BLUE){
				playfield[from] = PlayfieldPosition.NONE;
				blueMarker--;
				return true;
			}
		}
		else{
			if(playfield[from] == PlayfieldPosition.RED){
				playfield[from] = PlayfieldPosition.NONE;
				redMarker--;
				return true;
			}
		}
		return false;
	}

	/**
	 *  Returns true if the selected player have less than three markers left.
	 */
	public boolean isWinner() {
		if(currentPlayer == Player.RED){
			if(blueMarker < 3){
				return true;
			}
		}
		else{
			if(redMarker < 3){
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns EMPTY_SPACE = 0 BLUE_MARKER = 4 READ_MARKER = 5
	 */
	public PlayfieldPosition board(int from) throws IllegalArgumentException{
		if(from < 0 || from >= playfield.length){
			throw new IllegalArgumentException();
		}
		return playfield[from];
	}
	
	/**
	 * Check whether this is a legal move.
	 */
	private boolean isValidMove(int to, int from) throws IllegalArgumentException{
		if(to < 0 || to >= playfield.length || from < 0 || from >= playfield.length){
			throw new IllegalArgumentException();
		}
		
		if(this.playfield[to] != PlayfieldPosition.NONE) return false;
		
		switch (to) {
		case 1:
			return (from == 4 || from == 22);
		case 2:
			return (from == 5 || from == 23);
		case 3:
			return (from == 6 || from == 24);
		case 4:
			return (from == 1 || from == 7 || from == 5);
		case 5:
			return (from == 4 || from == 6 || from == 2 || from == 8);
		case 6:
			return (from == 3 || from == 5 || from == 9);
		case 7:
			return (from == 4 || from == 10);
		case 8:
			return (from == 5 || from == 11);
		case 9:
			return (from == 6 || from == 12);
		case 10:
			return (from == 11 || from == 7 || from == 13);
		case 11:
			return (from == 10 || from == 12 || from == 8 || from == 14);
		case 12:
			return (from == 11 || from == 15 || from == 9);
		case 13:
			return (from == 16 || from == 10);
		case 14:
			return (from == 11 || from == 17);
		case 15:
			return (from == 12 || from == 18);
		case 16:
			return (from == 13 || from == 17 || from == 19);
		case 17:
			return (from == 14 || from == 16 || from == 20 || from == 18);
		case 18:
			return (from == 17 || from == 15 || from == 21);
		case 19:
			return (from == 16 || from == 22);
		case 20:
			return (from == 17 || from == 23);
		case 21:
			return (from == 18 || from == 24);
		case 22:
			return (from == 1 || from == 19 || from == 23);
		case 23:
			return (from == 22 || from == 2 || from == 20 || from == 24);
		case 24:
			return (from == 3 || from == 21 || from == 23);
		}
		return false;
	}

	public Player getCurrentPlayer(){
		return currentPlayer;
	}

	private Player getRandomPlayer(){
		return (int)(Math.random() * 10) % 2 == 0 ? Player.RED : Player.BLUE;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public void restoreGameState(){
		if(redMarkersPlaced >= 9 && blueMarkersPlaced >= 9){
			gameState = GameState.POST_INITIAL;
		}
		else{
			gameState = GameState.INITIAL;
		}
	}

	public void saveModel(PlayfieldPosition[] playfieldPositions){
		if(playfieldPositions != null){
			playfield = playfieldPositions;
		}
	}

	public void printGame(){
		System.out.println("Current player: " + currentPlayer);
		System.out.println("Game State: " + gameState);
		System.out.println("Placed on board - Red: " + redMarkersPlaced + ", Blue: " + blueMarkersPlaced);
		System.out.println();
		System.out.printf("%-5s%-5s%-5s%-5s%-5s%-5s%-5s\n", playfield[3], "", "", playfield[6], "", "", playfield[9]);
		System.out.printf("%-5s%-5s%-5s%-5s%-5s%-5s%-5s\n", "", playfield[2], "", playfield[5], "", playfield[8], "");
		System.out.printf("%-5s%-5s%-5s%-5s%-5s%-5s%-5s\n", "", "", playfield[1], playfield[4], playfield[7], "", "");
		System.out.printf("%-5s%-5s%-5s%-5s%-5s%-5s%-5s\n", playfield[24], playfield[23], playfield[22], "", playfield[10], playfield[11], playfield[12]);
		System.out.printf("%-5s%-5s%-5s%-5s%-5s%-5s%-5s\n", "", "", playfield[19], playfield[16], playfield[13], "", "");
		System.out.printf("%-5s%-5s%-5s%-5s%-5s%-5s%-5s\n", "", playfield[20], "", playfield[17], "", playfield[14], "");
		System.out.printf("%-5s%-5s%-5s%-5s%-5s%-5s%-5s\n", playfield[21], "", "", playfield[18], "", "", playfield[15]);
	}
}