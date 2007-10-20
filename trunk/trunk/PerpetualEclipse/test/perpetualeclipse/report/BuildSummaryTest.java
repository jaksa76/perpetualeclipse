package perpetualeclipse.report;

import junit.framework.TestCase;

public class BuildSummaryTest extends TestCase {
    public void testBuildSummary() throws Exception {
        BuildSummary summary = new BuildSummary(BuildReportTest.createBuildReportWithOneErrorAndOneFailure());
        
        assertEquals(1, summary.getNumberOfErrors());
        assertEquals(2, summary.getNumberOfTests());
        assertEquals(1, summary.getNumberOfTestFailures());
        
        System.out.println(summary.toXML());
    }
}
