public class TotalEmails {
    int totalEmails;
    int spamEmails;
    int hamEmails;

    TotalEmails(int totalEmails, int spamEmails, int hamEmails){
        this.totalEmails = totalEmails;
        this.spamEmails = spamEmails;
        this.hamEmails = hamEmails;
    }

    public int getSpamEmails(){
        return this.spamEmails;
    }

    public int getHamEmails(){
        return this.hamEmails;
    }
}
