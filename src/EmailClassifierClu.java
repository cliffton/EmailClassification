//import edu.rit.pj2.*;
//
//import java.util.ArrayList;
//
//public class EmailClassifierClu extends Job {
//
//    private ArrayList<Word> wordsMake;
//    private ArrayList<Email> emails;
//
//
//    /**
//     * Job main program.
//     */
//    public void main
//    (String[] args) throws Exception {
//        // Parse command line arguments.
//        if (args.length != 1) usage();
//
//        String ctor = args[0];
//
//        try {
//
//            MakeScores ms = new MakeScores();
//            wordsMake = ms.getWords();
//            emails = ms.getClassifiedEmails();
//            masterChunk(50);
//
//            // Lower bound is inclusive so -1, also -2 for last 2 points.
//            masterFor(0, emails.size() - 2, WorkerTask.class).args(ctor);
//
//            // Set up reduction task.
//            rule().atFinish().task(ReduceTask.class).args(ctor)
//                    .runInJobProcess();
//
//        } catch (Exception e) {
//            usage();
//        }
//    }
//
//
//    /**
//     * Prints usage incase of error.
//     */
//    private static void usage() {
//        System.err.println("Usage: java pj2 [workers=<K>] LargestTriangleClu <ctor>");
//        System.err.println("<K> = Number of worker tasks (default: 1)");
//        System.err.println("<ctor> = Constructor Expression");
//        terminate(1);
//    }
//
//
//    // Task subclass.
//
//    private static class WorkerTask
//            extends Task {
//
//
//        private ArrayList<Word> wordsMake;
//        private ArrayList<Email> emails;
//        private ArrayList<Email> unclassifiedEmail;
//        private Email unclassified;
//        private NeighbourVbl neighbourVbl;
//        int N;
//        int size;
//        int rank;
//        int lb, ub, len;
//
//        /**
//         * Task main program.
//         */
//        public void main(String[] args) throws Exception {
//
//            String ctor = args[0];
//            N = Integer.parseInt(args[1]);
//            size = groupSize();
//            rank = taskRank();
//
//
//            // Allocate storage for next positions for just this worker's slice
//            // of zombies.
//            Chunk slice = Chunk.partition(0, N - 1, size, rank);
//            lb = slice.lb();
//            ub = slice.ub();
//            len = (int) slice.length();
//
//
//            int k = 10;
//
//
//            MakeScores ms = new MakeScores();
//            wordsMake = ms.getWords();
//            emails = ms.getClassifiedEmails();
//            neighbourVbl = new NeighbourVbl(k);
//
//            WorkQueue<Email> emailQueue = new WorkQueue<>();
//            for (Email email : emails) {
//                emailQueue.add(email);
//            }
//
//            while (lb < ub) {
//
//                unclassified = unclassifiedEmail.get(lb);
//                neighbourVbl.reset();
//                lb++;
//
//                parallelFor(emailQueue).exec(new ObjectLoop<Email>() {
//
//
//                    NeighbourVbl thrNeighbourVbl;
//                    //Email thrUnclassified;
//
//                    public void start() {
//                        thrNeighbourVbl = threadLocal(neighbourVbl);
//                        //thrUnclassified = unclassified;
//
//                    }
//
//
//                    public void run(Email email) {
//                        double similarityScore = thrNeighbourVbl.cosineSimilarity(email, unclassified, wordsMake);
//                        thrNeighbourVbl.addNeighbour(similarityScore, email);
//                    }
//                });
//            }
//
//            // TODO :
//
//            // Puts the tuple with the largest result
//            // for this task into tuple space.
////            putTuple(triangleVbl);
//
//        }
//    }
//
//
//    private static class ReduceTask
//            extends Task {
//
//
//        /**
//         * Reduce task main program.
//         */
//        public void main
//        (String[] args)
//                throws Exception {
//            String ctor = args[0];
//            int k = 10;
//
//            ResultTuple template = new ResultTuple();
//            ResultTuple resultTuple = new ResultTuple();
//            ResultTuple current;
//            while ((current = tryToTakeTuple(template)) != null) {
//                resultTuple.combine(current);
//            }
//
//            // Print solution
//            //triangleVbl.print(plane);
//        }
//    }
//
//
//}
