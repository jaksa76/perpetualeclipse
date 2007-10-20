package perpetualeclipse.webinterface;

import java.util.Map;

import perpetualeclipse.History;
import perpetualeclipse.report.BuildReport;

public class SimpleReportContentProvider implements ContentProvider {
    private History history = History.getInstance();
    
	public String invoke(Map parameters) {
		StringBuilder content = new StringBuilder();
		content.append("<html><body>");
        BuildReport latestReport = history.getLatestReport();
		if (latestReport != null) content.append(latestReport.toHTML());
		content.append("</body></html>");
		return content.toString();
	}
    
    
    /**
     * Used for testing.
     * @param history
     */
    void setHistory(History history) {
        this.history = history;
    }
}
