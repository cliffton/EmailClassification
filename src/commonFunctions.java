import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class commonFunctions {
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

    public void output(ArrayList<Email> result, String Filename) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File((Filename)));
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
