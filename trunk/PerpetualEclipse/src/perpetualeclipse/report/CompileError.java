/**
 * 
 */
package perpetualeclipse.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("error")
public class CompileError extends XMLReport {
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