import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class makeScores {
    public static void main(String[] args) throws IOException {
        String CSVFile = "C:\\Nikhil\\fall2018\\parallel\\Project\\EmailClassification\\src\\emails1.csv";
        String split = ",";
        String line = "";
        ArrayList<Email> email = new ArrayList<>();
        String[] content;
        HashMap<String, Word> IDF =new HashMap<>();
        int numberOfEmails = 0, numberOfSpamEmails = 0, numberOfHamEmails = 0;
        BufferedReader br = new BufferedReader(new FileReader(CSVFile));
        while ((line = br.readLine()) != null){
            numberOfEmails ++;
            content = line.split(split);
            try {
                email.add(new Email(content[1], Integer.parseInt(content[2])));
            }
            catch (Exception e){
                System.out.printf("ss");
            }
        }

//        email.add("Message-ID: <13505866.1075863688222.JavaMail.evans@thyme> Date: Mon, 23 Oct 2000 06:13:00 -0700 (PDT) From: phillip.allen@enron.com To: randall.gay@enron.com Subject:  Mime-Version: 1.0 Content-Type: text/plain; charset=us-ascii Content-Transfer-Encoding: 7bit X-From: Phillip K Allen X-To: Randall L Gay X-cc:  X-bcc:  X-Folder: \\Phillip_Allen_Dec2000\\Notes Folders\\'sent mail X-Origin: Allen-P X-FileName: pallen.nsf  Randy,   Can you send me a schedule of the salary and level of everyone in the  scheduling group.  Plus your thoughts on any changes that need to be made.   (Patti S for example)  Phillip\\\" \\\"allen-p/_sent_mail/1001.\\\",\\\"Message-ID: <30922949.1075863688243.JavaMail.evans@thyme> Date: Thu, 31 Aug 2000 05:07:00 -0700 (PDT) From: phillip.allen@enron.com To: greg.piper@enron.com Subject: Re: Hello Mime-Version: 1.0 Content-Type: text/plain; charset=us-ascii Content-Transfer-Encoding: 7bit X-From: Phillip K Allen X-To: Greg Piper X-cc:  X-bcc:  X-Folder: \\Phillip_Allen_Dec2000\\Notes Folders\\'sent mail X-Origin: Allen-P X-FileName: pallen.nsf  Let's shoot for Tuesday at 11:45.");
//        email.add("Message-ID: <15464986.1075855378456.JavaMail.evans@thyme> Date: Fri, 4 May 2001 13:51:00 -0700 (PDT) From: phillip.allen@enron.com To: john.lavorato@enron.com Subject: Re: Mime-Version: 1.0 Content-Type: text/plain; charset=us-ascii Content-Transfer-Encoding: 7bit X-From: Phillip K Allen X-To: John J Lavorato <John J Lavorato/ENRON@enronXgate@ENRON> X-cc:  X-bcc:  X-Folder: \\Phillip_Allen_Jan2002_1\\Allen, Phillip K.\\'Sent Mail X-Origin: Allen-P X-FileName: pallen (Non-Privileged).pst  Traveling to have a business meeting takes the fun out of the trip.  Especially if you have to prepare a presentation.  I would suggest holding the business plan meetings here then take a trip without any formal business meetings.  I would even try and get some honest opinions on whether a trip is even desired or necessary.  As far as the business meetings, I think it would be more productive to try and stimulate discussions across the different groups about what is working and what is not.  Too often the presenter speaks and the others are quiet just waiting for their turn.   The meetings might be better if held in a round table discussion format.    My suggestion for where to go is Austin.  Play golf and rent a ski boat and jet ski's.  Flying somewhere takes too much time.");
//        email.add("Message-ID: <18782981.1075855378110.JavaMail.evans@thyme> Date: Mon, 14 May 2001 16:39:00 -0700 (PDT) From: phillip.allen@enron.com To: tim.belden@enron.com Subject:  Mime-Version: 1.0 Content-Type: text/plain; charset=us-ascii Content-Transfer-Encoding: 7bit X-From: Phillip K Allen X-To: Tim Belden <Tim Belden/Enron@EnronXGate> X-cc:  X-bcc:  X-Folder: \\Phillip_Allen_Jan2002_1\\Allen, Phillip K.\\'Sent Mail X-Origin: Allen-P X-FileName: pallen (Non-Privileged).pst  Here is our forecast ");
        Iterator itr = email.iterator();
        Email emailCurrent;
        String[] wordsInCurrentEmail;
        Word WordInEmail;

        while (itr.hasNext()) {
            emailCurrent = (Email) itr.next();
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                if (!IDF.containsKey(wordsInCurrentEmail[i])) {
                    WordInEmail = new Word(wordsInCurrentEmail[i]);
                    IDF.put(wordsInCurrentEmail[i], WordInEmail);
                } else {
                    WordInEmail = IDF.get(wordsInCurrentEmail[i]);
                }
                WordInEmail.setIDFScore(WordInEmail.getIDFScore() + 1);
                emailCurrent.setWord(WordInEmail);
            }
            emailCurrent.makeTF();
        }
        itr = email.iterator();
        int categoryCounter = 0;
        int categoryOfTheEmail;
        while (itr.hasNext()){
            emailCurrent = (Email) itr.next();
            wordsInCurrentEmail = emailCurrent.getContent().split(" ");
            categoryOfTheEmail = emailCurrent.getCategory();
            for (int i = 0; i < wordsInCurrentEmail.length; i++) {
                WordInEmail = IDF.get(wordsInCurrentEmail[i]);
                if(categoryOfTheEmail == 1){
                    WordInEmail.setDFSpamScore(WordInEmail.getDFSpamScore() + 1);
                    numberOfSpamEmails ++;
                }
                else {
                    WordInEmail.setDFHamScore(WordInEmail.getDFHamScore() + 1);
                    numberOfHamEmails ++;
                }
            }
            categoryCounter ++;

        }
        Word currentWord;
        for(String word: IDF.keySet()){
            currentWord = IDF.get(word);
            currentWord.setSDScore(calculateSD(currentWord.getDFSpamScore(),
                    currentWord.getDFHamScore(), currentWord.getIDFScore()));
        }
        for(String word: IDF.keySet()) {
            currentWord = IDF.get(word);
            currentWord.makeIDF(numberOfEmails);
            currentWord.makeDFScores(numberOfSpamEmails, numberOfHamEmails);
        }
        System.out.printf("ss");
    }
    public static double calculateSD(double xjSpam, double xjHam, double U){

        double SpamPart = (xjSpam - U)/(2 * U);
        double HamPart = (xjHam - U)/(2 * U);
        return Math.sqrt(0.5*(SpamPart*SpamPart + HamPart*HamPart));
    }
}
