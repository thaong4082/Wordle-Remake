/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring2023* Instructor: Prof. Brian King
 *
 * Name: Thao Nguyen
 * Section: 02
 * Date: 3/8/23
 * Time: 6:31 PM
 *
 * Project: csci205_hw
 * Package: org.csci205_hw
 * Class: GuessEvaluator
 *
 * Description:
 *
 * *****************************************/
package org.csci205_hw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.Set;

public class GuessEvaluator{
    private String secretWord;
    private String currentGuess;
    private String guessAnalysis;

    public GuessEvaluator() throws IOException {
        this.secretWord = secretWord;
        this.currentGuess = currentGuess;
        this.guessAnalysis = guessAnalysis;
    }

    String getSecretWord(){
        return secretWord;
    }

    /**
     * Set the secret word from obtaining the methods from WordDictionary object methods
     * @throws IOException
     */
    public void setSecretWord() throws IOException {
        WordDictionary dict = new WordDictionary("words.txt");
        Set<String> wordSet = dict.readForUser();
        try{
            String randomWord = dict.getRandomWord(wordSet);
            this.secretWord = randomWord;
//            System.out.println("Here is our random word for the game: " + randomWord);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     *
     * @param guess
     * @return string of -, +, or * depending on user input and if it aligns with secret word
     */
    public String analyzeGuess(String guess){
        currentGuess = guess;
        guessAnalysis = "";

        if (currentGuess.length() != 5){
            throw new IllegalArgumentException("5 letters please!");
        }

        for (int i = 0; i < secretWord.length(); i++){
            char guessIndex = currentGuess.charAt(i);
            char secretIndex = secretWord.charAt(i);

            if (guessIndex == secretIndex){
                guessAnalysis += "*";
            }
            else if (secretWord.indexOf(guessIndex) >= 0){
                guessAnalysis += "+";
            }
            else{
                guessAnalysis += "-";
            }

        }
        return guessAnalysis;
    }
}

