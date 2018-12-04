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


    //    private String[] files = {
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\ham.csv",
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\spam.csv",
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\unclassified.csv"
//    };

    String[] files = {
            "/home/cliffton/workspace/EmailClassification/dataFiles/ham.csv",
            "/home/cliffton/workspace/EmailClassification/dataFiles/spam.csv",
            "/home/cliffton/workspace/EmailClassification/dataFiles/unclassified100.csv"
    };

//    String[] files = {
//            "/home/stu12/s12/cf6715/emails/dataFiles/ham.csv",
//            "/home/stu12/s12/cf6715/emails/dataFiles/spam.csv",
//            "/home/stu12/s12/cf6715/emails/dataFiles/unclassified100.csv"
//    };


//    String[] files = {
//            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/ham.csv",
//            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/spam.csv",
//            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/unclassified100.csv"
//    };


    public void main(String[] args) throws IOException {
    }

    public void letsGo() throws IOException {
        int numberOfSpamEmails = 0, numberOfHamEmails = 0;
        emails = new ArrayList<>();
        emailsClassified = new ArrayList<>();
        emailsUnClassified = new ArrayList<>();
        allWords = new ArrayList<>();
        ArrayList<tupleToSortWords> allWordsSelected = new ArrayList<>();
        ArrayList<String> selected = new ArrayList<>();
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
//        CSVFile = "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\idf1.csv";


        CSVFile = "idf1.csv";
        BufferedReader br = new BufferedReader(new FileReader(CSVFile));
        while ((line = br.readLine()) != null) {
            content = line.split("\n");
            String[] con;
            Word wTemp;
            for (String i : content) {
                con = i.split(split);
                totalWordCount.put(con[0], new Word(con[0], Double.parseDouble(con[1])));
            }
        }
        for (Email i : emails) {
            i.transferDataandMakeTFIDFscore(totalWordCount);
        }
        for (String i : totalWordCount.keySet()) {
            allWordsSelected.add(new tupleToSortWords(i, totalWordCount.get(i).getIDFScore()));
        }
        Collections.sort(allWordsSelected);

        for (int i = 0; i < (numberOfSpamEmails + numberOfHamEmails)*2; i++) {
            allWords.add(totalWordCount.get(allWordsSelected.get(i).word));
        }

        System.out.print("SS");

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
        PrintWriter pw = new PrintWriter(new File("/home/cliffton/workspace/EmailClassification/dataFiles/classifiedTheUnclassified.csv"));
//        PrintWriter pw = new PrintWriter(new File(("/home/stu12/s12/cf6715/emails/dataFiles/classifiedTheUnclassified.csv")));
//        PrintWriter pw = new PrintWriter(new File(("/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/classifiedTheUnclassified.csv")));
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
