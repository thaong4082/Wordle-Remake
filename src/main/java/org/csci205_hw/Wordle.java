/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring2023* Instructor: Prof. Brian King
 *
 * Name: Thao Nguyen & Aya Tarist
 * Section: 02
 * Date: 3/9/23
 * Time: 5:46 PM
 *
 * Project: csci205_hw
 * Package: org.csci205_hw
 * Class: Wordle
 *
 * Description: If words.txt file exists, the user is asked if they want to regenerate it or not.
 * If the file does not exist, it would automatically generate a words.txt file.
 * The user would be greeted to our wordle game in which they have 6 tries to guess
 * a five-letter word that matches with a random word in our master dictionary.
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
    private GameState currentState;

    /**
     * The main program that ties the wordle game together.
     * If words.txt file exists, the user is asked if they want to regenerate it or not.
     * If the file does not exist, it would automatically generate a words.txt file.
     * The user would be greeted to our wordle game in which they have 6 tries to guess
     * a five-letter word that matches with a random word in our master dictionary.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Wordle wordle = new Wordle();
        //Start the wordle game
        System.out.println("Welcome to Thao & Aya's Wordle <3");
        wordle.initNewGame();

        //Game in progress
        wordle.gameProgress();

        //Ask if user wants to play again
        Scanner user = new Scanner(System.in);
        wordle.playNextTurn(wordle, user);
    }

    public Wordle(){
        this.guessNumber = 0;
        this.lastGuess = "";
        this.currentState = GameState.NEW_GAME;
    }

    /**
     * Start a new game
     * @throws IOException
     */
    public void initNewGame() throws IOException {
        this.guessNumber = 0;
        this.lastGuess = "";
        this.currentState = GameState.NEW_GAME;
    }


    /**
     * The body of the game, keeps the game going and the user would have to
     * guess a five-letter word
     * @throws IOException
     */
    private void gameProgress() throws IOException {
        GuessEvaluator evaluate = new GuessEvaluator();
        evaluate.setSecretWord();
        Scanner userIn = new Scanner(System.in);
        System.out.println("Ready to play Wordle! You have 6 guesses.");

        //While the user guesses are under 6 or have not guessed the word yet,
        //They would have to keep guessing
        this.currentState = GameState.GAME_IN_PROCESS;
        int guessNum = 1;
        while(guessNum <= 6){
            System.out.printf("Guess #%d: ", guessNum);
            String userGuess = userIn.nextLine();
            System.out.print("   ---->  " + evaluate.analyzeGuess(userGuess));
            System.out.printf("     Try again. %d guesses left.%n", 6 - guessNum);

            //If the user guesses the word, they win
            if (userGuess.equals(evaluate.getSecretWord())){
                this.currentState = GameState.GAME_WINNER;
                System.out.printf("YOU WON! You guessed the word in %d turns!%n", guessNum);
                break;
            }
            guessNum++;
        }
        //If they used up their 6 tries, they lost and the word is revealed
        if(guessNum > 6){
            this.currentState = GameState.GAME_LOSER;
            System.out.println("YOU LOST! The word was " + evaluate.getSecretWord());
        }
    }

    /**
     * Once the user is finished with this round, either won or lost, ask if they want to play again
     * @param wordle
     * @param user
     * @throws IOException
     */
    private void playNextTurn(Wordle wordle, Scanner user) throws IOException {
        System.out.print("Would you like to play again? [y | n]: ");
        String playAgain = user.nextLine();
        //If they user wants to play again, a new game is made with a different word
        if (playAgain.equals("y") || playAgain.equals("Y")){
            wordle.initNewGame();
        }
        else{
            System.out.println("Goodbye <3");
        }
    }

    public boolean isGameOver() throws IOException {
        GuessEvaluator evaluate = new GuessEvaluator();
        return guessNumber >= 6 || lastGuess.equals(evaluate.getSecretWord());
    }
}
