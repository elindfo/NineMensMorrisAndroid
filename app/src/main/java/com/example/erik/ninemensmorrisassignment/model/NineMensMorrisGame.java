package com.example.erik.ninemensmorrisassignment.model;

import android.content.Context;
import android.util.Log;

import com.example.erik.ninemensmorrisassignment.shape.GameView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Singleton class handling the logic for a game of Nine Men's Morris
 * @author Jonas W�hsl�n, jwi@kth.se. 
 * Revised by Anders Lindstr�m, anderslm@kth.se
 * Revised by Erik Lindfors
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

public class NineMensMorrisGame {

	private static NineMensMorrisGame instance;

	/**
	 * Enum representing player colors
	 */
	public enum Player{
		RED, BLUE
	}

	/**
	 * Enum representing the playingfield tile values
	 */
	public enum PlayfieldPosition {
		RED, BLUE, NONE
	}

	/**
	 * Enum representing the different game states
	 */
	public enum GameState{
		INITIAL, POST_INITIAL, REMOVE_PIECE, FINISHED
	}

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

	/**
	 * Method used to serialize and store the current state of the game to device
	 * @return true if successful
	 */
	public boolean save(){
		Log.d(GameView.TAG, "Serialize: SAVE");
		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try {
			GameData data = new GameData(
					playfield,
					blueMarker,
					redMarker,
					redMarkersPlaced,
					blueMarkersPlaced,
					currentPlayer,
					gameState
			);
			fos = ApplicationContextProvider.getContext().openFileOutput("gamedata.ser", Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			os.writeObject(data);
			Log.d(GameView.TAG, "Serialize: SAVE - SUCCESSFUL");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.d(GameView.TAG, "Serialize: SAVE - NOT SUCCESSFUL");
		return false;
	}

	/**
	 * Method used to deserialize and retrieve the current state of the game from the device
	 * @return true if successful
	 */
	public boolean load(){
		Log.d(GameView.TAG, "Serialize: LOAD");
		FileInputStream fis = null;
		ObjectInputStream is = null;
		try {
			fis = ApplicationContextProvider.getContext().openFileInput("gamedata.ser");
			is = new ObjectInputStream(fis);
			GameData data = (GameData) is.readObject();
			playfield = data.getPlayfield();
			blueMarker = data.getBlueMarker();
			redMarker = data.getRedMarker();
			blueMarkersPlaced = data.getBlueMarkersPlaced();
			redMarkersPlaced = data.getRedMarkersPlaced();
			currentPlayer = data.getCurrentPlayer();
			gameState = data.getGameState();
			Log.d(GameView.TAG, "Serialize: LOAD - SUCCESSFUL");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.d(GameView.TAG, "Serialize: LOAD - NOT SUCCESSFUL");
		return false;
	}


	/**
	 * Reset the game to initial state
	 */
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

	/**
	 * Method used to move pieces while in GameState.INITIAL
	 * @param to
	 * @return true if move was successful
	 * @throws IllegalArgumentException
	 */
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
	 * Method used to move pieces while in GameState.POST_INITIAL
	 * @param to
	 * @param from
	 * @return true if move was successful
	 * @throws IllegalArgumentException
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
	 * Method used to check if a move will form a valid three-in-a-row
	 * @param to
	 * @return true if successful
	 * @throws IllegalArgumentException
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

	/**
	 * Method used to change to next player
	 */
	public void nextPlayer(){
		if(currentPlayer == Player.RED){
			currentPlayer = Player.BLUE;
		}
		else{
			currentPlayer = Player.RED;
		}
	}

	/**
	 * Method used to remove a piece from the board
	 * @param from
	 * @return true if successful
	 * @throws IllegalArgumentException
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
	 * Method used to check if the current player has won the game
	 * @return true if current player has won
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
	 * Method used to check if the current move is valid
	 * @param to
	 * @param from
	 * @return true if move is valid
	 * @throws IllegalArgumentException
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

	/**
	 * Method used to randomize a Player object
	 * @return Player
	 */
	private Player getRandomPlayer(){
		return (int)(Math.random() * 10) % 2 == 0 ? Player.RED : Player.BLUE;
	}

	/**
	 * Method used to return to the correct GameState from GameState.REMOVE_PIECE
	 */
	public void restoreGameState(){
		if(redMarkersPlaced >= 9 && blueMarkersPlaced >= 9){
			gameState = GameState.POST_INITIAL;
		}
		else{
			gameState = GameState.INITIAL;
		}
	}

	public Player getCurrentPlayer(){
		return currentPlayer;
	}

	public PlayfieldPosition[] getPlayfield(){
		return playfield;
	}

	public GameState getGameState(){
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}