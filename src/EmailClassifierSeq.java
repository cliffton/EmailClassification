import edu.rit.pj2.Task;

import java.util.ArrayList;

public class EmailClassifierSeq extends Task {


    ArrayList<Word> words;
    ArrayList<Email> unClassifiedEmails;
    ArrayList<Email> classifiedEmails;


    /**
     * Main Program
     *
     * @param args constructor expression.
     * @throws Exception
     */
    public void main(String[] args) throws Exception {


        try {

            if (args.length != 1) usage();

            int k = 10;

            MakeScores ms = new MakeScores();

            ms.main(args);
            words = ms.getWords();
            classifiedEmails = ms.getClassifiedEmails();
            NeighbourVbl neighbourVbl = new NeighbourVbl(k);


            unClassifiedEmails = ms.getUnClassifiedEmails();

            for (Email unclassified : unClassifiedEmails) {
                neighbourVbl.reset();
                for (Email email : classifiedEmails) {
                    double similarityScore = neighbourVbl.cosineSimilarity(email, unclassified, words);
//                    if(similarityScore > 0.0){
//                        System.out.println(similarityScore);
//                        System.out.flush();
//                    }
                    neighbourVbl.addNeighbour(similarityScore, email);

                }

                int category = neighbourVbl.voting();
                System.out.println("Email " + category);
                System.out.flush();
            }


        } catch (Exception e) {
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
