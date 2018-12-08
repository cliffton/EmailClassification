// ###################################################################
//
// This file is the VBL class that is used for reduction of the
// Nearest neighbours of an email.
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ###################################################################

import edu.rit.pj2.Vbl;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is a NeighbourVbl class that is used for reduction of the
 * Nearest neighbours of an email.
 */
public class NeighbourVbl implements Vbl {

    // This is a list that maintains the neighbours of the unclassified email.
    private ArrayList<Pair<Email, Double>> neighbours = new ArrayList<>();
    private int k = 0;
    private double[] similarityScores;

    // Use bitset instead
    public int[] categories;

    /**
     * This is constructor to create a NeighbourVbl object.
     * @param numberOfNeighbors The number of neighbours of an email.
     */
    public NeighbourVbl(int numberOfNeighbors) {
        k = numberOfNeighbors;
        similarityScores = new double[k];
        categories = new int[k];
        for (int i = 0; i < k; i++) {
            categories[i] = -1;
        }
    }

    /**
     * This method is used to reset the VBL object.
     */
    public void reset() {
        similarityScores = new double[k];
        categories = new int[k];
        neighbours = new ArrayList<>();
    }

    /**
     * This method is used for adding a neighbour to the neighbour list.
     * @param similarityScore Similarity index between the VBL email and the recieved email
     * @param email The name of the email i.e. the neighbour.
     */
    public void addNeighbour(double similarityScore, Email email) {
        neighbours.add(new Pair<Email, Double>(email, similarityScore));
        Collections.sort(neighbours, new EmailComparator());
        if (neighbours.size() > k) {
            ArrayList<Pair<Email, Double>> tmpArray = new ArrayList<Pair<Email, Double>>();
            tmpArray.addAll(neighbours.subList(0, k));
            neighbours = tmpArray;
        }

    }

    /**
     * This method is used for callculating the similarity between the 2 emails using
     * the given words.
     * @param e1 Email 1
     * @param e2 Email 2
     * @param words Selected words
     * @return similarity score between e1 and e2 using words
     */
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

    /**
     * This method is used for voting i.e. finding the category of the unclassified email.
     * @return Category
     */
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

    /**
     * This is the set method for the VBL reduction vetween the
     * neighbours of between threads.
     * @param vbl The NeighbourVBL object
     */
    public void set(Vbl vbl) {
        ArrayList<Pair<Email, Double>> result = new ArrayList<Pair<Email, Double>>();
        result.addAll(this.neighbours);
        result.addAll(((NeighbourVbl) vbl).neighbours);
        Collections.sort(result, new EmailComparator());
        if (result.size() < k) {
            this.neighbours = result;
        } else {
            this.neighbours.addAll(result.subList(0, k));
        }
    }

    /**
     * This method is called for reduction
     * @param vbl NeighbourVbl object
     */
    @Override
    public void reduce(Vbl vbl) {
        set(vbl);
    }

    /**
     * This method is used to clone the neighbourVbl object.
     * @return New object of neighbourVbl hat has the same value as this object
     */
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
