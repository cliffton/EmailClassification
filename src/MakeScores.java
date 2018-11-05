
import edu.rit.pj2.Task;

import java.io.*;
import java.util.*;


public class MakeScores extends Task {

    ArrayList<Word> allWords;
    ArrayList<Email> emailsClassified;
    ArrayList<Email> emailsUnClassified;
    ArrayList<Email> emails;

    public void main(String[] args) throws IOException {
        File f = new File("ham2.csv");
        System.out.println(f);
        // CSV File ?
        String CSVFile = "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\emails1.csv";
        // Spam
        PrintWriter pw1 = new PrintWriter(new File("spamSmp.csv"));
        // Ham
        PrintWriter pw2 = new PrintWriter(new File("ham.csv"));
        String split = ",";
        String line = "";
        emails = new ArrayList<>();
        emailsClassified = new ArrayList<>();
        emailsUnClassified = new ArrayList<>();
        String[] content;
        StringBuilder sb = new StringBuilder();
        int numberOfEmails = 0, numberOfSpamEmails = 0, numberOfHamEmails = 0;
        ArrayList<tupleToSortWords> spamWords = new ArrayList<>();
        ArrayList<tupleToSortWords> hamWords = new ArrayList<>();
        String[] files = {
                "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\ham.csv",
                "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\spam.csv",
                "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\unclassified.csv"
        };

        // Creating Email Objects
        Email temp;
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
                        emailsUnClassified.add(temp);
                    }
                }
            }
        }
        Iterator itr = emails.iterator();
        Email emailCurrent;
        String[] wordsInCurrentEmail;
        Word WordInEmail;
        HashMap<String, Word> totalWordCount = new HashMap<>();
        HashMap<String, Word> totalClassifiedWordCount = new HashMap<>();
        // Iterate over emails
        for (int j = 0; j < emails.size(); j++) {
            emailCurrent = emails.get(j);
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            HashSet<String> wordInEmailrecord = new HashSet<>();
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                if (!totalWordCount.containsKey(wordsInCurrentEmail[i])) {
                    WordInEmail = new Word(wordsInCurrentEmail[i]);
                    totalWordCount.put(wordsInCurrentEmail[i], WordInEmail);
                } else {
                    WordInEmail = totalWordCount.get(wordsInCurrentEmail[i]);
                }
                WordInEmail.setTotalWordCount(WordInEmail.getTotalWordCount() + 1);
                emailCurrent.setWord(WordInEmail);
                if (!wordInEmailrecord.contains(wordsInCurrentEmail[i])) {
                    WordInEmail.setIDFScore(WordInEmail.getIDFScore() + 1);
                    wordInEmailrecord.add(wordsInCurrentEmail[i]);
                }
            }
            emailCurrent.makeTF();
        }

        for (int j = 0; j < emailsClassified.size(); j++) {
            emailCurrent = emailsClassified.get(j);
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            HashSet<String> wordInEmailrecord = new HashSet<>();
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                WordInEmail = totalWordCount.get(wordsInCurrentEmail[i]);
                if (!totalClassifiedWordCount.containsKey(wordsInCurrentEmail[i])) {
                    totalClassifiedWordCount.put(wordsInCurrentEmail[i], WordInEmail);
                }
                if (!wordInEmailrecord.contains(wordsInCurrentEmail[i])) {
                    if (emailCurrent.getCategory() == 1) {
                        WordInEmail.setDFSpamScore(WordInEmail.getDFSpamScore() + 1);
                    } else {
                        WordInEmail.setDFHamScore(WordInEmail.getDFHamScore() + 1);
                    }
                    wordInEmailrecord.add(wordsInCurrentEmail[i]);
                }
            }
            if (emailCurrent.getCategory() == 1) {
                numberOfSpamEmails++;
            } else {
                numberOfHamEmails++;
            }
        }
        // 
        Word currentWord;

        for (String word : totalClassifiedWordCount.keySet()) {
            currentWord = totalWordCount.get(word);
            currentWord.setSDScore(
                    calculateSD(
                            currentWord.getDFSpamScore(),
                            currentWord.getDFHamScore(),
                            currentWord.getTotalWordCount()
                    )
            );
        }
        for (String word : totalWordCount.keySet()) {
            currentWord = totalWordCount.get(word);
            currentWord.makeIDF(numberOfEmails);
        }
        PrintWriter pw = new PrintWriter(new File("test.csv"));
        itr = emails.iterator();
        int counter = 0;
        while (itr.hasNext()) {
            emailCurrent = (Email) itr.next();
            emailCurrent.maxTFIDFScore();
            counter++;
        }

        for (int j = 0; j < emailsClassified.size(); j++) {
            emailCurrent = emailsClassified.get(j);
            emailCurrent.makeTfidfSDScore(sb, spamWords, hamWords);
        }

        Collections.sort(spamWords);
        Collections.sort(hamWords);
        pw.write(sb.toString());
        pw.close();
        System.out.println("done!");
        ArrayList<String> selectedWords = new ArrayList<>();
        HashSet<String> wordsRecords = new HashSet<>();
        MakeSelectedWordList(numberOfSpamEmails, spamWords, selectedWords, wordsRecords);
        MakeSelectedWordList(numberOfHamEmails, hamWords, selectedWords, wordsRecords);
        //ArrayList<Word> allWords = new ArrayList<>();
        for (String w : wordsRecords) {
            allWords.add(totalWordCount.get(w));
        }

        StringBuilder sb1 = new StringBuilder();
        for (String t : selectedWords) {
            sb1.append(t);
            sb1.append("\n");
        }
        pw1.write(sb1.toString());
        pw1.close();

    }

    public static double calculateSD(double xjSpam, double xjHam, double U) {

        double SpamPart = (xjSpam - U) / (2 * U);
        double HamPart = (xjHam - U) / (2 * U);
        return Math.sqrt(0.5 * (SpamPart * SpamPart + HamPart * HamPart));
    }

    public void MakeSelectedWordList(int numberOfEmails, ArrayList<tupleToSortWords> Words,
                                     ArrayList<String> selectedWords, HashSet<String> wordsRecords) {
        String word;
        int wordsCounter = 0, wordsTakenCounter = 0;
        while (wordsTakenCounter != numberOfEmails) {
            word = Words.get(wordsCounter).toString();
            if (!wordsRecords.contains(word)) {
                wordsRecords.add(word);
                selectedWords.add(word);
                wordsTakenCounter++;
            }
            wordsCounter++;
        }
    }


    public ArrayList<Word> getWords() {
        return allWords;
    }

    public ArrayList<Email> getClassifiedEmails() {
        return emailsUnClassified;
    }

    public ArrayList<Email> getUnClassifiedEmails() {
        return emailsUnClassified;
    }

    public ArrayList<Email> getAllEmails() {
        return emails;
    }

}
