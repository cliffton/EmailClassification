// ###############################################################################
//
// This file is used save the number of emails in the data set
//
// Autjor: Nikhil Keswaney, Cliffton Fernandes
// Last Modified: 07-Dec-2018
//
// ###############################################################################

/**
 * This class is used to keep a record of the count of all the emails
 */
public class CountOfEmails {
    private int numberOfEmails, numberOfSpamEmails, numberOfHamEmails;

    /**
     * This constructor is used to create an object using all the values
     * @param numberOfEmails total number of emails including spam ham unclassified
     * @param numberOfSpamEmails Total number of spam emails
     * @param numberOfHamEmails Total number of ham emails
     */
    public CountOfEmails(int numberOfEmails, int numberOfSpamEmails, int numberOfHamEmails){
        this.numberOfEmails = numberOfEmails;
        this.numberOfHamEmails = numberOfHamEmails;
        this.numberOfSpamEmails = numberOfSpamEmails;
    }

    /**
     * This method is used to got the total number of emails
     * @return Total emails
     */
    public int getNumberOfEmails() {
        return numberOfEmails;
    }

    /**
     * This method is used to got the total number of spam emails
     * @return Total spam emails
     */
    public int getNumberOfSpamEmails() {
        return numberOfSpamEmails;
    }

    /**
     * This method is used to got the total number of ham emails
     * @return Total ham emails
     */
    public int getNumberOfHamEmails() {
        return numberOfHamEmails;
    }
}
