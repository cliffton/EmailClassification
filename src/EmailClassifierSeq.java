import edu.rit.pj2.Task;
import javafx.util.Pair;

import java.util.ArrayList;

public class EmailClassifierSeq extends Task {

    /**
     * Main Program
     *
     * @param args constructor expression.
     * @throws Exception
     */
    public void main(String[] args) throws Exception {

        // Parse command line arguments.
        if (args.length != 1) usage();

        try {
            // Constructs PointSpec object and uses it to construct
            // create plane object.
            ArrayList<Pair<Email, Double>> neighbours = new ArrayList<>();
            CosineSimilarity cosine = new CosineSimilarity(10, neighbours);
            for (Email email : emails) {
                double similarityScore = cosine.cosineSimilarity(email, unclassified);
                cosine.addNeighbour(email, similarityScore);
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
