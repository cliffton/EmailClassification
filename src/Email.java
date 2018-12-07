// ##############################################################
//
// This file is a class method to store the data for
// every email that is processed
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ##############################################################

import java.util.HashMap;

/**
 * This is an email class that has all the fields availabe to
 * classify it into spam or ham
 */
public class Email {

    // This is a record of the Term Frequency of all the words in the
    // email log(count_of_word/MAX(count_of_all the words))
    HashMap<String, Double> wordsMake;

    // This is a record of the occurences of all the words in the email
    HashMap<Word, Double> words = new HashMap<>();

    // The count of the maximum occurence of any word in the email
    double maxTFScore;

    // The content of the email
    String content;

    // category of the email whether it is spam or ham
    int category;

    /**
     * It is constructor for making an email object
     * @param content The data that is there in the email in string
     * @param category Category of the email.
     */
    Email(String content, int category) {
        this.wordsMake = new HashMap<>();
        this.content = content;
        this.category = category;
        this.maxTFScore = 0;
    }

    /**
     * This method is used for convertig the occurence of the
     * email to TF score.
     */
    public void makeTF() {
        double score;
        for (String word : wordsMake.keySet()) {
            score = this.wordsMake.get(word) / this.maxTFScore;
            this.wordsMake.put(word, score);
        }
    }

    /**
     * To set the content of the email
     * @param content The email in string form
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * To set the max of the email.
     * @param maxTFScore Max occurence of any word in the email
     */
    public void setMaxTFScore(double maxTFScore) {
        this.maxTFScore = maxTFScore;
    }

    /**
     * To get the content of the email
     * @return Content of the email.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * To set the category of the eamil
     * @param category
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * To get the category of the email
     * @return Category of the email
     */
    public int getCategory() {
        return this.category;
    }


    /**
     * To get the TF-IDF score of the email.
     * @param word Word of which we want TF-IDF score.
     * @return The TF-IDF score.
     */
    public double getTFIDFScore(Word word) {
        if (words.get(word) == null) {
            return 0.0;
        }
        return words.get(word);
    }

    /**
     * This method is used for making the TF-IDF score by using the IDF score from
     * total WordCount.
     * @param totalWordCount This is a hashmap for the IDF score of the words
     */
    public void transferDataandMakeTFIDFscore(HashMap<String, Word> totalWordCount) {

        for (String i : wordsMake.keySet()) {

            try{
                words.put(totalWordCount.get(i), wordsMake.get(i) * totalWordCount.get(i).getIDFScore());
            }
            catch(Exception e){

            }
        }
    }
}
