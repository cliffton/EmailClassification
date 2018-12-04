import edu.rit.pj2.Task;

import java.util.ArrayList;

public class EmailClassifierSeq extends Task {


    ArrayList<Word> wordsMake;
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

//            if (args.length != 1) usage();

            int k = 100;
//            int k = Integer.parseInt(args[0]);

//            MakeScores ms = new MakeScores();
//            ms.main(args);
//            wordsMake = ms.getWords();
//            classifiedEmails = ms.getClassifiedEmails();
//            unClassifiedEmails = ms.getUnClassifiedEmails();

            MakeScoresSmp ms = new MakeScoresSmp();
            //ms.main(args);
            ms.letsGo();
            wordsMake = ms.getWords();
            classifiedEmails = ms.getClassifiedEmails();
            unClassifiedEmails = ms.getUnClassifiedEmails();
            neighbourVbl = new NeighbourVbl(k);

            for (Email unclassified : unClassifiedEmails) {
                neighbourVbl.reset();
                for (Email email : classifiedEmails) {
                    double similarityScore = neighbourVbl.cosineSimilarity(email, unclassified, wordsMake);
                    neighbourVbl.addNeighbour(similarityScore, email);
                    if (similarityScore != 0) {
//                        System.out.println("Ss");
                    }

                }

                int category = neighbourVbl.voting();
                if (category == 1) {
                    System.out.println("Cat " + category + " Email = " + unclassified.content);
                } else {
                    System.out.println("Cat " + category);
                }
                System.out.flush();

                unclassified.category = category;

            }

            ms.writeBackToCSV(unClassifiedEmails);


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
