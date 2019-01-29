// ###############################################################################
//
// This file is used to sort the words IDf value in descinding order
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ###############################################################################


/**
 * This class is used to  sort the words IDf value in descinding order.
 */
public class tupleToSortWords implements Comparable{
    String word;
    double tfidfScore;

    /**
     * Constructor to create a new tuple
     * @param word Word value
     * @param tfidfScore Their TF-IDF score
     */
    tupleToSortWords(String word, double tfidfScore){
        this.word = word;
        this.tfidfScore = tfidfScore;
    }

    /**
     * This compareto function is used to sort the tuples in descinding order
     * @param o Object of type tupleToSortWords
     * @return -1, 1, 0 depending on the values
     */
    @Override
    public int compareTo(Object o) {
        tupleToSortWords temp = (tupleToSortWords) o;
        return Double.compare(temp.getTfidfScore(), this.getTfidfScore());
    }

    /**
     * Returns the TF-IDF score of the word
     * @return TF-IDF score
     */
    public double getTfidfScore(){
        return tfidfScore;
    }

    /**
     * This method converts the tuple to string
     * @return The word in string
     */
    @Override
    public String toString() {
        return word;
    }
}
