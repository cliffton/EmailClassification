// ###################################################################
//
// This file is used as a coomparator for sorting the emails after the distance
// finding stage.
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ###################################################################
import java.util.Comparator;

/**
 * This class is a comparator class for sorting the email
 * after the distance with each email is found.
 */
public class EmailComparator implements Comparator<Pair<Email, Double>> {

    /**
     *  This is compare function to compare the distance of the emails
     * @param Email1 Email1 object.
     * @param Email2 Email2 object.
     * @return The comparision value between emails.
     */
    public int compare(Pair<Email, Double> Email1, Pair<Email, Double> Email2) {
        if (Email1.getValue() > Email2.getValue()) {
            return -1;
        }

        if(Email1.getValue() < Email2.getValue()){
            return 1;
        }
        return 0;
    }

}