// ###################################################################
//
// This file is main file that is used to start the classification of
// the email this file has a program that runs in multi threading\
//
// usage:java pj2 jar=<jar> threads=<NT> EmailClassifierSmp <HAM> <SPAM> <UNCLASSIFIED> <IDF>
//                a. <HAM> The location of the classified Ham file.
//                b. <SPAM> The location of the classified SPAM file.
//                c. <UNCLASSIFIED> The location of the unclassified file.
//                   similarity. <IDF> The location of the IDF file where the results of the
//                   computed IDF's are stored.
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


        if(args.length < 4) usage();
        // a library of common functions to reduce code repeat
        commonFunctions cf = new commonFunctions();

        // Nuber of K nearest neighbours to look at to classify itself
        int k = Integer.parseInt(args[0]);
        MakeScoresSeq makeScoresSeq = new MakeScoresSeq();
        try {
            //Function call to calculate the TF-IDF score of all the emails
            makeScoresSeq.FindImportantWords(new String[]{args[1], args[2], args[3], args[4]});
        } catch (Exception e){
            usage();
        }

        // ArrayList of words the selected word to classify the emails.
        ArrayList<Word> wordsMake = makeScoresSeq.getWords();

        // Arraylist of the classified emails
        ArrayList<Email> classifiedEmails = makeScoresSeq.getClassifiedEmails();

        // Arraylist of all the unclassifed emails
        ArrayList<Email> unClassifiedEmails = makeScoresSeq.getUnClassifiedEmails();

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
        if(args.length > 5) {
            cf.output(unClassifiedEmails, args[5]);
        }
        else {
            cf.output(unClassifiedEmails, "UnclassifyTheClassified.csv");
        }

    }


    /**
     * Print a usage message and exit.
     */
    private static void usage() {
        System.err.println("" +
                "java pj2 jar=<jar> threads=<NT> EmailClassifierSeq <HAM> <SPAM> <UNCLASSIFIED> <IDF>\n" +
                "a. <HAM> The location of the classified Ham file.\n" +
                "b. <SPAM> The location of the classified SPAM file.\n" +
                "c. <UNCLASSIFIED> The location of the unclassified file.\n" +
                "similarity. <IDF> The location of the IDF file where the results of the computed IDF's are stored.\n");
    }

    /**
     * Specify that this task requires one core.
     */
    protected static int coresRequired() {
        return 1;
    }
}
