/**
 * 
 */
package perpetualeclipse.report;

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