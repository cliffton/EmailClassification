import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class makeScores {
    public static void main(String[] args) throws IOException {
        String CSVFile = "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\src\\emails1.csv";
        String split = ",";
        String line = "";
        ArrayList<Email> email = new ArrayList<>();
        String[] content;
        HashMap<String, Word> IDF =new HashMap<>();
        int numberOfEmails = 0, numberOfSpamEmails = 0, numberOfHamEmails = 0;
        BufferedReader br = new BufferedReader(new FileReader(CSVFile));
        while ((line = br.readLine()) != null){
            numberOfEmails ++;
            content = line.split(split);
            try {
                email.add(new Email(content[1], Integer.parseInt(content[2])));
            }
            catch (Exception e){
                System.out.printf("ss");
            }
        }

        Iterator itr = email.iterator();
        Email emailCurrent;
        String[] wordsInCurrentEmail;
        Word WordInEmail;

        while (itr.hasNext()) {
            emailCurrent = (Email) itr.next();
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                if (!IDF.containsKey(wordsInCurrentEmail[i])) {
                    WordInEmail = new Word(wordsInCurrentEmail[i]);
                    IDF.put(wordsInCurrentEmail[i], WordInEmail);
                } else {
                    WordInEmail = IDF.get(wordsInCurrentEmail[i]);
                }
                WordInEmail.setIDFScore(WordInEmail.getIDFScore() + 1);
                emailCurrent.setWord(WordInEmail);
            }
            emailCurrent.makeTF();
        }
        itr = email.iterator();
        int categoryOfTheEmail;
        while (itr.hasNext()){
            emailCurrent = (Email) itr.next();
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            categoryOfTheEmail = emailCurrent.getCategory();
            HashSet<String> wordInEmailRecord= new HashSet<>();
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                WordInEmail = IDF.get(wordsInCurrentEmail[i]);
                if (!wordInEmailRecord.contains(wordsInCurrentEmail[i])){
                    if(categoryOfTheEmail == 1) {
                        WordInEmail.setDFSpamScore(WordInEmail.getDFSpamScore() + 1);
                    }
                    else {
                        WordInEmail.setDFHamScore(WordInEmail.getDFHamScore() + 1);
                    }
                    wordInEmailRecord.add(wordsInCurrentEmail[i]);
                }
            }
            if(categoryOfTheEmail == 1){
                numberOfSpamEmails ++;
            } else {
                numberOfHamEmails ++;
            }
        }
        Word currentWord;
        for(String word: IDF.keySet()){
            currentWord = IDF.get(word);
            currentWord.setSDScore(calculateSD(currentWord.getDFSpamScore(),
                    currentWord.getDFHamScore(), currentWord.getIDFScore()));
        }
        for(String word: IDF.keySet()) {
            currentWord = IDF.get(word);
            currentWord.makeIDF(numberOfEmails);
            currentWord.makeDFScores(numberOfSpamEmails, numberOfHamEmails);
        }
    }
    public static double calculateSD(double xjSpam, double xjHam, double U){

        double SpamPart = (xjSpam - U)/(2 * U);
        double HamPart = (xjHam - U)/(2 * U);
        return Math.sqrt(0.5*(SpamPart*SpamPart + HamPart*HamPart));
    }
}
