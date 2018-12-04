import edu.rit.pj2.vbl.LongVbl;
import edu.rit.pjmr.*;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class MakeIdfScore extends PjmrJob<TextId,String,String,LongVbl> {

    private String[] files = {
            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/ham.csv",
            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/spam.csv",
            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/unclassified100.csv"
    };

//    private String[] files = {
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\ham.csv",
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\spam.csv",
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\unclassified.csv",
//    };
    public static HashMap<String, Word> allWords = new HashMap<>();
    public void main(String[] args) {
        for (String i: files)
            mapperTask()
                    .source (new TextFileSource(i))
                    .mapper (MyMapper.class);

        reducerTask().reducer(MyReducer.class);

        startJob();

        System.out.print("s");
    }

    private static void usage() {
        System.err.println("Usage: java pj2 [threads=<NT>] edu.rit.pjmr.example.WebLog01 <nodes> <file> [<pattern>]");
        terminate(1);
    }

    private static class MyMapper extends Mapper<TextId,String,String,LongVbl> {
        private static final LongVbl ONE = new LongVbl.Sum (1L);

        public void map(TextId id, String contents, Combiner<String, LongVbl> combiner) {
            String data[];
            data =  contents.split(",")[2].split(" ");
            Word word;
            HashSet<String> record = new HashSet<>();
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

        long numberOfEmails;
        MakeScoresSmp makeTF = new MakeScoresSmp();
        Word w;
        @Override
        public void start(String[] strings) {
            try {
                makeTF.main(new String[]{""});
            } catch (IOException e) {
                e.printStackTrace();
            }
            numberOfEmails = MakeScoresSmp.numberOfEmails;
        }

        public void reduce(String key, LongVbl value) // Number of requests
        {
            w = new Word(key, value.item);
            allWords.put(key, w);
            w.makeIDF(numberOfEmails);
        }

        @Override
        public void finish() {
            
        }
    }
}