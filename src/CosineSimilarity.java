import java.util.ArrayList;

public class CosineSimilarity {


    ArrayList<Word> words;
    Email email1;
    Email email2;
    ArrayList<Email> neighbours = new ArrayList<>();
    int k = 0;
    double minSimilarityScore = Double.MAX_VALUE;


    public double cosineSimilarity(Email e1, Email e2) {

        double numerator = 0;
        double e1Denominator = 0;
        double e2Denominator = 0;

        for (Word word : words) {
            double tf1 = e1.getTFIDFScore(word);
            double tf2 = e2.getTFIDFScore(word);
            numerator += (tf1 * tf2);
            e1Denominator += (tf1 * tf1);
            e2Denominator += (tf2 * tf2);
        }

        return numerator / (Math.sqrt(e1Denominator) * Math.sqrt(e2Denominator));

    }

    public void addNeighbour(Email email, double similarityScore) {
        if (similarityScore < minSimilarityScore) {
            for (Email e : neighbours) {

            }
        }

    }


    public ArrayList<Email> getNeighbours(ArrayList<Email> emails, Email unclassified) {


        for (Email email : emails) {
            double similarityScore = cosineSimilarity(email, unclassified);

        }

        return emails;
    }


    public static void main(String[] args) {

    }

}
