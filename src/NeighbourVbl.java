import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;
import javafx.util.Pair;

import java.util.ArrayList;

public class NeighbourVbl implements Vbl {

    public ArrayList<Pair<Email, Double>> neighbours = new ArrayList<>();
    private int k = 0;
    public double[] similarityScores;

    // Use bitset instead
    public int[] categories;


    public NeighbourVbl(int numberOfNeighbors) {
        k = numberOfNeighbors;
        similarityScores = new double[k];
        categories = new int[k];
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
