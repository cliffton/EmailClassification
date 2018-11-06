import edu.rit.pj2.Loop;
import edu.rit.pj2.ObjectLoop;
import edu.rit.pj2.Task;
import edu.rit.pj2.WorkQueue;

import java.util.ArrayList;

public class EmailClassifierSmp extends Task {

    ArrayList<Word> words;
    ArrayList<Email> unClassifiedEmails;
    ArrayList<Email> classifiedEmails;
    NeighbourVbl neighbourVbl;
    Email unclassified;

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
            unClassifiedEmails = ms.getUnClassifiedEmails();
            neighbourVbl = new NeighbourVbl(k);
            int N = unClassifiedEmails.size();

            int count = 0;
            while (count < unClassifiedEmails.size()) {

                unclassified = unClassifiedEmails.get(count);

                parallelFor(0, N - 1).exec(new Loop() {


                    NeighbourVbl thrNeighbourVbl;
                    //Email thrUnclassified;

                    public void start() {
                        thrNeighbourVbl = threadLocal(neighbourVbl);
                        //thrUnclassified = threadLocal(unclassified);

                    }


                    public void run(int i) {
                        Email email = classifiedEmails.get(i);
                        double similarityScore = thrNeighbourVbl.cosineSimilarity(email, unclassified, words);
                        thrNeighbourVbl.addNeighbour(similarityScore, email);
                    }
                });

                count++;

            }


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

