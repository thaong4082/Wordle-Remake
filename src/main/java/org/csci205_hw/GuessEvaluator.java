/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring2023* Instructor: Prof. Brian King
 *
 * Name: Thao Nguyen & Aya Tarist
 * Section: 02
 * Date: 3/8/23
 * Time: 6:31 PM
 *
 * Project: csci205_hw
 * Package: org.csci205_hw
 * Class: GuessEvaluator
 *
 * Description: A simple guessEvaluator that would compare
 * the user's guess to the secret word.
 *
 * *****************************************/
package org.csci205_hw;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
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
        Set<Character> matchedCharacters = new HashSet<>();

        //if the user puts in a word that is not 5 letters, throw an exception
        if (currentGuess.length() != 5){
            throw new IllegalArgumentException("5 letters please!");
        }

        //Looping through the secret word
        for (int i = 0; i < secretWord.length(); i++){
            char guessIndex = currentGuess.charAt(i);
            char secretIndex = secretWord.charAt(i);

            //if the guess index is correct, *
            if (guessIndex == secretIndex){
                guessAnalysis += "*";
            }
            //Else if the index is wrong but the correct letter and not already matched, +
            else if (secretWord.indexOf(guessIndex) >= 0 && !matchedCharacters.contains(guessIndex)){
                guessAnalysis += "+";
                matchedCharacters.add(guessIndex);
            }
            //Else completely wrong, -
            else{
                guessAnalysis += "-";
            }

        }
        return guessAnalysis;
    }
}

