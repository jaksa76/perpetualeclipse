/**
 * 
 */
package perpetualeclipse.report;

public class CompileError {
	private final String message;

	public CompileError(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public String toHTML() {
		return "<li><font color=\"red\">" + message + "</font></li>";  
	}
}