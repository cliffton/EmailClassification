public class CountOfEmails {
    private int numberOfEmails, numberOfSpamEmails, numberOfHamEmails;
    public CountOfEmails(int numberOfEmails, int numberOfSpamEmails, int numberOfHamEmails){
        this.numberOfEmails = numberOfEmails;
        this.numberOfHamEmails = numberOfHamEmails;
        this.numberOfSpamEmails = numberOfSpamEmails;
    }

    public int getNumberOfEmails() {
        return numberOfEmails;
    }

    public int getNumberOfSpamEmails() {
        return numberOfSpamEmails;
    }

    public int getNumberOfHamEmails() {
        return numberOfHamEmails;
    }
}
