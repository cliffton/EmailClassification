import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class Email {
    PrintWriter pw;
    HashMap<String, Double> wordsMake;
    HashMap<Word, Double> words = new HashMap<>();
    double maxTFScore;
    String content;
    int category;

    Email(String content, int category) throws FileNotFoundException {
        this.wordsMake = new HashMap<>();
        this.content = content;
        this.category = category;
        this.maxTFScore = 0;
        pw = new PrintWriter(new File("test.csv"));
    }

    public void makeTF(double max) {
        double score;
        for (String word : wordsMake.keySet()) {
            score = this.wordsMake.get(word) / max;
            this.wordsMake.put(word, score);
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

        return words.get(word);
    }

    public void transferDataandMakeTFIDFscore(HashMap<String, Word> totalWordCount) {
        for (String i : wordsMake.keySet()) {
            words.put(totalWordCount.get(i), wordsMake.get(i) * totalWordCount.get(i).getIDFScore());
        }
    }
}
