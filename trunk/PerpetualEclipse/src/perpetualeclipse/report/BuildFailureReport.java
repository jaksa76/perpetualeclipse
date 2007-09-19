package perpetualeclipse.report;

public class BuildFailureReport implements Report {
	private final Exception e;

	public BuildFailureReport(Exception e) {
		this.e = e;
	}
	
	public String toHTML() {
		return null;
	}

}
