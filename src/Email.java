import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Email{
    PrintWriter pw;
    HashMap<String, Double> words;
    double maxTFScore;
    String content;
    int category;

    Email(String content, int category) throws FileNotFoundException {
        this.words = new HashMap<>();
        this.content = content;
        this.category = category;
        this.maxTFScore = 0;
        pw = new PrintWriter(new File("test.csv"));
    }

    public void makeTF(double max) {
        double score;
        for (String word: words.keySet()) {
            score = this.words.get(word) / max;
            this.words.put(word, score);
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCategory() {
        return this.category;
    }

    public double getTFIDFScore(Word word) {
        if (words.get(word) == null) {
            return 0.0;
        }

        return words.get(word).doubleValue();
    }

}
