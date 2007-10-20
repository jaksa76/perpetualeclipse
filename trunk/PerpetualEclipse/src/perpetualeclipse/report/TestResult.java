/*
* We reserve all rights in this document and in the information contained 
* therein. Reproduction, use or disclosure to third parties without express
* authority is strictly forbidden.
* 
* Copyright(c) ALSTOM (Switzerland) Ltd. 2007
*/


package perpetualeclipse.report;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author Jaksa Vuckovic
 */
public class TestResult extends XMLReport {
    @XStreamAsAttribute private final String testName;
    @XStreamAsAttribute private final String classname;
    @XStreamAsAttribute private long startTime;
    @XStreamAsAttribute public long duration;

    @XStreamAsAttribute public boolean failed = false;
    private String reason;

    public TestResult(String name, String classname) {
        this.testName = name;
        this.classname = classname;
    }

	public String getTestName() { return testName; }
	public String getClassName() { return classname; }
	
	public void started() { this.startTime = System.currentTimeMillis(); }
	public void finished() { this.duration = System.currentTimeMillis() - this.startTime; }

	public void failed(String reason) { this.failed = true; this.reason = reason; }
	public boolean hasFailed() { return failed; }
	
	public String toHTML() {
		if (failed) {
			return "<li><font color=\"red\">" + testName + " : " + reason + "</font></li>";  
		} else {
			return "<li><font color=\"green\">" + testName + "</font></li>";
		}
	}

	public String getReason() { return reason; }
}
