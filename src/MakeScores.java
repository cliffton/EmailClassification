import edu.rit.pj2.Task;
import edu.rit.pjmr.PjmrJob;
import edu.rit.pjmr.TextFileSource;
import edu.rit.pjmr.TextId;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MakeScores extends Task {
    private static void usage() {
        System.err.println("Usage: java pj2 [threads=<NT>] edu.rit.pjmr.example.WebLog01 <nodes> <file> [<pattern>]");
        terminate(1);
    }

    private ArrayList<Word> allWords;
    private ArrayList<Email> emailsClassified;
    private ArrayList<Email> emailsUnClassified;
    private ArrayList<Email> emails;
    private String[] files = {
            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\ham.csv",
            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\spam.csv",
            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\unclassified.csv",
    };
    /**
     * PJMR job main program.
     *
     * @param args Command line arguments.
     */
    public void main
    (String[] args) throws IOException {
        int numberOfEmails = 0, numberOfSpamEmails, numberOfHamEmails;
        emails = new ArrayList<>();
        emailsClassified = new ArrayList<>();
        emailsUnClassified = new ArrayList<>();
        allWords = new ArrayList<>();
        ArrayList<tupleToSortWords> spamWords = new ArrayList<>();
        ArrayList<tupleToSortWords> hamWords = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        HashMap<String, Word> totalWordCount = new HashMap<>();
        HashMap<String, Word> totalClassifiedWordCount = new HashMap<>();
        Email temp;
        String line, CSVFile, content[], split = ",";
        for (int i = 0; i < files.length; i++) {
            CSVFile = files[i];
            BufferedReader br = new BufferedReader(new FileReader(CSVFile));
            while ((line = br.readLine()) != null) {
                numberOfEmails++;
                content = line.split(split);
                temp = new Email(content[2], Integer.parseInt(content[1]));
                emails.add(temp);
                if (Integer.parseInt(content[1]) != 2) {
                    if (Integer.parseInt(content[1]) == 1) {
                        emailsClassified.add(temp);
                    } else {
                        emailsClassified.add(temp);
                    }
                } else {
                    emailsUnClassified.add(temp);
                }
            }
        }
        Email emailCurrent;
        String[] wordsInCurrentEmail;
        Word WordInEmail;
        for (int j = 0; j < emails.size(); j++) {
            emailCurrent = emails.get(j);
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            String word;
            double scorEmail, maxEmailScore = 0;
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                word = wordsInCurrentEmail[i].toLowerCase();
                if(!word.equals("")) {
                    if (!emailCurrent.words.containsKey(word)) {
                        emailCurrent.words.put(word, 1.0);
                    }
                    else {
                        scorEmail = emailCurrent.words.get(word) + 1.0;
                        if (maxEmailScore < scorEmail) {
                            maxEmailScore = scorEmail;
                        }
                        emailCurrent.words.put(word, scorEmail);
                    }
                }
            }
            emailCurrent.makeTF(maxEmailScore);
        }
        HashMap<String, Word> totalWords = new HashMap<>();
        for (int j = 0; j < emails.size(); j++) {
            emailCurrent = emails.get(j);
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            String word;
            double scorEmail, maxEmailScore = 0;
            HashSet<String> record = new HashSet<>();
            Word w;
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                word = wordsInCurrentEmail[i].toLowerCase();
                if (!record.contains(word)) {
                    w = new Word(word);
                    record.add(word);
                    if (!totalWords.containsKey(word)) {
                        totalWords.put(word, w);
                    }
                    totalWords.get(word).setIDFScore(totalWords.get(word).getIDFScore() + 1);
                }
            }
        }
        for(String i: totalWords.keySet()){
            totalWords.get(i).makeIDF(numberOfEmails);
        }

        System.out.println("s");
    }
}
