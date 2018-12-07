import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.LongVbl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MakeIdfSeq extends Task {


    public static long numberOfEmails = 0;
    private ArrayList<Word> allWords;
    private ArrayList<Email> emailsClassified;
    private ArrayList<Email> emailsUnClassified;
    private ArrayList<Email> emails;

    @Override
    public void main(String[] args) throws Exception {

        int numberOfEmails = 0, numberOfSpamEmails = 0 , numberOfHamEmails = 0;
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

        HashMap<String, Word> totalWords = new HashMap<>();
        String[] wordsInCurrentEmail;
        Email emailCurrent;
        for (int j = 0; j < emails.size(); j++) {
            emailCurrent = emails.get(j);
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            String word;
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


        Writer writer = null;
        StringBuilder sb;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("idftest.csv")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = "";
        sb = new StringBuilder();
        for(String i: totalWords.keySet()) {
            sb.append(i).append(",").append(totalWords.get(i)).append("\n");
        }
        try {
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
