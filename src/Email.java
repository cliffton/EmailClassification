import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class Email {
    PrintWriter pw;
    HashMap<Word, Double> words;
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

    public void setWord(Word word) {
        if (!this.words.containsKey(word)) {
            this.words.put(word, 1.0);
        } else {
            this.words.put(word, this.words.get(word) + 1);
        }

        if (this.words.get(word) > this.maxTFScore) {
            this.maxTFScore = this.words.get(word);
        }
    }

    public void makeTF() {
        double score;
        for (Word word : this.words.keySet()) {
            score = this.words.get(word) / this.maxTFScore;
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

    public void maxTFIDFScore() {
        double score;
        for (Word word : this.words.keySet()) {
            score = (this.words.get(word)) * word.getIDFScore();
            this.words.put(word, score);
        }
    }

    public void makeTfidfSDScore(StringBuilder sb, ArrayList<tupleToSortWords> spamWords, ArrayList<tupleToSortWords> hamWords) {
        double score, scoreSD;
        for (Word word : this.words.keySet()) {
            score = this.words.get(word);
            sb.append(word.toString());
            sb.append(",");
            scoreSD = score * (1 - word.getSDScore());
            if (this.category == 1) {
                spamWords.add(new tupleToSortWords(word.toString(), scoreSD));
            } else {
                hamWords.add(new tupleToSortWords(word.toString(), scoreSD));
            }
            sb.append("\n");
        }
    }
}
