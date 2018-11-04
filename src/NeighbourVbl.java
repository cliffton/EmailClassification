import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;
import javafx.util.Pair;

import java.util.ArrayList;

public class NeighbourVbl implements Vbl {

    public ArrayList<Pair<Email, Double>> neighbours = new ArrayList<>();
    private int k = 0;
    private double minSimilarityScore = Double.MAX_VALUE;


    public NeighbourVbl(int numberOfNeighbors) {
        k = numberOfNeighbors;
    }

    @Override
    public void set(Vbl vbl) {
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
    public void reduce(Vbl vbl) {
        set(vbl);
    }

    @Override
    public Object clone() {
        try {
            NeighbourVbl vbl = (NeighbourVbl) super.clone();
            vbl.neighbours = this.neighbours;
            vbl.k = this.k;
            return vbl;

        } catch (CloneNotSupportedException exc) {
            throw new RuntimeException("Shouldn't happen", exc);
        }
    }


}
