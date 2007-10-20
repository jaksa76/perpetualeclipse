package perpetualeclipse.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;



@XStreamAlias("build-summary") public 
class BuildSummary extends XMLReport {
    @XStreamAsAttribute private int numberOfErrors;
    @XStreamAsAttribute private int numberOfTests;
    @XStreamAsAttribute private int numberOfTestFailures;

    public BuildSummary(BuildReport report) {
        this.numberOfErrors = report.getNumberOfErrors();
        this.numberOfTests = report.getNumberOfTests();
        this.numberOfTestFailures = report.getNumberOfTestFailures();
    }

    public String toHTML() {
        return "";
    }

    public int getNumberOfErrors() {
        return numberOfErrors;
    }

    public int getNumberOfTestFailures() {
        return numberOfTestFailures;
    }

    public int getNumberOfTests() {
        return numberOfTests;
    }
}
