import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;

import java.io.IOException;

public class Word{
    public String word;
    public double IDFScore;

    Word(){

    }
    Word(String word) {
        this.word = word;
        this.IDFScore = 0;
    }

    Word(String word, double IDFScore){
        this.word = word;
        this.IDFScore = IDFScore;
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

    @Override
    public String toString() {
        return word;
    }

}

