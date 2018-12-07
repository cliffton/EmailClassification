import edu.rit.pj2.Vbl;

import java.util.ArrayList;
import java.util.Collections;

public class NeighbourVbl implements Vbl {

    public ArrayList<Pair<Email, Double>> neighbours = new ArrayList<>();
    private int k = 0;
    public double[] similarityScores;

    // Use bitset instead
    public int[] categories;
    double minSimilarityScore = 0.0;


    public NeighbourVbl(int numberOfNeighbors) {
        k = numberOfNeighbors;
        similarityScores = new double[k];
        categories = new int[k];
        for (int i = 0; i < k; i++) {
            categories[i] = -1;
        }
    }

    public void reset() {
        similarityScores = new double[k];
        categories = new int[k];
        neighbours = new ArrayList<>();
    }


//    public void addNeighbour(double similarityScore, Email email) {
//        if (neighbours.size() < k) {
//            neighbours.add(new Pair<Email, Double>(email, similarityScore));
//            return;
//        }
//
//
//        if (similarityScore > minSimilarityScore) {
//
//
//            Collections.sort(neighbours, new EmailComparator());
//            neighbours.remove(0);
//            minSimilarityScore = neighbours.get(0).getValue();
//            neighbours.add(new Pair<Email, Double>(email, similarityScore));
//        }
//
//    }

    public void addNeighbour(double similarityScore, Email email) {
        neighbours.add(new Pair<Email, Double>(email, similarityScore));
        Collections.sort(neighbours, new EmailComparator());
        if (neighbours.size() > k) {
            ArrayList<Pair<Email, Double>> tmpArray = new ArrayList<Pair<Email, Double>>();
            tmpArray.addAll(neighbours.subList(0, k));
            neighbours = tmpArray;
        }
        //neighbours = System.arraycopy(neighbours, 0, tmpArray, 0, k-1);
        //System.arraycopy(source_arr, sourcePos, dest_arr,destPos, len);

    }


    public double cosineSimilarity(Email e1, Email e2, ArrayList<Word> words) {

        double numerator = 0;
        double e1Denominator = 0;
        double e2Denominator = 0;


        //TODO: Should probably do this the other way round.
        for (Word word : words) {
            double tf1 = e1.getTFIDFScore(word);
            double tf2 = e2.getTFIDFScore(word);
            numerator += (tf1 * tf2);
            e1Denominator += (tf1 * tf1);
            e2Denominator += (tf2 * tf2);
        }

        double result = (double) (numerator / (Math.sqrt(e1Denominator) * Math.sqrt(e2Denominator)));

        // NaN
        if (result != result)
            return 0.0;

        return result;
    }


//    public int voting() {
//        int spamCount = 0;
//        int hamCount = 0;
//
//        for (int category : categories) {
//            if (category == 0) {
//                hamCount++;
//            } else {
//                spamCount++;
//            }
//        }
//
//
//        if (spamCount > hamCount) {
//            return 1;
//        }
//        return 0;
//
//    }

//    public int voting() {
//        int spamCount = 0;
//        int hamCount = 0;
//        int count = 0;
//        for (int i = neighbours.size() - 1; i >= 0 & count < k; i--, count++) {
//
//            Email neighbour = neighbours.get(i).getKey();
//
//            if (neighbour.category == 0) {
//                hamCount++;
//            } else {
//                spamCount++;
//            }
//
//        }
//
//
//        if (spamCount > hamCount) {
//            return 1;
//        }
//        return 0;
//
//    }

    public int voting() {
        int spamCount = 0;
        int hamCount = 0;
        double total = 0;
        for (int i = 0; i < k; i++) {


            Email neighbour = neighbours.get(i).getKey();
            total += neighbours.get(i).getValue();
            if (neighbour.category == 1) {
                spamCount++;
            } else {
                hamCount++;
            }

        }

        if (total == 0.0) {
            return 0;
        }


        if (hamCount >= spamCount) {
            return 0;
        }
        return 1;

    }


    public ArrayList<Pair<Email, Double>> getTopK(ArrayList<Pair<Email, Double>> candidates) {

        ArrayList<Pair<Email, Double>> topK = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            double max = Double.MIN_VALUE;
            int ans = 0;
            for (int j = 0; i < candidates.size(); j++) {
                Pair<Email, Double> candidate = candidates.get(j);
                if (candidate.getValue() > max) {
                    max = candidate.getValue();
                    ans = j;
                }

            }
            topK.add(candidates.get(ans));
            candidates.remove(ans);

        }

        return topK;
    }

    public void set(Vbl vbl) {
        ArrayList<Pair<Email, Double>> result = new ArrayList<Pair<Email, Double>>();
        result.addAll(this.neighbours);
        result.addAll(((NeighbourVbl) vbl).neighbours);
//        System.out.println("neighnots " + result.size() + " | " + n1.hashCode());
//        System.out.flush();
        Collections.sort(result, new EmailComparator());
        //this.neighbours = new ArrayList<Pair<Email, Double>>();
        if (result.size() < k) {
            this.neighbours = result;
        } else {
//            this.neighbours = this.getTopK(result);
            this.neighbours.addAll(result.subList(0, k));
        }

        //this.neighbours = result;
    }


//    @Override
//    public void set(Vbl vbl) {
//        ArrayList<Pair<Double, Integer>> result = new ArrayList<>();
//        double[] tmpScores = ((NeighbourVbl) vbl).similarityScores;
//        int[] tmpCategories = ((NeighbourVbl) vbl).categories;
//
//
//        int left = 0;
//        int right = 0;
//        while (left < k && right < k && result.size() < k) {
//
//            if (this.similarityScores[left] < tmpScores[right]) {
//
//                result.add(new Pair<Double, Integer>(this.similarityScores[left], this.categories[left]));
//                left++;
//
//            } else {
//
//                result.add(new Pair<Double, Integer>(tmpScores[right], tmpCategories[right]));
//                right++;
//
//            }
//        }
//
//        // while (left < k) {
//        //    result.add(new Pair<Double, Integer>(this.similarityScores[left], this.categories[left]));
//        //    left++;
//        // }
//
//
//        // while (right < k) {
//        //    result.add(new Pair<Double, Integer>(tmpScores[right], tmpCategories[right]));
//        //    right++;
//        // }
//
//
//    }

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
            vbl.neighbours = this.neighbours;
            return vbl;

        } catch (CloneNotSupportedException exc) {
            throw new RuntimeException("Shouldn't happen", exc);
        }
    }


}
