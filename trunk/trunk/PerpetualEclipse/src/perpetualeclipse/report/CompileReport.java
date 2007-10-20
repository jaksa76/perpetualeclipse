/**
 * 
 */
package perpetualeclipse.report;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("compile-report")
public class CompileReport extends XMLReport {
    public static final String ERROR_XML_TAG = "error";
    public static final String COMPILE_REPORT_XML_TAG = "compile-report";
    
	@XStreamAsAttribute private final String projectName;
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
	
	public int getNumberOfErrors() {
		return errors.size();
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