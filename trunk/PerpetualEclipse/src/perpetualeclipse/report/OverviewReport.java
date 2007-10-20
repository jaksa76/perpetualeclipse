package perpetualeclipse.report;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("overview")
public class OverviewReport extends XMLReport {

    private List<BuildSummary> builds = new ArrayList<BuildSummary>();
    
    public OverviewReport(List<BuildReport> buildReports) {
        for (BuildReport report : buildReports) {
            builds.add(report.getSummary());
        }
    }
    
    public String toHTML() {
        return "not implemented";
    }
}
