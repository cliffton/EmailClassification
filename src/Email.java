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
    public void setWord(Word word){
        if(!this.words.containsKey(word)){
            this.words.put(word, 1.0);
        }
        else {
            this.words.put(word, this.words.get(word) + 1);
        }

        if(this.words.get(word) > this.maxTFScore){
            this.maxTFScore = this.words.get(word);
        }
    }

    public void makeTF(){
        double score;
        for(Word word: this.words.keySet()) {
            score = this.words.get(word)/this.maxTFScore;
            this.words.put(word, score);
        }
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }

    public void setCategory(int category){
        this.category = category;
    }

    public int getCategory(){
        return this.category;
    }

    public void maxTFIDFScore(StringBuilder sb, ArrayList<tupleToSortWords> spamWords, ArrayList<tupleToSortWords> hamWords) {
        double score;
        for(Word word: this.words.keySet()){
            score = (this.words.get(word))*word.getIDFScore();
            sb.append(word.toString());
            sb.append(",");
            this.words.put(word, score);
            sb.append(score);
            sb.append(",");
            sb.append(this.category);
            sb.append("\n");
            if(this.category == 1){
                spamWords.add(new tupleToSortWords(word.toString(), score*(1 - word.getSDScore())));
            } else {
                hamWords.add(new tupleToSortWords(word.toString(), score*(1 - word.getSDScore())));
            }


        }
    }
}
