import edu.rit.pj2.Task;

import java.util.ArrayList;

public class EmailClassifierSeq extends Task {


    ArrayList<Word> words;
    ArrayList<Email> unClassifiedEmails;
    ArrayList<Email> classifiedEmails;
    NeighbourVbl neighbourVbl;


    /**
     * Main Program
     *
     * @param args constructor expression.
     * @throws Exception
     */
    public void main(String[] args) throws Exception {


        try {

            if (args.length != 1) usage();

            int k = 1000;

            MakeScores ms = new MakeScores();
            ms.main(args);
            words = ms.getWords();
            classifiedEmails = ms.getClassifiedEmails();
            unClassifiedEmails = ms.getUnClassifiedEmails();
            neighbourVbl = new NeighbourVbl(k);

            for (Email unclassified : unClassifiedEmails) {
                neighbourVbl.reset();
                for (Email email : classifiedEmails) {
                    double similarityScore = neighbourVbl.cosineSimilarity(email, unclassified, words);
                    neighbourVbl.addNeighbour(similarityScore, email);

                }

                int category = neighbourVbl.voting();
                if (category == 1) {
                    System.out.println("Cat " + category + " Email = " + unclassified.content);
                } else {
                    System.out.println("Cat " + category);
                }
                System.out.flush();
            }


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
