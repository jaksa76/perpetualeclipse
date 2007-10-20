package perpetualeclipse.report;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * @author Jaksa
 */
public class BuildReportTest extends TestCase {
    public void testBuildReport() throws Exception {
        BuildReport report = createBuildReportWithOneErrorAndOneFailure();
        
        assertEquals(1, report.getNumberOfErrors());
        assertEquals(2, report.getNumberOfTests());
        assertEquals(1, report.getNumberOfTestFailures());
        
        String xml = report.toXML();
        System.out.println(xml);

        // TODO parsing and asserting the DOM structure is too costly,
        // I should find a more elegant method of checking that the results are ok
        // maybe I should use the Flex client somehow
    }

    public static BuildReport createBuildReportWithOneErrorAndOneFailure() {
        BuildReport report = new BuildReport();
        report.addReport(CompileReportTest.createCompileReportWithError());
        report.addReport(TestReportTest.createTestReportWithFailure());
        return report;
    }

    private Document getDocument(String xml) throws SAXException, IOException {
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader(xml)));
        
        Document doc = parser.getDocument();
        return doc;
    }
}
