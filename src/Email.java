import java.util.ArrayList;
import java.util.HashMap;

public class Email {
    HashMap<Word, Double> words;
    double maxTFScore;
    String content;
    int category;
    Email(String content, int category){
        this.words = new HashMap<>();
        this.content = content;
        this.category = category;
        this.maxTFScore = 0;
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
        for(Word word: this.words.keySet()){
            this.words.put(word, (this.words.get(word)/this.maxTFScore));
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


    public double getTFIDFScore(Word word){
        return words.get(word).doubleValue();
    }
}
