package perpetualeclipse;

import java.util.ArrayList;
import java.util.List;

import report.Report;

public class History {
	private static History instance = new History();
	
	public static History getInstance() { return instance; }

	private Report latestReport;
	private List<Report> reports = new ArrayList<Report>();
	
	public Report getLatestReport() {
		return latestReport;
	}
	
	public void addReport(Report report) {
		reports.add(report);
		this.latestReport = report;
	}
}
