package perpetualeclipse.report;


public class TestReportTest extends junit.framework.TestCase {
    private static final String ERROR_MESSAGE = "Some error message";
    private static final String CONFIGURATION_NAME = "my-configuration";

    public void testTestReport() throws Exception {
        TestReport report = createTestReportWithFailure();
        
        assertEquals(CONFIGURATION_NAME, report.getConfigurationName());
        assertEquals(2, report.getNumberOfTests());
        assertEquals(1, report.getNumberOfFailures());
    }
    
    public static TestReport createTestReportWithFailure() {
        TestReport report = new TestReport(CONFIGURATION_NAME);
        TestResult testSuccess = new TestResult("testSuccess", "SomeTestClass");
        testSuccess.finished();
        TestResult testFailure = new TestResult("testFailure", "SomeTestClass");
        testFailure.failed(ERROR_MESSAGE);
        report.addTestCase(testSuccess);
        report.addTestCase(testFailure);
        return report;
    }
}
