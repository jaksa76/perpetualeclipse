/**
 * 
 */
package report;

import java.util.ArrayList;
import java.util.List;



public class TestReport {
	private final String configurationName;
	private final List<TestCase> tests = new ArrayList<TestCase>();
	private int failures = 0;

	public TestReport(String configurationName) {
		this.configurationName = configurationName;
	}

	public String getConfigurationName() {
		return configurationName;
	}
	
	public void addTestCase(TestCase testResult) {
		tests.add(testResult);
		if (testResult.failed) failures++;
	}
	
	public int getFailures() {
		return failures;
	}
	
	public String toHTML() {
		String html = "<p>Test results of <b>" + configurationName + "</b>"; 
		html += "<ul>";
		for (TestCase test : tests) {
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