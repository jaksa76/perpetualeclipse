package perpetualeclipse;

import java.util.ArrayList;
import java.util.List;

import perpetualeclipse.report.BuildReport;


public class History {
	private static History instance = new History();
	
	public static History getInstance() { return instance; }

	private BuildReport latestReport;
	private List<BuildReport> reports = new ArrayList<BuildReport>();
	
	public BuildReport getLatestReport() {
		return latestReport;
	}
	
	public void addReport(BuildReport report) {
		reports.add(report);
		this.latestReport = report;
	}
}
