/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring2023* Instructor: Prof. Brian King
 *
 * Name: Thao Nguyen
 * Section: 02
 * Date: 3/7/23
 * Time: 11:58 PM
 *
 * Project: csci205_hw
 * Package: org.csci205_hw
 * Class: WordDictionary
 *
 * Description:
 *
 * *****************************************/
package org.csci205_hw;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordDictionary {
    public static final String[] LIST_OF_URLS = {"https://www.gutenberg.org/cache/epub/1342/pg1342-images.html",
                                          "https://www.gutenberg.org/cache/epub/1661/pg1661-images.html",
                                          "https://www.gutenberg.org/cache/epub/67583/pg67583-images.html",
                                          "https://www.gutenberg.org/cache/epub/76/pg76-images.html",
                                          "https://www.gutenberg.org/cache/epub/16643/pg16643-images.html",
                                          "https://www.gutenberg.org/cache/epub/2701/pg2701-images.html"};

    public static final String WEBSTER_DICTIONARY = "https://www.gutenberg.org/cache/epub/29765/pg29765.txt";
    private static Set<String> wordSet;
    private static List<String> tempSet;
    private Map<String, Integer> wordMap;
    private int totalWords;
    private int totalGoodWordsDiscarded;
    private int totalGoodWords;
    private int totalUniqueWords;

    public Set<String> readForUser() throws IOException {
        //Check if word.txt file exists
        File wordTxtFile = new File("words.txt");

        //If file exists, ask user is they want to regenerate the file
        if (wordTxtFile.exists()){
            Scanner scnr = new Scanner (System.in);
            System.out.print("words.txt: FOUND! Do you want to regenerate the file [y | n]: ");
            String userAnswer = scnr.nextLine();

            //If they do, then we regenerate a new word set and print out the file
            if (userAnswer.equals("y") || userAnswer.equals("Y")){
                wordSet = generateNewWordSet();
                printFile(wordSet);
                System.out.println("READY!");
            }
            //If not, then we read directly from our existing file
            else{
                wordSet = readWordsFromFile(wordTxtFile);
                System.out.println("READY!");
            }
        }
        //If the file does not exist, we generate a new word set and print out the file
        else{
            System.out.println("words.txt: NOT FOUND! Generating a new set of words");
            wordSet = generateNewWordSet();
            printFile(wordSet);
            System.out.println("READY!");
        }

        return wordSet;

    }

    public WordDictionary(String fileName) throws MalformedURLException {
        this.wordSet = new HashSet<>();
        this.tempSet = new ArrayList<>();
        this.wordMap = new HashMap<>();
        this.totalWords = 0;
        this.totalGoodWordsDiscarded = 0;
        this.totalGoodWords = 0;
        this.totalUniqueWords = 0;
    }

    /**
     * Prints out the words.txt file
     * @param wordSet
     * @throws FileNotFoundException
     */
    private static void printFile(Set<String> wordSet) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File("words.txt"));
        for (String s : wordSet){
            writer.println(s);
        }
        writer.close();
        System.out.println("Storing word dataset as words.txt...");
    }

    /**
     * Reads in the url, eliminates uppercase, numbers, hyphens or any non-characters.
     * Then, sifts through for 5-letter words in which would then add to our temporary list.
     * @param url
     * @throws IOException
     */
    private void processTextAtURL(URL url) throws IOException {

        //Read in the URL
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        //While there is a line
        while((line = in.readLine()) != null){
            //Strips away the html tags
            line = line.replaceAll("\\<.*?\\>", "");

            String [] words = line.split("\\s+");
            for (String word : words){

                //Skip words that have uppercase letters or numbers
                if (word.matches(".*[A-Z0-9].*")){
                    continue;
                }

                //Strip away the quotations
                word = word.replaceAll("[”“]","");

                //Convert to lowercase
                word = word.toLowerCase();

                //Strip away the trailing punctuations
                Pattern p = Pattern.compile(".+(\\p{Punct}+)");
                Matcher m = p.matcher(word);
                if(m.matches()){
                    word = word.substring(0, m.start(1));
                }
//                System.out.println(word);
                totalWords++;

                //Words that have letters and are only 5 letters then added to temp list set
                if (word.matches("[a-zA-Z]{5}")){
                    totalGoodWords++;
                    tempSet.add(word);

                    //Keeping count of how frequent a word appears
                    if (wordMap.containsKey(word)){
                        int count = wordMap.get(word);
                        wordMap.put(word, count + 1);
                    }
                    else{
                        wordMap.put(word, 1);
                        totalUniqueWords++;
                    }
                }
                else{
                    totalGoodWordsDiscarded++;
                }
            }
        }
        in.close();
    }


    /**
     * If the user wanted to regenerate the existing file or the file did not exist,
     * then we regenerate it and add it to the Master wordSet in which would
     * not include any duplicates
     * @return Master wordSet
     * @throws IOException
     */
    static Set<String> generateNewWordSet() throws IOException{
        //Initialize a Master set, will add at the end
        wordSet = new HashSet<>();

        //Sift through dictionary to add 5-letter words to Temporary Master
        URL dictionary = new URL(WEBSTER_DICTIONARY);
        WordDictionary tpDict = new WordDictionary(WEBSTER_DICTIONARY);
        System.out.println("Reading master word set from Webster's Unabridged Dictionary");
        tpDict.processTextAtURL(dictionary);

        //Sift through the urls to add 5-letter words to Temporary Master
        for (String novel : LIST_OF_URLS) {
            URL novelURL = new URL(novel);
            WordDictionary tpNovel = new WordDictionary(novel);
            tpNovel.processTextAtURL(novelURL);
        }
        System.out.println("Finding common words from novels:\n" +
                "-Reading in Pride and Prejudice by Jane Austen......done\n" +
                "-Reading in The Adventures of Sherlock Holmes by Arthur Doyle......done\n" +
                "-Reading in The Jazz Singer......done\n" +
                "-Reading in The Adventures of Huckleberry Finn......done\n" +
                "-Reading in The Essays of Ralph Waldo Emerson......done\n" +
                "-Reading in Moby Dick......done");

        //Add all the good words from the temp set (List<String>) to
        // the Master Set (Set<String>) to ignore duplicates
        wordSet.addAll(tempSet);

        System.out.printf("Keeping %d valid words for the game...\n", wordSet.size());

        return wordSet;
    }

    /**
     * If the user did not want the regenerate the file when it already exists,
     * then we just read directly off that file
     * @param file
     * @return wordSet
     * @throws FileNotFoundException
     */
    private static Set<String> readWordsFromFile (File file) throws FileNotFoundException{
        Set<String> wordSet = new HashSet<>();
        Scanner in = new Scanner(file);

        while(in.hasNextLine()){
            String word = in.nextLine();
            wordSet.add(word);
        }
        in.close();
        return wordSet;
    }


    public boolean isWordInSet(String word){
        //TODO
        return false;
    }


    /**
     * Generate a random word from our Master Dictionary for the user to guess
     * @param wordSet
     * @return random word
     */
    public static String getRandomWord(Set<String> wordSet) {
        // initiate a variable to store a random chosen index that is not out of bound
        Random random = new Random();
        int wordIndex = random.nextInt(wordSet.size());
        int i = 0;
        for (String word : wordSet) {
            if (i == wordIndex) {
                return word;
            }
            i++;
        }
        throw new IllegalStateException("The file is empty");
    }

}

