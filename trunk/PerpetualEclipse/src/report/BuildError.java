/**
 * 
 */
package report;

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