package report;

import java.util.ArrayList;
import java.util.List;

public class Report {
	private List<UpdateReport> updates = new ArrayList<UpdateReport>(); 
	private List<BuildReport> builds = new ArrayList<BuildReport>();
	private List<TestReport> tests = new ArrayList<TestReport>();
	
	public void addUpdateReport(UpdateReport updateReport) {
		this.updates.add(updateReport);
	}
	
	public void addBuildReport(BuildReport buildReport) {
		builds.add(buildReport);
	}
	
	public void addTestReport(TestReport testReport) {
		tests.add(testReport);
	}
	
	public String toHTML() {
		String html = "";
		for (UpdateReport report : updates) { html += report.toHTML(); }
		for (BuildReport report : builds) { html += report.toHTML(); }
		for (TestReport report : tests) { html += report.toHTML(); }
		return html;
	}
}
