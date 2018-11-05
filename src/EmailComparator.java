import javafx.util.Pair;

import java.util.Comparator;


public class EmailComparator implements Comparator<Pair<Email, Double>> {

    public int compare(Pair<Email, Double> a, Pair<Email, Double> b) {
        if (a.getValue() > b.getValue()) {
            return -1;
        }
        return 1;
    }

}