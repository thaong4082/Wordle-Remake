/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring2023* Instructor: Prof. Brian King
 *
 * Name: Thao Nguyen
 * Section: 02
 * Date: 3/9/23
 * Time: 5:46 PM
 *
 * Project: csci205_hw
 * Package: org.csci205_hw
 * Class: Wordle
 *
 * Description:
 *
 * *****************************************/
package org.csci205_hw;

import java.io.IOException;
import java.util.Scanner;

enum GameState{
    NEW_GAME,
    GAME_IN_PROCESS,
    GAME_WINNER,
    GAME_LOSER
}

public class Wordle {
    public static final int MIN_WORD_LENGTH = 5;
    public static final int MAX_WORD_LENGTH = 5;
    private int guessNumber;
    private String lastGuess;
    public static void main(String[] args) throws IOException {
        Wordle wordle = new Wordle();
        wordle.initNewGame();

    }
    public Wordle(){
        this.guessNumber = 0;
        this.lastGuess = "";
    }
    public void initNewGame() throws IOException {
        System.out.println("Welcome to Thao & Aya's Wordle <3");

        GuessEvaluator evaluate = new GuessEvaluator();
        evaluate.setSecretWord();
        Scanner userIn = new Scanner(System.in);
        System.out.println("Ready to play Wordle! You have 6 guesses.");

        int guessNum = 1;
        while(guessNum <= 6){
            System.out.printf("Guess #%d: ", guessNum);
            String userGuess = userIn.nextLine();
            System.out.print("  -->     " + evaluate.analyzeGuess(userGuess));
            System.out.printf("     Try again. %d guesses left.%n", 6 - guessNum);

            if (userGuess.equals(evaluate.getSecretWord())){
                System.out.printf("YOU WON! You guessed the word in %d turns!", guessNum);
                break;
            }
            guessNum++;
        }


    }
    public void playNextTurn(){

    }
    public boolean isGameOver(){
        return false;
    }
}
