package perpetualeclipse.report;

import junit.framework.TestCase;

/**
 * @author Jaksa
 */
public class CompileReportTest extends TestCase {
    private static final String PROJECT_NAME = "test-project";
    private static final String ERROR_MESSAGE = "some-error";

    public void testCompileReport() throws Exception {
        CompileReport report = createCompileReportWithError();
        
        String xml = report.toXML();
        System.out.println(xml);

        // TODO parsing and asserting the DOM structure is too costly,
        // I should find a more elegant method of checking that the results are ok
        // maybe I should use the Flex client somehow
    }

    public static CompileReport createCompileReportWithError() {
        CompileReport report = new CompileReport(PROJECT_NAME);        
        report.addError(ERROR_MESSAGE);
        return report;
    }
}
