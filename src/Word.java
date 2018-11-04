public class Word {
    private String word;
    private double IDFScore, DFScoreSpam, SDScore, DFScoreHam, totalWordCount;

    Word() {
    }

    Word(String word) {
        this.word = word;
        this.DFScoreHam = 0;
        this.DFScoreSpam = 0;
        this.SDScore = 0;
        this.totalWordCount = 0;
    }

    Word(String word, double IDFScore, double DFScoreSpam, double SDScore, double DFScoreHam) {
        this.word = word;
        this.IDFScore = IDFScore;
        this.DFScoreSpam = DFScoreSpam;
        this.DFScoreHam = DFScoreHam;
        this.SDScore = SDScore;
    }

    public void setIDFScore(double IDFScore) {
        this.IDFScore = IDFScore;
    }

    public void setTotalWordCount(double totalWordCount) {
        this.totalWordCount = totalWordCount;
    }

    public void setDFSpamScore(double DFScoreSpam) {
        this.DFScoreSpam = DFScoreSpam;
    }

    public void setDFHamScore(double DFScoreHam) {
        this.DFScoreHam = DFScoreHam;
    }

    public void setSDScore(double SDScore) {
        this.SDScore = SDScore;
    }

    public double getIDFScore() {
        return this.IDFScore;
    }

    public double getTotalWordCount() {
        return this.totalWordCount;
    }

    public double getDFSpamScore() {
        return this.DFScoreSpam;
    }

    public double getDFHamScore() {
        return this.DFScoreHam;
    }

    public double getSDScore() {
        return this.SDScore;
    }

    public void makeIDF(int number) {
        this.IDFScore = log2(number / this.IDFScore);
    }

    double log2(double value) {
        return Math.log(value) / Math.log(2);
    }

    public void makeDFScores(int numberOfSpamEmails, int numberOfHamEmails) {
        this.DFScoreSpam = log2(numberOfSpamEmails / this.DFScoreSpam);
        this.DFScoreHam = log2(numberOfHamEmails / this.DFScoreHam);
    }

    @Override
    public String toString() {
        return word;
    }

//    @Override
//    public int compareTo(Object o) {
//        double tfidf = ((Word)o)
//    }
}

