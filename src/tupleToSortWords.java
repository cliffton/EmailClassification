public class tupleToSortWords implements Comparable{
    String word;
    double tfidfScore;

    tupleToSortWords(String word, double tfidfScore){
        this.word = word;
        this.tfidfScore = tfidfScore;
    }

    @Override
    public int compareTo(Object o) {
        tupleToSortWords temp = (tupleToSortWords)o;
        return Double.compare(temp.getTfidfScore(), this.getTfidfScore());
    }

    public double getTfidfScore(){
        return tfidfScore;
    }

}
