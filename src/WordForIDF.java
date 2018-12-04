import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;

import java.io.IOException;
import java.io.Serializable;

public class WordForIDF implements Comparable<WordForIDF>, Serializable {
    public String word;
    public double IDFScore;
    public int category;
    WordForIDF(){

    }
    WordForIDF(String word) {
        this.word = word;
        this.IDFScore = 0;
    }

    public boolean equals
            (Object obj)
    {
        return (obj instanceof WordForIDF) && (this.word.equals(((WordForIDF)obj).word));
    }

    WordForIDF(String word, double IDFScore){
        this.word = word;
        this.IDFScore = IDFScore;
    }

    WordForIDF(String word, double IDFScore, int category){
        this.word = word;
        this.IDFScore = IDFScore;
        this.category = category;
    }


    public void setIDFScore(double IDFScore) {
        this.IDFScore = IDFScore;
    }

    public double getIDFScore() {
        return this.IDFScore;
    }


    public double makeIDF(long number) {
        this.IDFScore = log2(number / this.IDFScore);
        return this.IDFScore;
    }

    double log2(double value) {
        return Math.log(value) / Math.log(2);
    }
    public int hashCode()
    {
        return word.hashCode();
    }

    @Override
    public String toString() {
        return word;
    }


    @Override
    public int compareTo(WordForIDF o) {
        return 0;
    }
}

