/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Spring2023* Instructor: Prof. Brian King
 *
 * Name: Thao Nguyen
 * Section: 02
 * Date: 3/6/23
 * Time: 5:43 PM
 *
 * Project: csci205_hw
 * Package: org.csci205_hw
 * Class: TextProcessor
 *
 * Description:
 *
 * *****************************************/
package org.csci205_hw;


import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextProcessor {
    private URL url;
    private Map<String, Integer> wordMap;
    private int totalWords;
    private int totalGoodWordsDiscarded;
    private int totalGoodWords;
    private int totalUniqueWords;

//    public static void main(String[] args) throws IOException {
//        //Reads in Pride and Prejudice
//        URL novelURL = new URL("https://www.gutenberg.org/cache/epub/1342/pg1342-images.html");
//        TextProcessor processor = new TextProcessor(novelURL);
//        processor.processTextAtURL(novelURL);
//        processor.printReport();
//    }

    public TextProcessor(URL url) {
        this.url = url;
        this.wordMap = new HashMap<>();
        this.totalWords = 0;
        this.totalGoodWordsDiscarded = 0;
        this.totalGoodWords = 0;
        this.totalUniqueWords = 0;
    }

    public void processTextAtURL(URL url) throws IOException {

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

                //Words that have letters and are only 5 letters
                if (word.matches("[a-zA-Z]{5}")){
                    totalGoodWords++;

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

    private boolean isWordValid(String word) {
        //TODO
        return false;
    }

    public void printReport() {
        System.out.println("Total number of words processed: " + totalWords);
        System.out.println("Total good words by wrong length: " + totalGoodWordsDiscarded);
        System.out.println("Total number of words kept: " + totalGoodWords);
        System.out.println("Number of unique words: " + totalUniqueWords);
        System.out.println("Top 20 most frequently occurring words");
        writeList();
    }

    private void writeList() {

        //List object
        List<Map.Entry<String, Integer>> wordList = new ArrayList<>(wordMap.entrySet());

        //Sort the lists to go in descending order
        Collections.sort(wordList, new Comparator<Map.Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2){
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //Add onto top 20 list
        for (int i = 0; i < Math.min(20, wordList.size()); i++) {
            System.out.println(wordList.get(i).getKey() + " : " + wordList.get(i).getValue());
        }
    }

    public Map<String, Integer> getSetOfWords() {
        return wordMap;
    }

    public int getTotalWords(){
        return totalWords;
    }

    public int getTotalGoodWords() {
        return totalGoodWords;
    }

    public int getTotalUniqueWords() {
        return totalUniqueWords;
    }
}
