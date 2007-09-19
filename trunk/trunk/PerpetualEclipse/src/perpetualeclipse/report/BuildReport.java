package perpetualeclipse.report;

import java.util.ArrayList;
import java.util.List;

public class BuildReport implements Report {
	private List<Report> reports = new ArrayList<Report>(); 
	
	public void addReport(Report report) {
		reports.add(report);
	}
	
	public String toHTML() {
		String html = "";
		for (Report report : reports) { html += report.toHTML(); }
		return html;
	}
}
