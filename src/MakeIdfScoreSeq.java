import edu.rit.pj2.Task;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MakeIdfScoreSeq extends Task {

    commonFunctions cf = null;
    @Override
    public void main(String[] args) throws Exception {
        cf = new commonFunctions();

        int numberOfEmails = 0, numberOfSpamEmails = 0 , numberOfHamEmails = 0;
        if(args.length < 3) usage();

        // The array list of all the email objects.
        ArrayList<Email> emails = new ArrayList<>();

        // The array list of the classified  emails.
        ArrayList<Email> emailsClassified = new ArrayList<>();

        // The array list of the un-classified  emails.
        ArrayList<Email> emailsUnClassified = new ArrayList<>();

        Email temp;
        String line, CSVFile, content[], split = ",";

        // This goes throug all the command line arguments
        for (int i = 0; i < args.length - 1; i++) {
            CSVFile = args[i];
            BufferedReader br = new BufferedReader(new FileReader(CSVFile));

            // This loop passes through all the lines in the filse and
            // processes it.
            while ((line = br.readLine()) != null) {
                numberOfEmails++;
                content = line.split(split);

                // Create a new email object
                temp = new Email(content[2], Integer.parseInt(content[1]));
                emails.add(temp);

                // Put category.
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
        // Tos save the IDF score for all the words
        HashMap<String, Word> totalWords = new HashMap<>();
        String[] wordsInCurrentEmail;
        Email emailCurrent;

        // Parse through all the emails in the list.
        for (int j = 0; j < emails.size(); j++) {
            emailCurrent = emails.get(j);
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            String word;

            // To keep a record of the occurence of the word in the email
            HashSet<String> record = new HashSet<>();
            Word currentWord;

            // Go through each word in the email and record
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                word = wordsInCurrentEmail[i].toLowerCase();
                if (!record.contains(word)) {
                    currentWord = new Word(word);
                    record.add(word);
                    if (!totalWords.containsKey(word)) {
                        totalWords.put(word, currentWord);
                    }
                    totalWords.get(word).setIDFScore(totalWords.get(word).getIDFScore() + 1);
                }
            }
        }
        // This is to make the IDF score of each word in the map.
        for(String i: totalWords.keySet()){
            totalWords.get(i).makeIDF(numberOfEmails);
        }
        StringBuilder sb;
        String file;
        if(args.length <= 3){
            file = "IDFseq.csv";
        } else {
            file = args[4];
        }
        sb = new StringBuilder();
        for(String i: totalWords.keySet()) {
            sb.append(i).append(",").append(totalWords.get(i)).append("\n");
        }
        cf.writingToFile(file, sb);

    }/**
     * Usage of the the file to run
     */
    private static void usage() {
        System.err.println("" +
                "java pj2 jar=<jar> MakeIdfScoreSeq <HAM> <SPAM> <UNCLASSIFIED> <IDF>\n" +
                "a. <HAM> The location of the classified Ham file.\n" +
                "b. <SPAM> The location of the classified SPAM file.\n" +
                "c. <UNCLASSIFIED> The location of the unclassified file.\n" +
                "d. <IDF> The location of the IDF file where the results should be stored."
        );
        terminate(1);
    }
}
