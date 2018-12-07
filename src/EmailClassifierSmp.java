import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

import java.util.ArrayList;

public class EmailClassifierSmp extends Task {

    // a library of common functions to reduce code repeat
    commonFunctions cf = new commonFunctions();

    // This has the records of all the words selected int after the
    // TF-IDf phase.
    private ArrayList<Word> wordsMake;

    // A list of the classified email objects.
    private ArrayList<Email> classifiedEmails;

    private NeighbourVbl neighbourVbl;

    // A list of the un-classified email objects.
    private Email unclassified;

    /**
     * Main Program
     *
     * @param args constructor expression.
     * @throws Exception
     */
    public void main(String[] args) throws Exception {
        int k = 3;

        if(args.length < 4) usage();
        try {
            k = Integer.parseInt(args[0]);
        } catch (Exception e){
            usage();
        }
        if(args.length < 4) usage();
        // This is a parallel approach to finding the TF-IDF score.
        MakeScoresSmp makeTFIDFScore = new MakeScoresSmp();
        makeTFIDFScore.FindImportantWords(new String[]{args[1], args[2], args[3], args[4]});

        // The highest selected score.
        wordsMake = makeTFIDFScore.getWords();

        // The record of all the classified emails.
        classifiedEmails = makeTFIDFScore.getClassifiedEmails();

        // The record of all the un-classified emails.
        ArrayList<Email> unClassifiedEmails = makeTFIDFScore.getUnClassifiedEmails();

        // The size of the classified emails
        int N = classifiedEmails.size();

        int count = 0;

        // This loop iterates though all the unclassified emails.
        while (count < unClassifiedEmails.size()) {

            // Unclassified email to classify
            unclassified = unClassifiedEmails.get(count);

            // VBl for neighbours of the email
            neighbourVbl = new NeighbourVbl(k);
            neighbourVbl.reset();

            // Parallel For loop to find the nearest neighbour with all the  classified emails
            parallelFor(0, N - 1).exec(new Loop() {
                NeighbourVbl thrNeighbourVbl;

                public void start() {
                    thrNeighbourVbl = threadLocal(neighbourVbl);
                    thrNeighbourVbl.reset();
                }


                public void run(int i) {

                    // get the classified email with which the distance needs to be found out.
                    Email email = classifiedEmails.get(i);

                    // Similarity score between unclassified email and the classified email.
                    double similarityScore = thrNeighbourVbl.cosineSimilarity(email, unclassified, wordsMake);
                    thrNeighbourVbl.addNeighbour(similarityScore, email);
                }

            });

            // Find the nearest neighbours and classify the unclassified email
            int category = neighbourVbl.voting();
            unclassified.setCategory(category);
            System.out.println(unclassified.category+" " + unclassified.content);
            neighbourVbl.reset();
            System.out.flush();
            count++;
        }
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
        System.err.println(
                "java pj2 jar=<jar> threads=<NT> EmailClassifierSmp <HAM> <SPAM> <UNCLASSIFIED> <IDF>\n" +
                "a. <HAM> The location of the classified Ham file.\n" +
                "b. <SPAM> The location of the classified SPAM file.\n" +
                "c. <UNCLASSIFIED> The location of the unclassified file.\n" +
                "d. <IDF> The location of the IDF file where the results of the computed IDF's are stored.\n"
        );
        terminate(1);
    }

    /**
     * Specify that this task requires one core.
     */
    protected static int coresRequired() {
        return 1;
    }

}

