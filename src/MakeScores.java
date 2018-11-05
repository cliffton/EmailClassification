import java.io.*;
import java.util.*;


class SomeSort implements Comparator<Integer> {

    ArrayList<Double> tflist;
    List wordList;

    public SomeSort(ArrayList<Double> tflist) {
        this.tflist = tflist;
//        this.wordList = wordList;
    }

    public int compare(Integer a, Integer b) {
        if (tflist.get(a) > tflist.get(b)) {
            return -1;
        }
        return 1;
    }

}

public class MakeScores {
    public static void main(String[] args) throws IOException {


        File f = new File("ham2.csv");
        System.out.println(f);

        // CSV File ?
        String CSVFile = "emails1.csv";


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
                "ham.csv",
                "spam.csv"
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
        while (itr.hasNext()) {

            emailCurrent = (Email) itr.next();
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            HashSet<String> wordInEmailrecord = new HashSet<>();

            for (int i = 0; i < wordsInCurrentEmail.length; i++) {

                // Add word to map.
                if (!totalWordCount.containsKey(wordsInCurrentEmail[i])) {

                    WordInEmail = new Word(wordsInCurrentEmail[i]);
                    totalWordCount.put(wordsInCurrentEmail[i], WordInEmail);

                } else {

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

                // Add word to email.
                emailCurrent.setWord(WordInEmail);
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
            currentWord.makeDFScores(numberOfSpamEmails, numberOfHamEmails);
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
        StringBuilder sb1 = new StringBuilder();

        for (tupleToSortWords t : spamWords) {
            sb1.append(t.word);
            sb1.append(",");
            sb1.append(t.tfidfScore);
            sb1.append("\n");
        }
        pw1.write(sb1.toString());
        pw1.close();
        pw.close();
        System.out.println("done!");

    }

    public static double calculateSD(double xjSpam, double xjHam, double U) {

        double SpamPart = (xjSpam - U) / (2 * U);
        double HamPart = (xjHam - U) / (2 * U);
        return Math.sqrt(0.5 * (SpamPart * SpamPart + HamPart * HamPart));
    }

    public static void sortIt() {

    }


    public ArrayList<Word> getWords() {
        return new ArrayList<Word>();
    }

    public ArrayList<Email> getEmails() {
        return new ArrayList<Email>();
    }

}
