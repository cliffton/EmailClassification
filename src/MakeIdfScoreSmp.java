// ##################################################################
//
// This program is used to create a CSV file of the Inverse Document
// Frequency (IDF) of all the words. in sequence
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes.
// Last Modified: 07-Dec-2018.
// usage: java pj2 jar=<jar> threads=<NT> MakeIdfScoreSmp <HAM> <SPAM> <UNCLASSIFIED> <IDF> <CLASSIFIED>
//                a. <HAM> The location of the classified Ham file.
//                b. <SPAM> The location of the classified SPAM file.
//                c. <UNCLASSIFIED> The location of the unclassified file.
//                similarity. <IDF> The location of the IDF file where the results should be stored.
//                d. <CLASSIFIED> This is an optional input if you want the classified data to
////                   go in a particular directory
// ##################################################################

import edu.rit.pj2.vbl.LongVbl;
import edu.rit.pjmr.*;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This is the pjmr class which is used for Map-Reduce of the IDF scores.
 */
public class MakeIdfScoreSmp extends PjmrJob<TextId, String, String, LongVbl> {

    // This is used to store the IDF of all the words in all the emails
    public static HashMap<String, Word> allWords = new HashMap<>();


    /**
     * The main program.
     * @param args The arguments passed to the main program.
     */
    public void main(String[] args) {
        if(args.length < 3) usage();

        // Set the number of threads to use in the node.
        int NT = Math.max(threads(), 1);

        // Parse through the args list and pass each arg to each mapper to process
        for (int i = 0;i < args.length - 1; i++)
            mapperTask("dr0")
                    .source (new TextFileSource(args[i]))
                    .mapper (NT, MyMapper.class);

        // Reduce task
        reducerTask().reducer(MyReducer.class, args);
        startJob();

    }

    /**
     * Usage of the the file to run
     */
    private static void usage() {
        System.err.println("" +
                            "java pj2 jar=<jar> threads=<NT> MakeIdfScoreSmp <HAM> <SPAM> <UNCLASSIFIED> <IDF> <CLASSIFIED>\n" +
                            "a. <HAM> The location of the classified Ham file.\n" +
                            "b. <SPAM> The location of the classified SPAM file.\n" +
                            "c. <UNCLASSIFIED> The location of the unclassified file.\n" +
                            "similarity. <IDF> The location of the IDF file where the results should be stored.\n" +
                            "d. <CLASSIFIED> This is an optional input if you want the classified data to go in a particular directory"
        );
        terminate(1);
    }

    /**
     * Mapper class
     */
    private static class MyMapper extends Mapper<TextId,String,String,LongVbl> {
        // LongVbl to keep an object to
        private static final LongVbl ONE = new LongVbl.Sum (1L);

        public void map(TextId id, String contents, Combiner<String, LongVbl> combiner) {
            String data[];

            // The category of the email being processed
            data =  contents.split(",")[2].split(" ");

            // To keep a record of whether that word occured in the email
            // already or not
            HashSet<String> record = new HashSet<>();

            // This is a special case and "" will have the total count
            // i.e. Total number of doccuments
            combiner.add("", new LongVbl.Sum (1L));

            for(String i: data) {
                if(!record.contains(i) && !i.equals("")) {
                    combiner.add(i, ONE);
                    record.add(i);
                }
            }
        }
    }

    /**
     * Reducer class.
     */
    private static class MyReducer
            extends Reducer<String, LongVbl> {

        // Current word object.
        Word wordRightNow;

        // Writer object to write to the file
        Writer writer = null;

        // String builder object to create the data of the file
        StringBuilder sb;

        // VBL object for storing the total count of all the doccuments
        LongVbl count;

        // Location where the output should be stored
        String outputFileLocation;

        commonFunctions cf = null;
        /**
         * Start function of the reduce
         * @param strings arguments passed to the reduce function
         */
        @Override
        public void start(String[] strings) {
            if(strings.length == 4 ){
                outputFileLocation = strings[3];
            }
            else {
                outputFileLocation = "IDF.csv";
            }
            cf = new commonFunctions();
            sb = new StringBuilder();
        }

        /**
         * Reduce function of the reducer
         * @param key Key of the combiner
         * @param value Keys corresponding value in the combiner
         */
        @Override
        public void reduce(String key, LongVbl value) // Number of requests
        {
            if(!key.equals("")) {
                wordRightNow = new Word(key, value.item);
                allWords.put(key, wordRightNow);
            }
            else {
                count = value;
            }
        }

        @Override
        public void finish() {
            String s = "";

            // Run through all the values in the combiner and appending it
            // to the the sb and the calculation of the IDF score.
            for(String i: allWords.keySet()) {
                    sb.append(i).append(",").
                            append(allWords.get(i).
                                    makeIDF(count.item)).append("\n");
            }

            cf.writingToFile(outputFileLocation, sb);

        }
    }
}