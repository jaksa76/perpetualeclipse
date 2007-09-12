/**
 * 
 */
package report;

import java.util.ArrayList;
import java.util.List;


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