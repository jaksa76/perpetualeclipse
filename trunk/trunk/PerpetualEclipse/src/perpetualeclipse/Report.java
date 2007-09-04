package perpetualeclipse;

import java.util.ArrayList;
import java.util.List;

public class Report {
	private static Report current;
	
	public static Report getCurrentReport() {
		return current;
	}
	
	List<UpdateReport> updates = new ArrayList<UpdateReport>(); 
	List<BuildReport> builds = new ArrayList<BuildReport>();
	List<TestReport> tests = new ArrayList<TestReport>();
	
	public Report() {
		current = this;
	}
	
	public UpdateReport createUpdateReport(String name) {
		UpdateReport updateReport = new UpdateReport(name);
		this.updates.add(updateReport);
		return updateReport;
	}
	
	public BuildReport createBuildReport(String projectName) {
		BuildReport buildReport = new BuildReport(projectName);
		builds.add(buildReport);
		return buildReport;
	}
	
	public TestReport createTestReport(String configurationName) {
		TestReport testReport = new TestReport(configurationName);
		tests.add(testReport);
		return testReport;
	}
	
	public String toHTML() {
		String html = "";
		for (UpdateReport report : updates) { html += report.toHTML(); }
		for (BuildReport report : builds) { html += report.toHTML(); }
		for (TestReport report : tests) { html += report.toHTML(); }
		return html;
	}
	
	public class UpdateReport {
		private String name;
		private int numberOfUpdatedResources;
		
		public UpdateReport(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getResourcesUpdated() {
			return numberOfUpdatedResources;
		}

		public void setResourcesUpdated(int resourcesUpdated) {
			this.numberOfUpdatedResources = resourcesUpdated;
		}
		
		public String toHTML() {
			if (numberOfUpdatedResources == 0) return "<p>No resources updated for project " + name + "</p>";
			else return "<p>" + numberOfUpdatedResources + " resources updated for project " + name + "</p>";
		}
	}
	
	public class BuildReport {
		private final String projectName;
		private List<BuildError> errors = new ArrayList<BuildError>();

		public BuildReport(String projectName) {
			this.projectName = projectName;
		}

		public String getProjectName() {
			return projectName;
		}
		
		public void addError(String errorMessage) {
			this.errors.add(new BuildError(errorMessage));
		}
		
		public String toHTML() {
			if (errors.size() == 0) return "<p>Build of " + projectName + " successful.</p>";
			else {
				String html = "<p>Build of " + projectName + " failed <ul>";
				for (BuildError error : errors) {
					html += error.toHTML();
				}
				html += "</ul></p>";
				return html;
			}
		}
	}
	
	
	public class BuildError {
		private final String message;

		public BuildError(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
		
		public String toHTML() {
			return "<li><font color=\"red\">" + message + "</font></li>";  
		}
	}
	
	
	public class TestReport {
		private final String configurationName;
		private List<TestResult> tests = new ArrayList<TestResult>();
		private int failures = 0;

		public TestReport(String configurationName) {
			this.configurationName = configurationName;
			
		}

		public String getConfigurationName() {
			return configurationName;
		}
		
		public void addTestSuccess(String testName) {
			tests.add(new TestSuccess(testName));
		}
		
		public void addTestFailure(String testName, String errorMessage) {
			tests.add(new TestFailure(testName, errorMessage));
			failures++;
		}

		public int getFailures() {
			return failures;
		}
		
		public String toHTML() {
			String html = "<p>Test results of <b>" + configurationName + "</b>"; 
			html += "<ul>";
			for (TestResult test : tests) {
				html += test.toHTML();
			}
			html += "</ul>";
			html += "total tests: " + tests.size();
			if (failures > 0) html += "<br>falures: " + failures;
			html += "</p>";
			return html;
		}
	}
	
	public abstract class TestResult {
		protected final String testName;

		public TestResult(String testName) { this.testName = testName; }

		public String getTestName() { return testName; }
		
		public abstract boolean success();
		public abstract String toHTML();
	}
	
	
	public class TestSuccess extends TestResult {
		public TestSuccess(String testName) { super(testName); }
		public boolean success() { return true; }
		public String toHTML() {
			return "<li><font color=\"green\">" + testName + "</font></li>";  
		}
	}
	

	public class TestFailure extends TestResult {
		private final String reason;

		public TestFailure(String testName, String reason) {
			super(testName);
			this.reason = reason;
		}
		
		public boolean success() { return false; }
		
		public String toHTML() {
			return "<li><font color=\"red\">" + testName + " : " + reason + "</font></li>";  
		}
	}
}
