import edu.rit.pj2.Loop;
import edu.rit.pj2.ObjectLoop;
import edu.rit.pj2.Task;
import edu.rit.pj2.WorkQueue;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;


public class EmailClassifierSmp
        extends Task {


    private ArrayList<Word> words;
    private ArrayList<Email> emails;
    private Email unclassified;
    private NeighbourVbl neighbourVbl;

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

            unclassified = new Email("I want to sell toys", 2);
            MakeScores ms = new MakeScores();
            words = ms.getWords();
            emails = ms.getUnClassifiedEmails();
            neighbourVbl = new NeighbourVbl(k);

            WorkQueue<Email> emailQueue = new WorkQueue<>();
            for (Email email : emails) {
                emailQueue.add(email);
            }

            parallelFor(emailQueue).exec(new ObjectLoop<Email>() {


                NeighbourVbl thrNeighbourVbl;
                //Email thrUnclassified;

                public void start() {
                    thrNeighbourVbl = threadLocal(neighbourVbl);
                    //thrUnclassified = unclassified;

                }


                public void run(Email email) {
                    double similarityScore = thrNeighbourVbl.cosineSimilarity(email, unclassified, words);
                    thrNeighbourVbl.addNeighbour(similarityScore, email);
                }
            });


            int category = neighbourVbl.voting();

            System.out.println(category);


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

