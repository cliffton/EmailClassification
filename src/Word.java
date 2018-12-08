// ###################################################################
//
// This file is used to structurize the words in all the documents
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ###################################################################
import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;

import java.io.IOException;

/**
 * This class is used to store the value of words
 */
public class Word {

    public String word;
    public double IDFScore;
    public int category;

    /**
     * Default cunstructor
     */
    Word() {

    }

    /**
     * This is to store the value of words
     * @param word word to store in the Word object
     */
    Word(String word) {
        this.word = word;
        this.IDFScore = 0;
    }

    /**
     * This is to store the value of words
     * @param word word to store in the Word object
     * @param IDFScore anf the IDFScore of the word
     */
    Word(String word, double IDFScore) {
        this.word = word;
        this.IDFScore = IDFScore;
    }

    /**
     * This is to store the value of words.
     * @param word word to store in the Word object.
     * @param IDFScore anf the IDFScore of the word.
     * @param category Category of the word.
     */
    Word(String word, double IDFScore, int category) {
        this.word = word;
        this.IDFScore = IDFScore;
        this.category = category;
    }

    /**
     * This is used to set the IDF score of the word.
     * @param IDFScore IDF score of the word.
     */
    public void setIDFScore(double IDFScore) {
        this.IDFScore = IDFScore;
    }

    /**
     * This is used to get the IDF score of the word.
     * @return IDF score of the word.
     */
    public double getIDFScore() {
        return this.IDFScore;
    }

    /**
     * This is used to calculate the IDF score.
     * @param number Totol number of doccuments
     * @return IDF score of the word
     */
    public double makeIDF(long number) {
        this.IDFScore = log2(number / this.IDFScore);
        return this.IDFScore;
    }

    /**
     * This is used to calculate the log to base 2 with a given number divided by the
     * IDF score
     * @param value (Total number of documents)/(Total occurence)
     * @return
     */
    double log2(double value) {
        return Math.log(value) / Math.log(2);
    }

    /**
     * This is an equals method to return whether the object passed is same as this object
     * @param obj Object to check equality with
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        return this.word.equals(((Word) obj).word);
    }


    /**
     * A to string method.
     * @return word
     */
    @Override
    public String toString() {
        return word;
    }

}

