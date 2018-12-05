import edu.rit.pj2.vbl.LongVbl;
import edu.rit.pjmr.*;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class MakeIdfScore extends PjmrJob<TextId,String,String,LongVbl> {

//    private String[] files = {
//            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/ham.csv",
//            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/spam.csv",
//            "/home/stu2/s18/nhk8621/Courses/Parallel/project/dataFiles/unclassified100.csv"
//    };

//    private String[] files = {
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\ham.csv",
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\spam.csv",
//            "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\dataFiles\\unclassified.csv",
//    };
    public static HashMap<String, Word> allWords = new HashMap<>();
    public void main(String[] args) {
        int NT = Math.max(threads(), 1);
        for (int i = 0;i < args.length - 1; i++)
            mapperTask("dr0")
                    .source (new TextFileSource(args[i]))
                    .mapper (NT, MyMapper.class);
        reducerTask().reducer(MyReducer.class, args);
        startJob();

    }

    private static void usage() {
        System.err.println("Usage: java pj2 [threads=<NT>] edu.rit.pjmr.example.WebLog01 <nodes> <file> [<pattern>]");
        terminate(1);
    }

    private static class MyMapper extends Mapper<TextId,String,String,LongVbl> {
        private static final LongVbl ONE = new LongVbl.Sum (1L);
        int count;
        @Override
        public void start(String[] strings, Combiner<String, LongVbl> combiner) {
            count = 0;
        }

        public void map(TextId id, String contents, Combiner<String, LongVbl> combiner) {
            String data[];
            int cactegory = Integer.parseInt(contents.split(",")[1]);
            data =  contents.split(",")[2].split(" ");
            Word w;
            HashSet<String> record = new HashSet<>();
            combiner.add("", new LongVbl.Sum (1L));
            for(String i: data) {
                if(!record.contains(i) && !i.equals("")) {
                    w = new Word(i);
                    w.category = cactegory;
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
        Word w;
        Writer writer = null;
        StringBuilder sb;
        LongVbl count;
        String s;
        @Override
        public void start(String[] strings) {
            s = strings[3];
        }

        @Override
        public void reduce(String key, LongVbl value) // Number of requests
        {
            if(!key.equals("")) {
                w = new Word(key, value.item);
                allWords.put(key, w);
            }
            else {
                count = value;
            }
        }

        @Override
        public void finish() {
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(s)));
                sb = new StringBuilder();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String s = "";
            for(String i: allWords.keySet()) {
                    sb.append(i).append(",").append(allWords.get(i).makeIDF(count.item)).append("\n");
            }
            try {
                writer.write(sb.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}