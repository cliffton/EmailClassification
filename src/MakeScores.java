
import edu.rit.pj2.Task;

import java.io.*;
import java.util.*;


public class MakeScores extends Task {

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
        int numberOfEmails, numberOfSpamEmails, numberOfHamEmails;
        emails = new ArrayList<>();
        emailsClassified = new ArrayList<>();
        emailsUnClassified = new ArrayList<>();
        allWords = new ArrayList<>();
        ArrayList<tupleToSortWords> spamWords = new ArrayList<>();
        ArrayList<tupleToSortWords> hamWords = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        PrintWriter pw = new PrintWriter(new File("test.csv"));
        HashMap<String, Word> totalWordCount = new HashMap<>();
        HashMap<String, Word> totalClassifiedWordCount = new HashMap<>();
        numberOfEmails = makeEmailsLists();
        calculateAllWordsAndTheirTF(totalWordCount);
        TotalEmails ans = makeValuesForClassifiedEmails(totalWordCount, totalClassifiedWordCount);
        numberOfSpamEmails = ans.getSpamEmails();
        numberOfHamEmails = ans.getHamEmails();
        calculateSDForallEmails(totalClassifiedWordCount, totalWordCount);
        makeIDFScoreForAllEmails(totalWordCount, numberOfEmails);
        makeTFIDFForAllEmails();
        makeWordsArray(sb, spamWords, hamWords, numberOfSpamEmails, numberOfHamEmails, totalWordCount);
        pw.write(sb.toString());

//        System.out.println("done!");


    }

    public int makeEmailsLists() throws IOException {
        Email temp;
        String line, CSVFile, content[], split = ",";
        ;
        int numberOfEmails = 0;
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
        return numberOfEmails;
    }


    public void makeWordsArray(StringBuilder sb, ArrayList<tupleToSortWords> spamWords,
                               ArrayList<tupleToSortWords> hamWords, int numberOfSpamEmails,
                               int numberOfHamEmails, HashMap<String, Word> totalWordCount) {
        Email emailCurrent;
        ArrayList<String> selectedWords = new ArrayList<>();
        HashSet<String> wordsRecords = new HashSet<>();
        for (int j = 0; j < emailsClassified.size(); j++) {
            emailCurrent = emailsClassified.get(j);
            emailCurrent.makeTfidfSDScore(sb, spamWords, hamWords);
        }
        Collections.sort(spamWords);
        Collections.sort(hamWords);

        MakeSelectedWordList(numberOfSpamEmails, spamWords, selectedWords, wordsRecords);
        MakeSelectedWordList(numberOfHamEmails, hamWords, selectedWords, wordsRecords);

        for (String w : selectedWords) {
            allWords.add(totalWordCount.get(w));
        }
    }

    public void makeTFIDFForAllEmails() {
        Email emailCurrent;
        Iterator itr = emails.iterator();
        while (itr.hasNext()) {
            emailCurrent = (Email) itr.next();
            emailCurrent.maxTFIDFScore();
        }
    }

    public void makeIDFScoreForAllEmails(HashMap<String, Word> totalWordCount, int numberOfEmails) {
        Word currentWord;
        for (String word : totalWordCount.keySet()) {
            currentWord = totalWordCount.get(word);
            currentWord.makeIDF(numberOfEmails);
        }
    }

    private void calculateSDForallEmails(HashMap<String, Word> totalClassifiedWordCount,
                                         HashMap<String, Word> totalWordCount) {
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

    private void calculateAllWordsAndTheirTF(HashMap<String, Word> totalWordCount) {
        Email emailCurrent;
        String[] wordsInCurrentEmail;
        Word WordInEmail;
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
    }

    private TotalEmails makeValuesForClassifiedEmails(HashMap<String, Word> totalWordCount,
                                                      HashMap<String, Word> totalClassifiedWordCount) {
        Email emailCurrent;
        String[] wordsInCurrentEmail;
        Word WordInEmail;
        int numberOfSpamEmails = 0, numberOfHamEmails = 0;
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
        return new TotalEmails(numberOfHamEmails + numberOfHamEmails, numberOfSpamEmails, numberOfHamEmails);
    }

    public ArrayList<Word> getWords() {
        return allWords;
    }

    public ArrayList<Email> getClassifiedEmails() {
        return emailsClassified;
    }

    public ArrayList<Email> getUnClassifiedEmails() {
//        Collections.shuffle(emailsUnClassified);
        return emailsUnClassified;
    }

    public ArrayList<Email> getAllEmails() {
        return emails;
    }

    public void writeBackToCSV(ArrayList<Email> result) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("/home/cliffton/workspace/EmailClassification/dataFiles/classifiedTheUnclassified.csv"));
//        PrintWriter pw = new PrintWriter(new File(("/home/stu12/s12/cf6715/emails/dataFiles/classifiedTheUnclassified.csv")));
//        PrintWriter pw = new PrintWriter(new File(("/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/classifiedTheUnclassified.csv")));
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        String delimiter = ",";
        for(Email e: result){
            sb.append(counter).append(delimiter).
                    append(e.getCategory()).append(delimiter).
                    append(e.getContent()).append("\n");
        }
        pw.write(sb.toString());
        pw.close();

    }

}
