package perpetualeclipse.report;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class OverviewReportTest extends TestCase {
    public void testOverviewReport() throws Exception {
        List<BuildReport> buildReports = new ArrayList<BuildReport>();
        buildReports.add(BuildReportTest.createBuildReportWithOneErrorAndOneFailure());
        OverviewReport report = new OverviewReport(buildReports);
        System.out.println(report.toXML());
    }
}
