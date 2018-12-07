// ###################################################################
//
// This file is used to classify all the emails in a sequential manner.
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ###################################################################
import edu.rit.pj2.Task;
import java.util.ArrayList;

/**
 * This class is used to classify all the emails in a sequential manner.
 */
public class EmailClassifierSeq extends Task {


    /**
     * Thsi is the main program
     * @param args
     * @throws Exception
     */
    public void main(String[] args) throws Exception {


        try {

            // Nuber of K nearest neighbours to look at to classify itself
            int k = Integer.parseInt(args[0]);
            MakeScores makeScores = new MakeScores();

            //Function call to calculate the TF-IDF score of all the emails
            makeScores.FindImportantWords(new String[]{args[1], args[2], args[3], args[4]});

            // ArrayList of words the selected word to classify the emails.
            ArrayList<Word> wordsMake = makeScores.getWords();

            // Arraylist of the classified emails
            ArrayList<Email> classifiedEmails = makeScores.getClassifiedEmails();

            // Arraylist of all the unclassifed emails
            ArrayList<Email> unClassifiedEmails = makeScores.getUnClassifiedEmails();

            // Vbl object to store the k nearest neighbours.
            NeighbourVbl neighbourVbl = new NeighbourVbl(k);

            // Iterate over all the unclassified emails
            for (Email unclassified : unClassifiedEmails) {
                neighbourVbl.reset();

                // To classify iterate over all the calssifed email
                for (Email email : classifiedEmails) {

                    // Find the similarity score between the classified email and the unclassified email
                    double similarityScore = neighbourVbl.cosineSimilarity(email, unclassified, wordsMake);
                    neighbourVbl.addNeighbour(similarityScore, email);
                }
                // calssify the email based on the nearest neighbours based on voting.
                int category = neighbourVbl.voting();
                unclassified.category = category;
            }

            // To write back the classified emails to CSV.
            makeScores.writeBackToCSV(unClassifiedEmails);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            usage();
        }

    }


    /**
     * Print a usage message and exit.
     */
    private static void usage() {
        System.err.println("Usage: java pj2 LargestTriangleSeq \"<ctor>\"");
        System.err.println("<ctor> = PointSpec constructor expression");
        terminate(1);
    }

    /**
     * Specify that this task requires one core.
     */
    protected static int coresRequired() {
        return 1;
    }
}
