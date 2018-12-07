import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MakeScoresSmp extends Task {
    private static void usage() {
        System.err.println("Usage: java pj2 [threads=<NT>] edu.rit.pjmr.example.WebLog01 <nodes> <file> [<pattern>]");
        terminate(1);
    }

    public static long numberOfEmails = 0;
    private ArrayList<Word> allWords;
    private ArrayList<Email> emailsClassified;
    private ArrayList<Email> emailsUnClassified;
    private ArrayList<Email> emails;

    public void main(String[] args) throws IOException {
        FindImportantWords(args);
    }

    public void FindImportantWords(String args[]) throws IOException {
        commonFunctions cf = new commonFunctions();


        int numberOfSpamEmails = 0, numberOfHamEmails = 0;
        emails = new ArrayList<>();
        emailsClassified = new ArrayList<>();
        emailsUnClassified = new ArrayList<>();
        allWords = new ArrayList<>();
        ArrayList<tupleToSortWords> allWordsSelected = new ArrayList<>();
        HashMap<String, Word> totalWordCount = new HashMap<>();
        CountOfEmails count = cf.createRecorde(args, emails, emailsClassified, emailsUnClassified);
        numberOfEmails = count.getNumberOfEmails();
        numberOfSpamEmails = count.getNumberOfSpamEmails();
        numberOfHamEmails = count.getNumberOfHamEmails();
        parallelFor(0, emails.size() - 1).exec(new Loop() {
            @Override
            public void run(int j) {
                Email emailCurrent = emails.get(j);
                String word;
                double scorEmail, maxEmailScore = 0;
                String[] wordsInCurrentEmail = emailCurrent.getContent().split(" ");
                for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                    word = wordsInCurrentEmail[i].toLowerCase();
                    if (!word.equals("")) {
                        if (!emailCurrent.wordsMake.containsKey(word)) {
                            emailCurrent.wordsMake.put(word, 1.0);
                        } else {
                            scorEmail = emailCurrent.wordsMake.get(word) + 1.0;
                            if (maxEmailScore < scorEmail) {
                                maxEmailScore = scorEmail;
                            }
                            emailCurrent.wordsMake.put(word, scorEmail);
                        }
                    }
                }
                emailCurrent.setMaxTFScore(maxEmailScore);
                emailCurrent.makeTF();
            }
        });

        cf.readFromIDFFileAndSelect(args, totalWordCount,allWordsSelected, emails,
                numberOfHamEmails, numberOfSpamEmails, allWords);

    }


    public ArrayList<Word> getWords() {
        return allWords;
    }

    public ArrayList<Email> getClassifiedEmails() {
        return emailsClassified;
    }

    public ArrayList<Email> getUnClassifiedEmails() {
        return emailsUnClassified;
    }


}
