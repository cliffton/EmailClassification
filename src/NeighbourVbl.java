import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class NeighbourVbl implements Vbl {

    public ArrayList<Pair<Email, Double>> neighbours = new ArrayList<>();
    private int k = 0;
    public double[] similarityScores;

    // Use bitset instead
    public int[] categories;
    double minSimilarityScore = Double.MAX_VALUE;


    public NeighbourVbl(int numberOfNeighbors) {
        k = numberOfNeighbors;
        similarityScores = new double[k];
        categories = new int[k];
    }

    public void reset() {
        similarityScores = new double[k];
        categories = new int[k];
    }


    public void addNeighbour(double similarityScore, Email email) {

        if (similarityScore < minSimilarityScore) {

            if (neighbours.size() < k) {
                neighbours.add(new Pair<Email, Double>(email, similarityScore));
                minSimilarityScore = similarityScore;
                return;
            }

            Collections.sort(neighbours, new EmailComparator());
            neighbours.remove(0);
            neighbours.add(new Pair<Email, Double>(email, similarityScore));
        }

    }


    public double cosineSimilarity(Email e1, Email e2, ArrayList<Word> words) {

        double numerator = 0;
        double e1Denominator = 0;
        double e2Denominator = 0;

        for (Word word : words) {
            double tf1 = e1.getTFIDFScore(word);
            double tf2 = e2.getTFIDFScore(word);
            numerator += (tf1 * tf2);
            e1Denominator += (tf1 * tf1);
            e2Denominator += (tf2 * tf2);
        }

        double result = (double) (numerator / (Math.sqrt(e1Denominator) * Math.sqrt(e2Denominator)));
        if(result != result)
            return 0.0;

        return result;
    }


    public int voting() {
        int spamCount = 0;
        int hamCount = 0;

        for (int category : categories) {
            if (category == 0) {
                hamCount++;
            } else {
                spamCount++;
            }
        }


        if (spamCount > hamCount) {
            return 1;
        }
        return 0;

    }

    public void setOld(Vbl vbl) {
        ArrayList<Pair<Email, Double>> result = new ArrayList<>();
        ArrayList<Pair<Email, Double>> n1 = this.neighbours;
        ArrayList<Pair<Email, Double>> n2 = ((NeighbourVbl) vbl).neighbours;

        int left = 0;
        int right = 0;

        while (left < n1.size() && right < n2.size()) {
            if (n1.get(left).getValue() < n2.get(right).getValue()) {
                result.add(n1.get(left));
                left++;
            } else {
                result.add(n2.get(right));
                right++;
            }
        }

        while (left < n1.size()) {
            result.add(n1.get(left));
            left++;
        }


        while (right < n2.size()) {
            result.add(n2.get(right));
            right++;
        }

        this.neighbours = result;
    }


    @Override
    public void set(Vbl vbl) {
        ArrayList<Pair<Double, Integer>> result = new ArrayList<>();
        double[] tmpScores = ((NeighbourVbl) vbl).similarityScores;
        int[] tmpCategories = ((NeighbourVbl) vbl).categories;


        int left = 0;
        int right = 0;
        while (left < k && right < k && result.size() < k) {

            if (this.similarityScores[left] < tmpScores[right]) {

                result.add(new Pair<Double, Integer>(this.similarityScores[left], this.categories[left]));
                left++;

            } else {

                result.add(new Pair<Double, Integer>(tmpScores[right], tmpCategories[right]));
                right++;

            }
        }

        // while (left < k) {
        //    result.add(new Pair<Double, Integer>(this.similarityScores[left], this.categories[left]));
        //    left++;
        // }


        // while (right < k) {
        //    result.add(new Pair<Double, Integer>(tmpScores[right], tmpCategories[right]));
        //    right++;
        // }


    }

    @Override
    public void reduce(Vbl vbl) {
        set(vbl);
    }

    @Override
    public Object clone() {
        try {
            NeighbourVbl vbl = (NeighbourVbl) super.clone();
            vbl.similarityScores = this.similarityScores;
            vbl.k = this.k;
            vbl.categories = this.categories;
            return vbl;

        } catch (CloneNotSupportedException exc) {
            throw new RuntimeException("Shouldn't happen", exc);
        }
    }


}
