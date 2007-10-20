package perpetualeclipse.report;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("build-report")
public class BuildReport extends XMLReport {
	private List<Report> reports = new ArrayList<Report>(); 
	
	public void addReport(Report report) {
		reports.add(report);
	}
	
	public String toHTML() {
		String html = "";
		for (Report report : reports) { html += report.toHTML(); }
		return html;
	}

    public int getNumberOfErrors() {
        int errors = 0;
        for (Report report : reports) {
            if (report instanceof CompileReport) {
                CompileReport compileReport = (CompileReport) report;
                errors += compileReport.getNumberOfErrors();
            }
        }
        return errors;
    }

    public int getNumberOfTests() {
        int tests = 0;
        for (Report report : reports) {
            if (report instanceof TestReport) {
                TestReport testReport = (TestReport) report;
                tests += testReport.getNumberOfTests();
            }
        }
        return tests;
    }

    public int getNumberOfTestFailures() {
        int failures = 0;
        for (Report report : reports) {
            if (report instanceof TestReport) {
                TestReport testReport = (TestReport) report;
                failures += testReport.getNumberOfFailures();
            }
        }
        return failures;
    }
    
    public BuildSummary getSummary() {
        return new BuildSummary(this);
    }
}
