// ###############################################################################
//
// This file is used have common functions in one place such that it can be accessed
// by all the files and can improve code reuse.
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ###############################################################################


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class has a selt of all functions that are used across all the files.
 */
public class commonFunctions {

    /**
     * This function is used to write data to a particular file
     * using a string builder.
     * @param filename Name of the file where i have to write.
     * @param sb StringBuilder object to write to the file.
     */
    public void writingToFile(String filename, StringBuilder sb){
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to read the IDF file extract data make the TF-IDF scores
     * and select the top words as parameters
     * @param args arguments used i.e. location of the IDF file
     * @param totalWordCount Hashmap with the count of each word
     * @param allWordsSelected The Hashmap of all the selected words
     * @param emails An array list of all the emails
     * @param numberOfHamEmails number of spam emails
     * @param numberOfSpamEmails Number of ham emails
     * @param allWords All words hashmap
     * @throws IOException
     */
    public void readFromIDFFileAndSelect(String[] args,
                                         HashMap<String, Word> totalWordCount,
                                         ArrayList<tupleToSortWords> allWordsSelected,
                                         ArrayList<Email> emails,
                                         int numberOfHamEmails,
                                         int numberOfSpamEmails,
                                         ArrayList<Word> allWords) throws IOException {
        String CSVFile = args[3], split = ",";
        String line;
        String[] content;
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

        // This is used to sort the words in descinding order to select top words
        Collections.sort(allWordsSelected);

        // Used to trasnfer data from local to emails hashmap
        for (Email i : emails) {
            i.transferDataandMakeTFIDFscore(totalWordCount);
        }

        // Selecting some top words that are uncommon in emails.
        // i.e with high IDF scores
        for (int i = 0; i < numberOfHamEmails + numberOfSpamEmails; i++) {
            allWords.add(totalWordCount.get(allWordsSelected.get(i).word));
        }

        // Selecting some words that are very common in all the emails
        // i.e with low IDF scores
        for (int i = 0; i < numberOfHamEmails + numberOfSpamEmails; i++) {
            allWords.add(totalWordCount.get(allWordsSelected.get(allWordsSelected.size() - 1 - i).word));
        }
    }

    /**
     * This function is used to create the CSV file that will be genrated
     * after all the emails are classified
     * @param result The arraylist of all the unclassified emails that are now classified
     * @param Filename Name of the file where to store
     * @throws FileNotFoundException
     */
    public void output(ArrayList<Email> result, String Filename) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File((Filename)));
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        String delimiter = ",";
        for (Email e : result) {
            sb.append(counter).append(delimiter).
                    append(e.getCategory()).append(delimiter).
                    append(e.getContent()).append("\n");
            counter ++;
        }
        pw.write(sb.toString());
        pw.close();
    }

    /**
     * This file is used to read the CSV and then fill the araylists accordingly
     * @param args Arguments from where to read the files.
     * @param emails ArrayList to store all emails
     * @param emailsClassified Arraylist to store all the classified emails
     * @param emailsUnClassified Arraylist to store all the unclassified emails
     * @return This returns an object of the count of all the types of emails.
     * @throws IOException
     */
    public CountOfEmails createRecorde(String[] args,
                                       ArrayList<Email> emails,
                                       ArrayList<Email> emailsClassified,
                                       ArrayList<Email> emailsUnClassified) throws IOException {
        String CSVFile, line, split = ",";
        String[] content;
        Email temp;
        int numberOfEmails = 0, numberOfSpamEmails= 0, numberOfHamEmails = 0;
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
        return new CountOfEmails(numberOfEmails, numberOfSpamEmails, numberOfHamEmails);
    }

}
