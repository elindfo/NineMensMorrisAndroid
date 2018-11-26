package com.example.erik.ninemensmorrisassignment.model;

import java.util.Scanner;

public class Main {

    private NineMensMorrisGame game = NineMensMorrisGame.getInstance();
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Main m = new Main();
        m.gameLoop();
    }

    private void gameLoop(){
        game.reset();
        while(true){
            game.printGame();
            System.out.println();
            if(game.getGameState() == NineMensMorrisGame.GameState.INITIAL){
                int i;
                boolean legalMove;
                do{
                    System.out.print("Put: ");
                    i = input.nextInt();
                    legalMove = game.makeInitialMove(i);
                    if(!legalMove){
                        System.out.println("Illegal move!");
                    }
                }while(!legalMove);
                if(game.isPartOfThreeInARow(i)){
                    game.printGame();
                    int removeIndex;
                    boolean validRemoval;
                    do{
                        System.out.print("Remove index: ");
                        removeIndex = input.nextInt();
                        validRemoval = game.remove(removeIndex);
                    }while(!validRemoval);
                }
            }
            else{
                int from, to;
                boolean legalMove;
                do{
                    System.out.print("From: ");
                    from = input.nextInt();
                    System.out.print("To: ");
                    to = input.nextInt();
                    legalMove = game.makeMove(to, from);
                    if(!legalMove){
                        System.out.println("Illegal move!");
                    }
                }while(!legalMove);

                if(game.isPartOfThreeInARow(to)){
                    game.printGame();
                    int removeIndex;
                    boolean validRemoval;
                    do{
                        System.out.print("Remove index: ");
                        removeIndex = input.nextInt();
                        validRemoval = game.remove(removeIndex);
                    }while(!validRemoval);
                }
            }
            if(game.isWinner()){
                System.out.println("WINNER: " + game.getCurrentPlayer());
                break;
            }
            game.nextPlayer();
        }
    }
}
