import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
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

    public void main(String[] args){

    }

    public void letsGo(String args[]) throws IOException {
        int numberOfSpamEmails = 0, numberOfHamEmails = 0;
        emails = new ArrayList<>();
        emailsClassified = new ArrayList<>();
        emailsUnClassified = new ArrayList<>();
        allWords = new ArrayList<>();
        ArrayList<tupleToSortWords> allWordsSelected = new ArrayList<>();
        HashMap<String, Word> totalWordCount = new HashMap<>();
        Email temp;
        String line, CSVFile, content[], split = ",";
        for (int i = 0; i < args.length - 1; i++) {
            CSVFile = args[i];
            BufferedReader br = new BufferedReader(new FileReader(CSVFile));
            while ((line = br.readLine()) != null) {
                numberOfEmails++;
                content = line.split(split);
                temp = new Email(content[2], Integer.parseInt(content[1]));
                emails.add(temp);
                if (Integer.parseInt(content[1]) != 2) {
                    if (Integer.parseInt(content[1]) == 1) {
                        emailsClassified.add(temp);
                        numberOfSpamEmails++;
                    } else {
                        emailsClassified.add(temp);
                        numberOfHamEmails++;
                    }
                } else {
                    emailsUnClassified.add(temp);
                }
            }
        }

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
                emailCurrent.makeTF(maxEmailScore);
            }
        });


        CSVFile = args[3];
        BufferedReader br = new BufferedReader(new FileReader(CSVFile));
        ArrayList<tupleToSortWords> allwordsWithIDF = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            content = line.split("\n");
            String[] con;
            Word wTemp;
            for (String i : content) {
                con = i.split(split);
                wTemp = new Word(con[0], Double.parseDouble(con[1]));
                totalWordCount.put(con[0], wTemp);
                allWordsSelected.add(new tupleToSortWords(con[0], Double.parseDouble(con[1])));
            }
        }
        Collections.sort(allWordsSelected);

        for (Email i : emails) {
            i.transferDataandMakeTFIDFscore(totalWordCount);
        }
        for (int i = 0; i < numberOfHamEmails + numberOfSpamEmails; i++) {
            allWords.add(totalWordCount.get(allWordsSelected.get(i).word));
        }

        for (int i = 0; i < numberOfHamEmails + numberOfSpamEmails; i++) {
            allWords.add(totalWordCount.get(allWordsSelected.get(allWordsSelected.size() - 1 - i).word));
        }
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


    public void writeBackToCSV(ArrayList<Email> result) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File(("/home/stu2/s18/nhk8621/tardis/outputs/classifiedTheUnclassified-"+result.size()+".csv")));
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        String delimiter = ",";
        for (Email e : result) {
            sb.append(counter).append(delimiter).
                    append(e.getCategory()).append(delimiter).
                    append(e.getContent()).append("\n");
        }
        pw.write(sb.toString());
        pw.close();

    }

}
