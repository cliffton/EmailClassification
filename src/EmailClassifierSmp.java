import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

import java.util.ArrayList;

public class EmailClassifierSmp extends Task {

    ArrayList<Word> wordsMake;
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
        int k = Integer.parseInt(args[0]);
        MakeScoresSmp ms = new MakeScoresSmp();
        //ms.main(args);
        ms.letsGo(new String[]{args[1], args[2], args[3], args[4]});
        wordsMake = ms.getWords();
        classifiedEmails = ms.getClassifiedEmails();
        unClassifiedEmails = ms.getUnClassifiedEmails();
        //System.out.println("works");
        int N = classifiedEmails.size();

        int count = 0;
        while (count < unClassifiedEmails.size()) {

            unclassified = unClassifiedEmails.get(count);
            neighbourVbl = new NeighbourVbl(k);
            neighbourVbl.reset();
            parallelFor(0, N - 1).exec(new Loop() {


                NeighbourVbl thrNeighbourVbl;

                public void start() {
                    thrNeighbourVbl = threadLocal(neighbourVbl);
                    thrNeighbourVbl.reset();
                }


                public void run(int i) {
                    Email email = classifiedEmails.get(i);
                    double similarityScore = thrNeighbourVbl.cosineSimilarity(email, unclassified, wordsMake);
                    thrNeighbourVbl.addNeighbour(similarityScore, email);
                }

            });
            int category = neighbourVbl.voting();
            if (category == 1) {

               System.out.println("Cat " + category + " Email = " + unclassified.content);
            } else {
                System.out.println("Cat " + category);
            }


            unclassified.category = category;
            neighbourVbl.reset();
            System.out.flush();
            count++;
        }
        ms.writeBackToCSV(unClassifiedEmails);


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

