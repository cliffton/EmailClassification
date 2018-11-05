import java.io.*;
import java.util.*;


public class MakeScores {
    public static void main(String[] args) throws IOException {
        File f = new File("ham2.csv");
        System.out.println(f);
        // CSV File ?
        String CSVFile = "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\emails1.csv";
        // Spam
        PrintWriter pw1 = new PrintWriter(new File("spam.csv"));
        // Ham
        PrintWriter pw2 = new PrintWriter(new File("ham.csv"));
        String split = ",";
        String line = "";
        ArrayList<Email> emails = new ArrayList<>();
        String[] content;
        StringBuilder sb = new StringBuilder();
        int numberOfEmails = 0, numberOfSpamEmails = 0, numberOfHamEmails = 0;
        ArrayList<tupleToSortWords> spamWords = new ArrayList<>();
        ArrayList<tupleToSortWords> hamWords = new ArrayList<>();
        String[] files = {
                "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\ham.csv",
                "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\spam.csv"
        };

        // Creating Email Objects
        for (int i = 0; i < files.length; i++) {
            CSVFile = files[i];
            BufferedReader br = new BufferedReader(new FileReader(CSVFile));
            while ((line = br.readLine()) != null) {
                numberOfEmails++;
                content = line.split(split);
                try {
                    emails.add(new Email(content[2], Integer.parseInt(content[1])));
                } catch (Exception e) {
                    System.out.printf("ss");
                }
            }
        }
        Iterator itr = emails.iterator();
        Email emailCurrent;
        String[] wordsInCurrentEmail;
        Word WordInEmail;
        HashMap<String, Word> totalWordCount = new HashMap<>();
        // Iterate over emails
        for(int j = 0; j < emails.size(); j++) {
            emailCurrent = emails.get(j);
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            HashSet<String> wordInEmailrecord = new HashSet<>();
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                // Add word to map.
                if (!totalWordCount.containsKey(wordsInCurrentEmail[i])) {
                    WordInEmail = new Word(wordsInCurrentEmail[i]);
                    totalWordCount.put(wordsInCurrentEmail[i], WordInEmail);
                }
                else {
                    WordInEmail = totalWordCount.get(wordsInCurrentEmail[i]);
                }
                // Increment word count.
                WordInEmail.setTotalWordCount(WordInEmail.getTotalWordCount() + 1);
                // Per Email Calculation
                if (!wordInEmailrecord.contains(wordsInCurrentEmail[i])) {
                    WordInEmail.setIDFScore(WordInEmail.getIDFScore() + 1);
                    wordInEmailrecord.add(wordsInCurrentEmail[i]);
                    if (emailCurrent.getCategory() == 1) {
                        WordInEmail.setDFSpamScore(WordInEmail.getDFSpamScore() + 1);
                    } else {
                        WordInEmail.setDFHamScore(WordInEmail.getDFHamScore() + 1);
                    }
                }
                // Add word to email
                emailCurrent.setWord(WordInEmail);
            }
            if (emailCurrent.getCategory() == 1) {
                numberOfSpamEmails ++;
            } else {
                numberOfHamEmails ++;
            }
            emailCurrent.makeTF();
        }
        // 
        Word currentWord;
        for (String word : totalWordCount.keySet()) {
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
            emailCurrent.maxTFIDFScore(sb, spamWords, hamWords);
            counter++;
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

    public static void MakeSelectedWordList(int numberOfEmails, ArrayList<tupleToSortWords> Words,
                                     ArrayList<String> selectedWords, HashSet<String> wordsRecords) {
        String word;
        int wordsCounter = 0, wordsTakenCounter = 0;
        while (wordsTakenCounter != numberOfEmails) {
            word = Words.get(wordsCounter).toString();
            if(!wordsRecords.contains(word)) {
                wordsRecords.add(word);
                selectedWords.add(word);
                wordsTakenCounter ++;
            }
            wordsCounter++;
        }
    }


    public ArrayList<Word> getWords() {
        return new ArrayList<Word>();
    }

    public ArrayList<Email> getEmails() {
        return new ArrayList<Email>();
    }

}
