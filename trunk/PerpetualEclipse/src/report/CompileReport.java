/**
 * 
 */
package report;

import java.util.ArrayList;
import java.util.List;


public class CompileReport {
	private final String projectName;
	private List<CompileError> errors = new ArrayList<CompileError>();

	public CompileReport(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}
	
	public void addError(String errorMessage) {
		this.errors.add(new CompileError(errorMessage));
	}
	
	public String toHTML() {
		if (errors.size() == 0) return "<p>Build of " + projectName + " successful.</p>";
		else {
			String html = "<p>Build of " + projectName + " failed <ul>";
			for (CompileError error : errors) {
				html += error.toHTML();
			}
			html += "</ul></p>";
			return html;
		}
	}
}