/**
 * 
 */
package perpetualeclipse.report;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("test-report")
public class TestReport extends XMLReport {
	@XStreamAsAttribute private final String configurationName;
	@XStreamAsAttribute private int failures = 0;
	private final List<TestResult> tests = new ArrayList<TestResult>();

	public TestReport(String configurationName) {
		this.configurationName = configurationName;
	}

	public String getConfigurationName() {
		return configurationName;
	}
	
	public void addTestCase(TestResult testResult) {
		tests.add(testResult);
		if (testResult.failed) failures++;
	}
	
	public int getNumberOfFailures() {
		return failures;
	}
	
	public int getNumberOfTests() {
		return tests.size();
	}
	
	public String toHTML() {
		String html = "<p>Test results of <b>" + configurationName + "</b>"; 
		html += "<ul>";
		for (TestResult test : tests) {
			html += test.toHTML();
		}
		html += "</ul>";
		html += "total tests: " + tests.size();
		if (failures > 0) 
			html += "<br>falures: " + failures;
		html += "</p>";
		return html;
	}
}